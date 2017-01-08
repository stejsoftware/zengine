/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         COPY_TABLE baddr1 baddr2 s V5+
 */
public class COPY_TABLE extends ZInstructionBase
{
    public COPY_TABLE()
    {
        m_code = 0x1d;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        int i;
        int s;

        s = signedWord(p.vops[2]);

        // If baddr2 is 0, zero the data at baddr1.
        if( p.vops[1] == 0 )
        {
            for( i = 0; i < s; i++ )
                p.memory.putByte(p.vops[0] + i, 0);
            return;
        }

        // If s < 0, copy forwards
        if( s < 0 )
        {
            for( i = 0; i < (-s); i++ )
                p.memory.putByte(p.vops[1] + i, p.memory.fetchByte(p.vops[0] + i));
            return;
        }

        // Otherwise, copy backwards
        for( i = (s - 1); i >= 0; i-- )
            p.memory.putByte(p.vops[1] + i, p.memory.fetchByte(p.vops[0] + i));
        return;
    }
}
