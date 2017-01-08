/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_PADDR saddr V1+
 */
public class PRINT_PADDR extends ZInstructionBase
{
    public PRINT_PADDR()
    {
        m_code = 0x0d;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.printString(decodeZString(p, p.unpackAddr(p.op1, false)));
    }
}
