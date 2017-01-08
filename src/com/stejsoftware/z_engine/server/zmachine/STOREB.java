/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         STOREB baddr n byte
 */
public class STOREB extends ZInstructionBase
{
    public STOREB()
    {
        m_code = 0x02;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.memory.putByte((p.vops[0] + p.vops[1]), p.vops[2]);
    }
}
