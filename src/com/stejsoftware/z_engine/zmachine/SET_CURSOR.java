/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // SET_CURSOR s x V4-5,7-8
 * 
 *         // SET_CURSOR s x [window] V6
 */
public class SET_CURSOR extends ZInstructionBase
{
    public SET_CURSOR()
    {
        m_code = 0x0f;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.outputFlush();
        p.zui.setCursorPosition(p.vops[1], p.vops[0]);
    }
}
