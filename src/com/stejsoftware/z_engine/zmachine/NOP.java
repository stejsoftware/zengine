/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         NOP
 */
public class NOP extends ZInstructionBase
{
    public NOP()
    {
        m_code = 0x04;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        // No op.
        return;
    }
}
