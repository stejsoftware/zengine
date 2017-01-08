/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SUB a b <result>
 */
public class SUB extends ZInstructionBase
{
    public SUB()
    {
        m_code = 0x15;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        int sop1, sop2;

        sop1 = signedWord(p.op1);
        sop2 = signedWord(p.op2);

        p.putVariable(p.curResult, unsignedWord(sop1 - sop2));
    }
}
