/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         OR a b <result>
 */
public class OR extends ZInstructionBase
{
    public OR()
    {
        m_code = 0x08;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(p.curResult, (p.op1 | p.op2));
    }
}
