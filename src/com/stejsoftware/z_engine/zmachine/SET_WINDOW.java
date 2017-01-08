/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SET_WINDOW window V3+
 */
public class SET_WINDOW extends ZInstructionBase
{
    public SET_WINDOW()
    {
        m_code = 0x0b;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.outputFlush();

        // In V6, -3 represents the current window
        p.zui.setCurrentWindow(p.vops[0]);
    }
}
