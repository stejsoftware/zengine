/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PUT_WIND_PROP window p a V6
 */
public class PUT_WIND_PROP extends ZInstructionBase
{
    public PUT_WIND_PROP()
    {
        m_code = 0x19;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("PUT_WIND_PROP instruction unimplemented");
    }
}
