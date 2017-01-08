/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SET_MARGINS xl xr window
 */
public class SET_MARGINS extends ZInstructionBase
{
    public SET_MARGINS()
    {
        m_code = 0x08;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("SET_MARGINS instruction unimplemented");
    }
}
