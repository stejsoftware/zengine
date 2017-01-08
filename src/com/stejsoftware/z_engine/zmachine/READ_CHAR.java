/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         READ_CHAR 1 [time raddr] <result> V4+
 */
public class READ_CHAR extends ZInstructionBase
{
    public READ_CHAR()
    {
        m_code = 0x16;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        int c;

        p.ioCard.outputFlush();

        if( (p.numvops > 1) && (p.vops[1] != 0) && (p.vops[2] != 0) )
        {
            // A timed READ_CHAR
            while( true )
            {
                // Yuck.
                c = p.ioCard.readChar(p.vops[1]);

                if( c == -1 )
                {
                    // A timeout
                    if( p.interrupt(p.vops[2]) == 0 )
                    {
                        continue;
                    }
                    else
                    {
                        p.putVariable(p.curResult, 0);
                        return;
                    }
                }
                else
                {
                    // A character
                    p.putVariable(p.curResult, c);
                    return;
                }
            }
        }
        else
        {
            c = p.ioCard.readChar(0);
            p.putVariable(p.curResult, c);
        }
    }
}
