/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         JG s t <branch>
 */
public class JG extends ZInstructionBase
{
    public static final int         CODE = 0x03;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP2;

    public JG()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        if( signedWord(p.op1) > signedWord(p.op2) )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
