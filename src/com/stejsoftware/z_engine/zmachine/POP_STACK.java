/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         POP_STACK n [baddr] V6
 */
public class POP_STACK extends ZInstructionBase
{
    public POP_STACK()
    {
        m_code = 0x15;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("POP_STACK instruction unimplemented");
    }
}
