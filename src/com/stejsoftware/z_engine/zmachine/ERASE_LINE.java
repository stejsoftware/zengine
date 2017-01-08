/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         ERASE_LINE V4-5,7-8
 * 
 *         ERASE_LINE n V6
 */
public class ERASE_LINE extends ZInstructionBase
{
    public ERASE_LINE()
    {
        m_code = 0x0e;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        if( ((p.version >= 4) && (p.version <= 5)) || ((p.version >= 7) && (p.version <= 8)) )
        {
            p.zui.eraseLine(1);
        }
        else if( p.version == 6 )
        {
            p.zui.eraseLine(p.vops[0]);
        }
    }
}
