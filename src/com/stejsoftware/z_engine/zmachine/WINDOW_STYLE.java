/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         WINDOW_STYLE window flags op V6
 */
public class WINDOW_STYLE extends ZInstructionBase
{
    public WINDOW_STYLE()
    {
        m_code = 0x12;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("WINDOW_STYLE instruction unimplemented");
    }
}
