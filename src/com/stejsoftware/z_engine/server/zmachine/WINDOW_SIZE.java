/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         WINDOW_SIZE window y x V6
 */
public class WINDOW_SIZE extends ZInstructionBase
{
    public WINDOW_SIZE()
    {
        m_code = 0x11;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("WINDOW_SIZE instruction unimplemented");
    }
}
