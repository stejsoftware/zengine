/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         TEST a b <branch>
 */
public class TEST extends ZInstructionBase
{
    public TEST()
    {
        m_code = 0x07;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        if( (p.op1 & p.op2) == p.op2 )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
