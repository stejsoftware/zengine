/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import java.util.Stack;
 

/**
 * @author jon
 * 
 *         CALL_PV raddr [a1 a2 a3] V5+
 * 
 */
public class CALL_PV extends ZInstructionBase
{
    public static final int                 CODE = 0x19;
    public static final ZOpCode.OPCODE_TYPE TYPE = ZOpCode.OPCODE_TYPE.VAR;

    public CALL_PV()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
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

        /*
         * System.out.println(Integer.toHexString(p.curCallFrame.pc) + " CALL "
         * + Integer.toHexString(addr) + " " + p.vops[1] + " " + p.vops[2] + " "
         * + p.vops[3]);
         */

        // Get the number of local variables
        numvars = p.memory.fetchByte(addr);

        // Bump the address past the variables byte, in any version
        addr++;

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
        p.curCallFrame.callType = ZCallFrame.PROCEDURE;

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
