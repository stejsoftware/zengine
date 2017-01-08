/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         inc_chk var t <branch>
 */
public class INC_CHK extends ZInstructionBase
{
    public INC_CHK()
    {
        m_code = 0x00;
        m_type = OPCODE_TYPE.None;
    }

    @Override
    public void call(ZCPU p)
    {
        int w;

        w = signedWord(p.getVariable(p.op1));
        w = ((w + 1) % 0x10000);

        p.putVariable(p.op1, w);

        if( w > signedWord(p.op2) )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
