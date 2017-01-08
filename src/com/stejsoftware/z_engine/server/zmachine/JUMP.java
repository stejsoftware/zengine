/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         JUMP s V1+
 */
public class JUMP extends ZInstructionBase
{
    public JUMP()
    {
        m_code = 0x0c;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        p.curCallFrame.pc = p.curCallFrame.pc + signedWord(p.op1) - 2;
    }
}
