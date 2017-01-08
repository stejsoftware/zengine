/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         RESTORE [baddr1 n baddr2] <result> V5+
 */
public class EXT_RESTORE extends ZInstructionBase
{
    public EXT_RESTORE()
    {
        m_code = 0x01;
        m_type = OPCODE_TYPE.EXT;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        String fn;
        String suggested;
        ZInputStream dis;
        int slen;

        // If there are no arguments, do a normal restore
        if( p.numvops == 0 )
        {
            // zop_restore(p);
            ZCPU.call(p, RESTORE.TYPE, RESTORE.CODE);
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
        fn = p.zui.getFilename("Load Auxiliary File", suggested, false);
        if( fn == null )
        { // An error-probably user cancelled.
            p.putVariable(p.curResult, 0);
            return;
        }

        try
        {
            dis = new ZInputStream();
            p.memory.readMemory(dis, p.vops[0], p.vops[1]);
        }
        catch( Exception ex1 )
        {
            p.putVariable(p.curResult, 0);
            return;
        }

        // We did it!
        if( p.version >= 3 )
        {
            p.putVariable(p.curResult, p.vops[1]);
        }
    }
}
