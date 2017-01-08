/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PUSH_STACK a baddr <branch> V6
 */
public class PUSH_STACK extends ZInstructionBase
{
    public PUSH_STACK()
    {
        m_code = 0x18;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getBranch(p);
        p.zui.fatal("PUSH_STACK instruction unimplemented");
    }
}
