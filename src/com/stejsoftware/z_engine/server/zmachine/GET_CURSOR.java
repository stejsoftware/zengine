/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_CURSOR baddr V4+
 */
public class GET_CURSOR extends ZInstructionBase
{
    public GET_CURSOR()
    {
        m_code = 0x10;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.outputFlush();

        Point pt = p.zui.getCursorPosition();

        p.memory.putWord(p.vops[0], pt.y);
        p.memory.putWord(p.vops[0] + 2, pt.x);
    }
}
