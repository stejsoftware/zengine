/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         LOAD var <result> V1+
 */
public class LOAD extends ZInstructionBase
{
    public LOAD()
    {
        m_code = 0x0e;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        p.putVariable(p.curResult, p.getVariable(p.op1));
    }
}
