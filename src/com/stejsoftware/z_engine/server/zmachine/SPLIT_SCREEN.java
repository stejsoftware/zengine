/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SPLIT_SCREEN n V3+
 */
public class SPLIT_SCREEN extends ZInstructionBase
{
    static int        CODE = 0x0a;
    static OPCODE_TYPE TYPE = OPCODE_TYPE.VAR;

    public SPLIT_SCREEN()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.outputFlush();
        p.zui.splitScreen(p.vops[0]);
    }
}
