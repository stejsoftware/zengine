/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_SIBLING obj <result> <branch>
 */
public class GET_SIBLING extends ZInstructionBase
{
    public GET_SIBLING()
    {
        m_code = 0x01;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        getBranch(p);

        int sib;

        sib = p.objTable.getSibling(p.op1);
        p.putVariable(p.curResult, sib);

        if( sib != 0 )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
