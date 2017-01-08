/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CLEAR_ATTR obj attr
 */
public class CLEAR_ATTR extends ZInstructionBase
{
    public CLEAR_ATTR()
    {
        m_code = 0x0c;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.objTable.clearAttribute(p.op1, p.op2);
    }
}
