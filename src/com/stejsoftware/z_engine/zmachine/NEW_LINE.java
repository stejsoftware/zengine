/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         NEW_LINE
 */
public class NEW_LINE extends ZInstructionBase
{
    public static final int        CODE = 0x0b;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP0;

    public NEW_LINE()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        p.did_newline = true;
        p.ioCard.printString("\n");
    }
}
