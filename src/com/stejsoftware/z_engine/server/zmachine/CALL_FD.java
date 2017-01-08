/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_FD raddr [a1 a2 a3 a4 a5 a6 a7] <result> V4+
 * 
 *         CALL_FV actually handles this
 */
public class CALL_FD extends CALL_FV
{
    public CALL_FD()
    {
        m_code = 0x0c;
        m_type = OPCODE_TYPE.VAR;
    }
}
