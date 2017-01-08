/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_PROP_ADDR obj prop <result>
 */
public class GET_PROP_ADDR extends ZInstructionBase
{
    public GET_PROP_ADDR()
    {
        m_code = 0x12;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(p.curResult, p.objTable.getPropertyAddress(p.op1, p.op2));
    }
}
