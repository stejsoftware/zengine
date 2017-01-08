/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_FORM baddr V6
 */
public class PRINT_FORM extends ZInstructionBase
{
    public PRINT_FORM()
    {
        m_code = 0x1a;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("PRINT_FORM instruction unimplemented");
    }
}
