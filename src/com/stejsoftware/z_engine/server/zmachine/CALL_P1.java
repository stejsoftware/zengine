/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_P1 raddr a1 V5+
 */
public class CALL_P1 extends ZInstructionBase
{
    public CALL_P1()
    {
        m_code = 0x1a;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.numvops = 2;
        p.vops[0] = p.op1;
        p.vops[1] = p.op2;

        ZCPU.call(p, CALL_PV.TYPE, CALL_PV.CODE);
    }
}
