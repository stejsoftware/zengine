/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         TEST_ATTR obj attr <branch>
 */
public class TEST_ATTR extends ZInstructionBase
{
    public TEST_ATTR()
    {
        m_code = 0x0a;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.objTable.hasAttribute(p.op1, p.op2) )
        {
            doBranch(p);
        }
        else
        {
            dontBranch(p);
        }
    }
}
