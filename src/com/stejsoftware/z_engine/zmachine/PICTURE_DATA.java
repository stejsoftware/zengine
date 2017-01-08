/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // PICTURE_DATA pic baddr <branch> V6
 */
public class PICTURE_DATA extends ZInstructionBase
{
    public PICTURE_DATA()
    {
        m_code = 0x06;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        p.zui.fatal("PICTURE_DATA instruction unimplemented");
    }
}
