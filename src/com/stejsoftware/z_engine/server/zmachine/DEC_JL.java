/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         DEC_JL var s <branch>
 */
public class DEC_JL extends ZInstructionBase
{
    public DEC_JL()
    {
        m_code = 0x04;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        // DEC var
        ZCPU.call(p, DEC.TYPE, DEC.CODE);

        // JL var s
        p.op1 = p.getVariable(p.op1);
        p.op1type = ZCPU.ARGTYPE_WORD;
        p.call(JL.TYPE, JL.CODE);
    }
}
