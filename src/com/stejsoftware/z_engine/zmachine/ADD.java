/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         ADD a b <result>
 */
public class ADD extends ZInstructionBase
{
    public ADD()
    {
        m_code = 0x14;
        m_type = OPCODE_TYPE.OP2; 
    }

    @Override
    public void call(ZCPU p)
    {
        int sop1 = signedWord(p.op1);
        int sop2 = signedWord(p.op2);

        p.putVariable(p.curResult, unsignedWord(sop1 + sop2));
    }
}
