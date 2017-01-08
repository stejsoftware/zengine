/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

/**
 * @author jon
 * 
 */

public class ZOpCode
{
    // Private constants

    // Opcode types
    public enum OPCODE_TYPE
    {
        None,
        OP0,
        OP1,
        OP2,
        VAR,
        EXT
    }

    public OPCODE_TYPE m_type;
    public int  m_code;

    public ZOpCode()
    {
        m_type = OPCODE_TYPE.None;
        m_code = -1;
    }

    public ZOpCode(OPCODE_TYPE type, int code)
    {
        m_type = type;
        m_code = code;
    }

    public ZOpCode(int code, OPCODE_TYPE type)
    {
        m_type = type;
        m_code = code;
    }
}
