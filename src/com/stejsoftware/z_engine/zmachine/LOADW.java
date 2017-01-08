/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         LOADW baddr n <result>
 */
public class LOADW extends ZInstructionBase
{
    public LOADW()
    {
        m_code = 0x0f;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(p.curResult, p.memory.fetchWord(p.op1 + (2 * p.op2)));
    }
}
