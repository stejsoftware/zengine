/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RTRUE V1+
 */
public class RTRUE extends ZInstructionBase
{
    public static final int         CODE = 0x00;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP0;

    public RTRUE()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        // This is equivalent to RET 1
        p.op1 = 1;
        p.call(RET.TYPE, RET.CODE);
    }
}
