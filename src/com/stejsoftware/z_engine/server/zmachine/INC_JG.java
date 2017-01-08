/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         INC_JG var t <branch>()
 */
public class INC_JG extends ZInstructionBase
{
    public INC_JG()
    {
        m_code = 0x05;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        // INC var
        p.call(INC.TYPE, INC.CODE);

        // JG var t
        p.op1 = p.getVariable(p.op1);
        p.op1type = ZCPU.ARGTYPE_WORD;
        p.call(JG.TYPE, JG.CODE);
    }
}
