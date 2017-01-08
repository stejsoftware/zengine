/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_CHILD obj <result> <branch>
 */
public class GET_CHILD extends ZInstructionBase
{
    public GET_CHILD()
    {
        m_code = 0x02;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        getBranch(p);

        int child = p.objTable.getChild(p.op1);

        p.putVariable(p.curResult, child);

        if( child != 0 )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
