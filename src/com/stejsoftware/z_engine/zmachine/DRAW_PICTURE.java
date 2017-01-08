/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         DRAW_PICTURE pic [y x] V6
 */
public class DRAW_PICTURE extends ZInstructionBase
{
    public DRAW_PICTURE()
    {
        m_code = 0x05;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("DRAW_PICTURE instruction unimplemented");
    }
}
