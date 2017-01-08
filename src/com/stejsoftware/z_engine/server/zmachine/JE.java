/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;


/**
 * @author jon
 * 
 *         JE a [b1 b2 b3] <branch>
 * 
 *         je
 * 
 *         2OP:1 1 je a b ?(label)
 * 
 *         Jump if a is equal to any of the subsequent operands. (Thus @je a
 *         never jumps and @je a b jumps if a = b.)
 */
public class JE extends ZInstructionBase
{
    public JE()
    {
        m_code = 0x01;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        // The variable version
    //    if( p.curInstruction == 0xc1 )
    //    {
    //        for( int i = 1; i < p.numvops; i++ )
    //        {
    //            if( p.vops[0] == p.vops[i] )
    //            {
    //                doBranch(p);
    //                return;
    //            }
    //        }

            // If we get here, there were no matches.
    //        dontBranch(p);
    //    }

        // The two-operand version
    //    else
    //    {
            if( p.op1 == p.op2 )
            {
                doBranch(p);
            }
            else
            {
                dontBranch(p);
            }
        }
   // }
}
