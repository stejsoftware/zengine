/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine.zop;

import com.stejsoftware.z_engine.server.zmachine.ZInstructionBase;
import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         ZOP V1+
 */
public class ZOP extends ZInstructionBase
{
    public ZOP()
    {
        m_code = 0x00;
        m_type = OPCODE_TYPE.None;
    }

    @Override
    public void call(ZCPU p)
    {
    }
}
