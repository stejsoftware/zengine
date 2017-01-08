/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         CALL_PD raddr [a1 a2 a3 a4 a5 a6 a7] V5+
 * 
 *         CALL_PV actually handles this
 */
public class CALL_PD extends CALL_PV
{
    public CALL_PD()
    {
        m_code = 0x1a;
        m_type = OPCODE_TYPE.VAR;
    }
}
