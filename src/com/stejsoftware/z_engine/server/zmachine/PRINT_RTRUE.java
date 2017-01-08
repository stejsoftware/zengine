/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_RTRUE <string>
 */
public class PRINT_RTRUE extends ZInstructionBase
{
    public PRINT_RTRUE()
    {
        m_code = 3;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        getString(p);

        p.call(PRINT.TYPE, PRINT.CODE);
        p.call(NEW_LINE.TYPE, NEW_LINE.CODE);
        p.call(RTRUE.TYPE, RTRUE.CODE);
    }
}
