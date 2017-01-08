/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         DIV a b <result>
 */
public class DIV extends ZInstructionBase
{
    public DIV()
    {
        m_code = 0x17;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        int sop1 = signedWord(p.op1);
        int sop2 = signedWord(p.op2);

        if( sop2 == 0 )
        {
            p.zui.fatal("Divide by zero");
        }
        else
        {
            p.putVariable(p.curResult, unsignedWord(sop1 / sop2));
        }
    }
}
