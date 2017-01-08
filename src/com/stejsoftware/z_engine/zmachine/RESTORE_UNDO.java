/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RESTORE_UNDO <result> V5+
 */
public class RESTORE_UNDO extends ZInstructionBase
{
    public RESTORE_UNDO()
    {
        m_code = 0x0a;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        ZInputStream dis;
        int tsBit;

        // Remember the transcript bit
        tsBit = p.memory.fetchWord(0x10) & 0x0001;

        try
        {
            dis = new ZInputStream(p.undoState);
            p.readState(dis);
            p.memory.readMemory(dis, 0, p.dynamicMemorySize);
        }
        catch( Exception ex1 )
        {
            p.zui.fatal("I/O Exception during RESTORE_UNDO???");
        }

        // We did it!
        p.memory.putWord(0x10, p.memory.fetchWord(0x10) | tsBit);
        if( p.version >= 3 )
        {
            p.curResult = p.memory.fetchByte(p.curCallFrame.pc - 1);
            p.putVariable(p.curResult, 2); // Is this correct?
        }
    }
}
