/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SHOW_STATUS V3
 */
public class SHOW_STATUS extends ZInstructionBase
{
    public static final int         CODE = 0x0c;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.OP0;

    public SHOW_STATUS()
    {
        m_code = CODE;
        m_type = TYPE;
    }

    @Override
    public void call(ZCPU p)
    {
        boolean timegame;
        String s;
        int a, b, name;

        // This instruction is known to appear spuriously in some
        // V5 games (notably Wishbringer Solid Gold), so if this
        // storyfile is not V1-3, we'll ignore it.
        if( p.version > 3 )
        {
            return;
        }

        // Find out if this is a time game or not. Can this change
        // during a game? I'm assuming it can.
        if( (p.memory.fetchByte(0x01) & 0x02) == 0x02 )
        {
            timegame = true;
        }
        else
        {
            timegame = false;
        }

        // Get the current location name
        name = p.objTable.getObjectName(p.getVariable(16));
        s = decodeZString(p, name);

        // Get the two integers
        a = signedWord(p.getVariable(17));
        b = signedWord(p.getVariable(18));

        // Pass it on to the user interface.
        p.zui.showStatusBar(s, a, b, timegame);
    }
}
