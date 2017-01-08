/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // SOUND n [op time raddr] V3+
 */
public class SOUND extends ZInstructionBase
{
    public SOUND()
    {
        m_code = 0x15;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        // Silently fail on this instruction if no raddr argument;
        // otherwise, go straight to raddr for now.
        if( p.numvops == 1 ) return;

        if( p.vops[1] != 2 ) return;

        // Pretend a CALL_P0 has just been executed.
        p.op1 = p.vops[3];
        p.op1type = p.voptypes[3];

        p.numvops = 1;
        p.vops[0] = p.op1;

        p.call(CALL_PV.TYPE, CALL_PV.CODE);
    }
}
