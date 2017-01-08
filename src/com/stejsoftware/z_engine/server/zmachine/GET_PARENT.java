/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_PARENT obj <result>
 */
public class GET_PARENT extends ZInstructionBase
{
    public GET_PARENT()
    {
        m_code = 0x03;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        int parent = p.objTable.getParent(p.op1);
        p.putVariable(p.curResult, parent);
    }
}
