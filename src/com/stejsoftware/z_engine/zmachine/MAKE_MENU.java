/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         MAKE_MENU n baddr <branch> V6
 */
public class MAKE_MENU extends ZInstructionBase
{
    public MAKE_MENU()
    {
        m_code = 0x1b;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getBranch(p);
        p.zui.fatal("MAKE_MENU instruction unimplemented");
    }
}
