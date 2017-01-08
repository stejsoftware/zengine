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
public class NOTV extends ZInstructionBase
{
    public NOTV()
    {
        m_code = 0x18;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        int val;

        if( p.voptypes[0] == ZCPU.ARGTYPE_WORD )
        {
            val = ((~p.vops[0]) & 0xffff);
        }
        else
        {
            val = ((~p.vops[0]) & 0xff);
        }

        p.putVariable(p.curResult, val);
    }
}
