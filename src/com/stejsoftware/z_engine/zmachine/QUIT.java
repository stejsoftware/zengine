/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         QUIT
 */
public class QUIT extends ZInstructionBase
{
    public QUIT()
    {
        m_code = 0x0a;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.quit();
    }
}
