/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PULL var V1-5,7-8
 * 
 *         PULL [baddr] <result> V6
 */
public class PULL extends ZInstructionBase
{
    public PULL()
    {
        m_code = 0x09;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        // This will need to be extendedfor V6 support
        p.putVariable(p.vops[0], p.getVariable(0));
    }
}
