/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PUSH a
 */
public class PUSH extends ZInstructionBase
{
    public PUSH()
    {
        m_code = 0x08;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(0, p.vops[0]);
    }
}
