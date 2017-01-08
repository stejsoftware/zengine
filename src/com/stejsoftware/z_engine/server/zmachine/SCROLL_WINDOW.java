/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SCROLL_WINDOW window s V6
 */
public class SCROLL_WINDOW extends ZInstructionBase
{
    public SCROLL_WINDOW()
    {
        m_code = 0x14;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("SCROLL_WINDOW instruction unimplemented");
    }
}
