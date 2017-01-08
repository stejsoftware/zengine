/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_ADDR addr V1+
 */
public class PRINT_ADDR extends ZInstructionBase
{
    public PRINT_ADDR()
    {
        m_code = 0x07;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        String s;

        s = decodeZString(p, p.op1);
        p.ioCard.printString(s);
    }
}
