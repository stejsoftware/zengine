/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         OUTPUT_STREAM s V3-4
 * 
 *         OUTPUT_STREAM s [baddr] V5,7-8
 * 
 *         OUTPUT_STREAM s [baddr w] V6
 */
public class OUTPUT_STREAM extends ZInstructionBase
{
    public OUTPUT_STREAM()
    {
        m_code = 0x13;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.numvops == 3 )
        {
            p.ioCard.setOutputStream(signedWord(p.vops[0]), p.vops[1], p.vops[2], true);
        }
        else
        {
            p.ioCard.setOutputStream(signedWord(p.vops[0]), p.vops[1], 0, false);
        }
    }
}
