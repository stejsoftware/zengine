/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PUT_PROP obj prop a
 */
public class PUT_PROP extends ZInstructionBase
{
    public PUT_PROP()
    {
        m_code = 0x00;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        p.objTable.putProperty(p.vops[0], p.vops[1], p.vops[2]);
    }

}
