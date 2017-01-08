/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PICTURE_TABLE baddr V6
 */
public class PICTURE_TABLE extends ZInstructionBase
{
    public PICTURE_TABLE()
    {
        m_code = 0x1c;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        p.zui.fatal("PICTURE_TABLE instruction unimplemented");
    }
}
