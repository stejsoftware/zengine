/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_WIND_PROP window p <result> V6
 */
public class GET_WIND_PROP extends ZInstructionBase
{
    public GET_WIND_PROP()
    {
        m_code = 0x13;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("GET_WINDOW_PROP instruction unimplemented");
    }
}
