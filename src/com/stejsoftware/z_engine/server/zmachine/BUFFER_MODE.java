/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         BUFFER_MODE bit V4+
 */
public class BUFFER_MODE extends ZInstructionBase
{
    public BUFFER_MODE()
    {
        m_code = 0x12;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        // This doesn't really fit in with our buffering method, so we ignore
        // it.
        // Flush the buffer, though.
        p.ioCard.outputFlush();
    }
}
