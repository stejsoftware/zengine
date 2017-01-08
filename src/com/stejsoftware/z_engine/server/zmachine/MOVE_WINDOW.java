/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         MOVE_WINDOW window y x V6
 */
public class MOVE_WINDOW extends ZInstructionBase
{
    public MOVE_WINDOW()
    {
        m_code = 0x10;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("MOVE_WINDOW instruction unimplemented");
    }
}
