/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_P0 raddr V5+
 */
public class CALL_P0 extends ZInstructionBase
{
    public static final int         CODE = 0x0f;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP1;

    public CALL_P0()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.version < 5 )
        {
            p.call(NOT.TYPE, NOT.CODE);
        }
        else
        {
            p.numvops = 1;
            p.vops[0] = p.op1;

            ZCPU.call(p, CALL_PV.TYPE, CALL_PV.CODE);
        }
    }
}
