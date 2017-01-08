/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         LOADB baddr n <result>
 */
public class LOADB extends ZInstructionBase
{
    public LOADB()
    {
        m_code = 0x10;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.putVariable(p.curResult, p.memory.fetchByte(p.op1 + p.op2));
    }
}
