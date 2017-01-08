/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;


/**
 * @author jon
 * 
 *         RET a V1+
 */
public class RET extends ZInstructionBase
{
    public static final int        CODE = 0x0b;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP1;

    public RET()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        // First, make sure we *can* return.
        if( p.callStack.empty() ) p.zui.fatal("Call stack underflow");

        // Now do the appropriate thing for each call type.
        if( p.curCallFrame.callType == ZCallFrame.PROCEDURE )
        {
            p.curCallFrame = p.callStack.pop();
            return;
        }
        else if( p.curCallFrame.callType == ZCallFrame.FUNCTION )
        {
            p.curCallFrame = p.callStack.pop();
            p.curResult = p.memory.fetchByte(p.curCallFrame.pc);
            p.curCallFrame.pc++;
            p.putVariable(p.curResult, p.op1);
            return;
        }
        else if( p.curCallFrame.callType == ZCallFrame.INTERRUPT )
        {
            p.curCallFrame = p.callStack.pop();
            p.decode_ret_flag = true;
            p.ret_value = p.op1;
            return;
        }

        // If we make it here, something is wrong.
        p.zui.fatal("Corrupted call frame");
        return;
    }
}
