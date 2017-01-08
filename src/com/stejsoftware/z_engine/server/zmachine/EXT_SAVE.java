/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SAVE [baddr1 n baddr2] <result> V5+
 */
public class EXT_SAVE extends ZInstructionBase
{
    public EXT_SAVE()
    {
        m_code = 0x00;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        String fn;
        String suggested;
        ZOutputStream dos;
        int slen;

        // If there are no arguments, do a normal save
        if( p.numvops == 0 )
        {
            // zop_save(p);
            p.call(SAVE.TYPE, SAVE.CODE);
            return;
        }

        // Get a filename to save under
        suggested = null;
        if( (p.numvops > 2) && (p.vops[2] != 0) )
        {
            slen = p.memory.fetchByte(p.vops[2]);
            if( slen > 0 )
            {
                StringBuffer tmp = new StringBuffer();
                for( int i = 1; i <= slen; i++ )
                    tmp.append(String.valueOf((char) p.memory.fetchByte(p.vops[2] + i)));
                suggested = tmp.toString();
            }
        }
        fn = p.zui.getFilename("Save Auxiliary File", suggested, true);
        if( fn == null )
        {
            // An error-probably user cancelled.
            p.putVariable(p.curResult, 0);
            return;
        }

        try
        {
            dos = new ZOutputStream();
            p.memory.dumpMemory(dos, p.vops[0], p.vops[1]);
        }
        catch( Exception ex1 )
        {
            p.putVariable(p.curResult, 0);
            return;
        }

        // We did it!
        p.putVariable(p.curResult, 1);
    }
}
