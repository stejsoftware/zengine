/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         STOREW baddr n a
 */
public class STOREW extends ZInstructionBase
{
    public STOREW()
    {
        m_code = 0x01;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.memory.putWord((p.vops[0] + (2 * p.vops[1])), p.vops[2]);
    }
}
