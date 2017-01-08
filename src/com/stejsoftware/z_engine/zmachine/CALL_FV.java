/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_FV raddr [a1 a2 a3] <result>
 * 
 *         CALL_FD raddr [a1 a2 a3 a4 a5 a6 a7] <result> V4+
 */
public class CALL_FV extends ZInstructionBase
{
    private static Logger           log  = LoggerFactory.getLogger(CALL_FV.class);

    public static final int         CODE = 0x00;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.VAR;

    public CALL_FV()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        int addr;
        int numvars;
        int numargs;
        int newFrameNumber;

        // First, make sure raddr is not 0
        if( p.vops[0] == 0 )
        {
            p.putVariable(p.curResult, 0);
            return;
        }

        // Get the number of arguments
        numargs = p.numvops - 1;

        // Unpack the routine address
        addr = p.unpackAddr(p.vops[0], true);

        log.info(Integer.toHexString(p.curCallFrame.pc) + " CALL " + Integer.toHexString(addr) + " " + p.vops[1] + " " + p.vops[2] + " " + p.vops[3] + " "
                + p.vops[4] + " " + p.vops[5] + " " + p.vops[6] + " " + p.vops[7] + "(" + p.numvops + ")" + " " + Integer.toHexString(p.getVariable(3)) + " "
                + Integer.toHexString(p.getVariable(5)));

        // Get the number of local variables
        numvars = p.memory.fetchByte(addr);

        // Bump the address past the variables byte, in any version
        addr++;

        // Back up the PC to point to the result byte
        p.curCallFrame.pc--;

        // Get the number of the next call frame.
        newFrameNumber = p.curCallFrame.frameNumber + 1;

        // Push the current call frame onto the stack.
        p.callStack.push(p.curCallFrame);

        // Initialize a new call frame
        p.curCallFrame = new ZCallFrame();

        // Put the PC at the appropriate place, depending on
        // whether local variables are present.
        if( p.version < 5 )
            p.curCallFrame.pc = addr + (numvars * 2);
        else
            p.curCallFrame.pc = addr;

        // Create an empty routine stack
        p.curCallFrame.routineStack = new Stack<Integer>();

        // Initialize local variables
        p.curCallFrame.numLocalVars = numvars;

        for( int i = 0; i < numvars; i++ )
        {
            // Fill in an argument in this variable, if one exists.
            if( i < numargs )
            {
                p.curCallFrame.localVars[i] = p.vops[i + 1];
                continue;
            }

            // Otherwise, if this is a pre-V5 game, fill in
            // a local variable.
            if( p.version < 5 )
            {
                p.curCallFrame.localVars[i] = p.memory.fetchWord(addr + (i * 2));
                continue;
            }

            // Otherwise, just make this variable 0.
            p.curCallFrame.localVars[i] = 0;
        }

        // Store the call type (only strictly necessary in V3+)
        p.curCallFrame.callType = ZCallFrame.FUNCTION;

        // Store the number of arguments (only strictly necessary
        // in V5+)
        if( numargs > numvars )
            p.curCallFrame.argCount = numvars;
        else
            p.curCallFrame.argCount = numargs;

        // Store the call frame number
        p.curCallFrame.frameNumber = newFrameNumber;
    }
}
