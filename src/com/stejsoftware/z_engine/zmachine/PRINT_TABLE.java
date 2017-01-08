/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_TABLE baddr x [y n] V5+
 */
public class PRINT_TABLE extends ZInstructionBase
{
    public PRINT_TABLE()
    {
        m_code = 0x1e;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {

        int baddr, x, y, n;
        // int baseX, curY;
        int lineAddr;
        int c;

        // Get operands
        baddr = p.vops[0];
        x = p.vops[1];
        if( p.numvops == 4 )
        {
            y = p.vops[2];
            n = p.vops[3];
        }
        else
        {
            y = 1;
            n = 0;
        }

        // If y == 0, forget it (recommended by Klooster)
        if( y == 0 ) return;

        // Print the table
        // Point pt = zui.getCursorPosition();
        // baseX = pt.x;
        // curY = pt.y;
        lineAddr = baddr;

        for( int i = 0; i < y; i++ )
        {
            for( int j = 0; j < x; j++ )
            {
                c = p.memory.fetchByte(lineAddr + j);
                p.ioCard.printString(String.valueOf((char) c));
            }
            lineAddr += x + n;
        }

        // Done!
        return;
    }
}
