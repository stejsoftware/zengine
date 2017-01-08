/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         ART_SHIFT a s <result> V5+
 */
public class ART_SHIFT extends ZInstructionBase
{
    public ART_SHIFT()
    {
        m_code = 0x03;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        int val;
        int sop2;

        sop2 = signedWord(p.vops[1]);

        if( sop2 < 0 )
        {
            if( ((p.voptypes[0] == ZCPU.ARGTYPE_WORD) && ((p.vops[0] & 0x8000) == 0x8000))
                    || ((p.voptypes[0] == ZCPU.ARGTYPE_BYTE) && ((p.vops[0] & 0x80) == 0x80)) )
            {
                val = p.vops[0] >> Math.abs(sop2);
            }
            else
            {
                val = p.vops[0] >>> Math.abs(sop2);
            }
        }
        else
        {
            val = p.vops[0] << sop2;
        }

        p.putVariable(p.curResult, val);
    }
}
