/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_CHAR n
 */
public class PRINT_CHAR extends ZInstructionBase
{
    public PRINT_CHAR()
    {
        m_code = 0x05;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        String s = new String(String.valueOf((char) p.vops[0]));
        p.ioCard.printString(s);
    }
}
