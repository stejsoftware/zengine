/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // VERIFY <branch>
 */
public class VERIFY extends ZInstructionBase
{
    public VERIFY()
    {
        m_code = 0x0d;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        // VERIFY is always successful for now. I've had problems getting it
        // working,
        // and it's not a high priority.
        getBranch(p);
        doBranch(p);
    }
}
