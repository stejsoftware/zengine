/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         JZ a <branch>
 */
public class JZ extends ZInstructionBase
{
    public JZ()
    {
        m_code = 0x00;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getBranch(p);

        if( p.op1 == 0 )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }

}
