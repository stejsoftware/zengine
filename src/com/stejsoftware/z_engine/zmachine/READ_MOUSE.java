/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         READ_MOUSE baddr V6
 */
public class READ_MOUSE extends ZInstructionBase
{
    public READ_MOUSE()
    {
        m_code = 0x16;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("READ_MOUSE instruction unimplemented");
    }
}
