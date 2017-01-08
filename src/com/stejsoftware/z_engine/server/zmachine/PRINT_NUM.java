/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_NUM s
 */
public class PRINT_NUM extends ZInstructionBase
{
    public PRINT_NUM()
    {
        m_code = 0x06;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        int sop1 = signedWord(p.vops[0]);
        p.ioCard.printString(Integer.toString(sop1));
    }
}
