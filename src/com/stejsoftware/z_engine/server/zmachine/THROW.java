/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         THROW a fp V5+
 */
public class THROW extends ZInstructionBase
{
    public THROW()
    {
        m_code = 0x1c;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        // Pop the stack until we either find the frame being referenced, or the
        // stack underflows (a fatal error).
        while( (p.curCallFrame.frameNumber != p.op2) && (!p.callStack.empty()) )
        {
            p.curCallFrame = p.callStack.pop();
        }

        if( p.curCallFrame.frameNumber != p.op2 ) // Stack underflow
        {
            p.zui.fatal("THROW: Call stack underflow");
        }

        // We have the frame; now do a RET a
        p.call(RET.TYPE, RET.CODE);
    }
}
