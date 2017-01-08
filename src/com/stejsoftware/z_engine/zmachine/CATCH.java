/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CATCH <result> V5+
 */
public class CATCH extends ZInstructionBase
{
    public CATCH()
    {
        m_code = 0x09;
        m_type = OPCODE_TYPE.OP0;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.version < 5 )
        {
            p.getVariable(0);
        }
        else
        {
            getResult(p);
            p.putVariable(p.curResult, p.curCallFrame.frameNumber);
        }
    }
}
