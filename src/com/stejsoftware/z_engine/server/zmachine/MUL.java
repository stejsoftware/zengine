/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         MUL a b <result>
 */
public class MUL extends ZInstructionBase
{
    public MUL()
    {
        m_code = 0x16;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        int sop1, sop2;

        sop1 = signedWord(p.op1);
        sop2 = signedWord(p.op2);

        p.putVariable(p.curResult, unsignedWord(sop1 * sop2));
    }
}
