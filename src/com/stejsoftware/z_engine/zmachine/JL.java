/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         JL s t <branch>
 */
public class JL extends ZInstructionBase
{
    public static final int         CODE = 0x02;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP2;

    public JL()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        if( signedWord(p.op1) < signedWord(p.op2) )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
