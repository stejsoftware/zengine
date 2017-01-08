/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_NEXT_PROP obj prop <result>
 */
public class GET_NEXT_PROP extends ZInstructionBase
{
    public GET_NEXT_PROP()
    {
        m_code = 0x13;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(p.curResult, p.objTable.getNextProperty(p.op1, p.op2));
    }
}
