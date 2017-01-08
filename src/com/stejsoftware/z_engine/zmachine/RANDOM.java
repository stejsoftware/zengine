/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RANDOM s <result>
 */
public class RANDOM extends ZInstructionBase
{
    public RANDOM()
    {
        m_code = 0x07;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        
        if( signedWord(p.vops[0]) > 0 )
        {
            p.putVariable(p.curResult, p.rndgen.getRandom(signedWord(p.vops[0])));
        }
        else
        {
            p.rndgen.seed(signedWord(p.vops[0]));
            p.putVariable(p.curResult, 0);
        }
    }
}
