/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT <string>
 */
public class PRINT extends ZInstructionBase
{
    public static final int         CODE = 0x02;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP0;

    public PRINT()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        getString(p);

        // The spec says to output the string as if a sequence
        // of PRINT_CHAR instructions were executed. However,
        // we output an entire string at a time for maximum
        // drawing efficiency.
        p.ioCard.printString(p.curString);
    }
}
