/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         LOG_SHIFT a s <result> V5+
 */
public class LOG_SHIFT extends ZInstructionBase
{
    public LOG_SHIFT()
    {
        m_code = 0x020;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        
        int val = 0;
        int sop2 = signedWord(p.vops[1]);

        if( sop2 < 0 )
        {
            val = p.vops[0] >>> Math.abs(sop2);
        }
        else
        {
            val = p.vops[0] << sop2;
        }

        p.putVariable(p.curResult, val);
    }
}
