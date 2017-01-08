/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SAVE_UNDO <result> V5+
 */
public class SAVE_UNDO extends ZInstructionBase
{
    public SAVE_UNDO()
    {
        m_code = 0x09;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        
        ZOutputStream dos;

        try
        {
            dos = new ZOutputStream();
            p.dumpState( dos);
            p.memory.dumpMemory(dos, 0, p.dynamicMemorySize);
            p.undoState = dos.toByteArray();
        }
        catch( Exception ioex )
        {
            p.zui.fatal("I/O exception during SAVE_UNDO??");
        }

        // We did it!
        if( p.version <= 3 )
        {
            doBranch(p);
        }
        else
        {
            p.putVariable(p.curResult, 1);
        }
    }
}
