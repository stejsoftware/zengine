/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         MOUSE_WINDOW window V6
 */
public class MOUSE_WINDOW extends ZInstructionBase
{
    public MOUSE_WINDOW()
    {
        m_code = 0x17;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("MOUSE_WINDOW instruction unimplemented");
    }
}
