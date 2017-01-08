/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         JIN obj n <branch>
 */
public class JIN extends ZInstructionBase
{
    public JIN()
    {
        m_code = 0x06;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.objTable.getParent(p.op1) == p.op2 )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
