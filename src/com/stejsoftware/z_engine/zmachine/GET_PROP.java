/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_PROP obj prop <result>
 */
public class GET_PROP extends ZInstructionBase
{
    public GET_PROP()
    {
        m_code = 0x11;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(p.curResult, p.objTable.getProperty(p.op1, p.op2));
    }
}
