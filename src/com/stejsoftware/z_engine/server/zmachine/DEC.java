/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         DEC var V1+
 */
public class DEC extends ZInstructionBase
{
    public static final int         CODE = 0x06;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP1;

    public DEC()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        int w;

        w = signedWord(p.getVariable(p.op1));
        w = ((w - 1) % 0x10000);
        p.putVariable(p.op1, w);
    }
}
