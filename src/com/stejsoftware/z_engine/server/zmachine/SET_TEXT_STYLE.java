/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SET_TEXT_STYLE n V4+
 */
public class SET_TEXT_STYLE extends ZInstructionBase
{
    public SET_TEXT_STYLE()
    {
        m_code = 0x11;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.outputFlush();
        p.zui.setTextStyle(p.vops[0]);

        Dimension s = p.zui.getFontSize();

        p.memory.putByte(0x26, s.height);
        p.memory.putByte(0x27, s.width);
    }
}
