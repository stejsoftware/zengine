/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;


/**
 * @author jon
 * 
 *         // INPUT_STREAM n V3+
 */
public class INPUT_STREAM extends ZInstructionBase
{
    public INPUT_STREAM()
    {
        m_code = 0x14;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.setInputStream(p.vops[0]);
    }
}
