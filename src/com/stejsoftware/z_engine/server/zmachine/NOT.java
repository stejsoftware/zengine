/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         NOT a <result> V1+
 */
public class NOT extends ZInstructionBase
{
    public static final int         CODE = 0x0f;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP1;

    public NOT()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.version < 5 )
        {
            getResult(p);

            int val;

            if( p.op1type == ZCPU.ARGTYPE_WORD )
            {
                val = ((~p.op1) & 0xffff);
            }
            else
            {
                val = ((~p.op1) & 0xff);
            }

            p.putVariable(p.curResult, val);
        }
        else
        {
            p.numvops = 1;
            p.vops[0] = p.op1;

            p.call(CALL_PV.TYPE, CALL_PV.CODE);
        }
    }
}
