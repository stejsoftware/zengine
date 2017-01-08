/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;


/**
 * @author jon
 * 
 *         DEC_CHK var s <branch>
 */
public class DEC_CHK extends ZInstructionBase
{
    public DEC_CHK()
    {
        m_code = 0x00;
        m_type = OPCODE_TYPE.None;
    }

    @Override
    public void call(ZCPU p)
    {
        int w;

        w = signedWord(p.getVariable(p.op1));
        w = ((w - 1) % 0x10000);

        p.putVariable(p.op1, w);

        if( w < signedWord(p.op2) )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
