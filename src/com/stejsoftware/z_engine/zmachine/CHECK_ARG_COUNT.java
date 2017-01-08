/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CHECK_ARG_COUNT n <branch> V5+
 */
public class CHECK_ARG_COUNT extends ZInstructionBase
{
    public CHECK_ARG_COUNT()
    {
        m_code = 0x1f;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        getBranch(p);

        if( p.curCallFrame.argCount >= p.vops[0] )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
