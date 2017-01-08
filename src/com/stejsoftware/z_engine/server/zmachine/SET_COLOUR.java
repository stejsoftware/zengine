/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SET_COLOUR f b V5+
 */
public class SET_COLOUR extends ZInstructionBase
{
    public SET_COLOUR()
    {
        m_code = 0x1b;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.op1 == 1 )
        {
            p.op1 = p.memory.fetchByte(0x2d);
        }

        if( p.op2 == 1 )
        {
            p.op2 = p.memory.fetchByte(0x2c);
        }

        p.zui.setColor(p.op1, p.op2);
    }
}
