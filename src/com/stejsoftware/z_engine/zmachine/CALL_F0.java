/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_F0 raddr <result> V4+
 */
public class CALL_F0 extends ZInstructionBase
{
    public CALL_F0()
    {
        m_code = 0x08;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        p.numvops = 1;
        p.vops[0] = p.op1;

        ZCPU.call(p, CALL_FV.TYPE, CALL_FV.CODE);
    }
}
