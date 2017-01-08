/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SET_ATTR obj attr
 */
public class SET_ATTR extends ZInstructionBase
{
    public SET_ATTR()
    {
        m_code = 0x0b;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.objTable.setAttribute(p.op1, p.op2);
    }
}
