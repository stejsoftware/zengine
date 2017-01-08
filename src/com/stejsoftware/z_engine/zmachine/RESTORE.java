/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // RESTORE <branch> V1-3
 * 
 *         // RESTORE <result> V4
 */
public class RESTORE extends ZInstructionBase
{
    public static final int         CODE = 0x06;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP0;

    public RESTORE()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.version < 4 )
        {
            getBranch(p);
        }
        else if( p.version == 4 )
        {
            getResult(p);
        }
        else
        {
            p.zui.fatal("RESTORE 0OP unsupported after version 4.");
        }

        String fn;
        ZInputStream dis;
        int tsBit;

        // Get a filename to restore from
        fn = p.zui.getFilename("Restore Game", null, false);
        if( fn == null )
        { // An error-probably user cancelled.
            if( p.version >= 4 ) p.putVariable(p.curResult, 0);
            return;
        }

        // Remember the transcript bit
        tsBit = p.memory.fetchWord(0x10) & 0x0001;

        try
        {
            dis = new ZInputStream();
            p.readState(dis);
            p.memory.readMemory(dis, 0, p.dynamicMemorySize);
        }
        catch( Exception ex1 )
        {
            if( p.version >= 4 ) p.putVariable(p.curResult, 0);
            return;
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
