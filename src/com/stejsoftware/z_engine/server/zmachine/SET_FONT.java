/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // SET_FONT n <result> V5,7-8
 * 
 *         // SET_FONT n [window] <result> V6
 */
public class SET_FONT extends ZInstructionBase
{
    public SET_FONT()
    {
        m_code = 0x04;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        p.zui.setFont(p.vops[0]);

        Dimension s = p.zui.getFontSize();

        p.memory.putByte(0x26, s.height);
        p.memory.putByte(0x27, s.width);
    }
}
