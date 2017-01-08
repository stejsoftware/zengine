/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         // SAVE <branch> V1-3
 * 
 *         // SAVE <result> V4+
 */
public class SAVE extends ZInstructionBase
{
    public static final int         CODE = 0x05;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP0;

    public SAVE()
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
            p.zui.fatal("SAVE 0OP unsupported after version 4.");
        }

        String fn;
        ZOutputStream dos;

        // Get a filename to save under
        fn = p.zui.getFilename("Save Game", null, true);
        if( fn == null )
        {
            // An error-probably user cancelled.
            if( p.version <= 3 )
            {
                dontBranch(p);
            }
            else
            {
                p.putVariable(p.curResult, 0);
            }

            return;
        }

        try
        {
            dos = new ZOutputStream();
            p.dumpState(dos);
            p.memory.dumpMemory(dos, 0, p.dynamicMemorySize);
        }
        catch( Exception ex1 )
        {
            if( p.version <= 3 )
            {
                dontBranch(p);
            }
            else
            {
                p.putVariable(p.curResult, 0);
            }

            return;
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
