/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         ERASE_PICTURE pic [y x] V6
 */
public class ERASE_PICTURE extends ZInstructionBase
{
    public ERASE_PICTURE()
    {
        m_code = 0x07;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("ERASE_PICTURE instruction unimplemented");
    }
}
