/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PIRACY <branch> V5+
 */
public class PIRACY extends ZInstructionBase
{
    public PIRACY()
    {
        m_code = 0x0f;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        // This always branches.
        getBranch(p);
        doBranch(p);
    }
}
