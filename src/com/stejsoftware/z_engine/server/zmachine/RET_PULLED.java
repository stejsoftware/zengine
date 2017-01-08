/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RET_PULLED
 */
public class RET_PULLED extends ZInstructionBase
{
    public RET_PULLED()
    {
        m_code = 0x08;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        p.op1 = p.getVariable(0);
        p.call(RET.TYPE, RET.CODE);
    }
}
