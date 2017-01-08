/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         ERASE_WINDOW window V4+
 */
public class ERASE_WINDOW extends ZInstructionBase
{
    static int         CODE = 0x0d;
    static OPCODE_TYPE TYPE = OPCODE_TYPE.VAR;

    public ERASE_WINDOW()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        if( signedWord(p.vops[0]) == -1 )
        {
            // Erase everything, do a SPLIT_SCREEN 0
            p.zui.eraseWindow(0);
            p.zui.eraseWindow(1);

            p.vops[0] = 0;
            p.numvops = 1;

            ZCPU.call(p, SPLIT_SCREEN.TYPE, SPLIT_SCREEN.CODE);

            return;
        }
        else
        {
            // In V6, we'll have to handle -2 explicitly
            p.zui.eraseWindow(p.vops[0]);
        }
    }

}
