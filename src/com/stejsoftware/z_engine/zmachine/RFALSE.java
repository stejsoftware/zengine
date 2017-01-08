/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RFALSE
 */
public class RFALSE extends ZInstructionBase
{
    public RFALSE()
    {
        m_code = 0x01;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        // This is equivalent to RET 0
        p.op1 = 0;
        p.call(RET.TYPE, RET.CODE);
    }
}
