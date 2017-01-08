/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RESTART
 */
public class RESTART extends ZInstructionBase
{
    public RESTART()
    {
        m_code = 0x07;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        // This will cause the decoder to exit and the ZMachine to restart
        p.zui.restart();
        p.restartFlag = true;
        return;
    }
}
