/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_F1 raddr a1 <result> V4+
 */
public class CALL_F1 extends ZInstructionBase
{
    public CALL_F1()
    {
        m_code = 0x19;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.numvops = 2;
        p.vops[0] = p.op1;
        p.vops[1] = p.op2;

        // zop_call_fv(p)
        ZCPU.call(p, CALL_FV.TYPE, CALL_FV.CODE);
    }
}
