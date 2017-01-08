/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         MOD a b <result>
 */
public class MOD extends ZInstructionBase
{
    public MOD()
    {
        m_code = 0x18;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        int rv = 0;
        int sop1 = signedWord(p.op1);
        int sop2 = signedWord(p.op2);

        if( sop2 == 0 )
        {
            rv = sop1;
        }
        else
        {
            rv = unsignedWord(sop1 % sop2);
        }

        p.putVariable(p.curResult, rv);
    }
}
