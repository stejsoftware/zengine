/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         READ baddr1 baddr2 V1-3
 * 
 *         READ baddr1 baddr2 [time raddr] V4
 * 
 *         READ baddr1 baddr2 [time raddr] <result> V5+
 */
public class READ extends ZInstructionBase
{
    public READ()
    {
        m_code = 0x04;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        if( p.version >= 5 )
        {
            getResult(p);
        }

        // DebugConsole.log("zop_read: " + run_state);

        String s;
        StringBuffer sb;
        int termChar;
        int len;
        int curaddr;
        int baddr1, baddr2;
        int time = 0, raddr = 0;

        baddr1 = p.vops[0];
        baddr2 = p.vops[1];
        if( p.numvops > 2 )
        {
            time = p.vops[2];
            raddr = p.vops[3];
        }

        // Flush the I/O card's output buffer
        p.ioCard.outputFlush();

        // This implies a SHOW_STATUS in V1-3.
        if( p.version < 4 )
        {
            p.call(SHOW_STATUS.TYPE, SHOW_STATUS.CODE);
        }

        // Read a line of text
        sb = new StringBuffer();
        if( (time > 0) && (raddr > 0) )
        { // A timed READ
            while( true )
            { // Ick.
                termChar = p.ioCard.readLine(sb, time);
                if( termChar == -1 )
                { // A timeout
                  // ioCard.outputFlush(p);
                  // did_newline = false;
                    for( int i = 0; i < sb.length(); i++ )
                        p.ioCard.printString("\b");
                    int rc = p.interrupt(raddr);
                    if( rc == 0 )
                    {
                        // if (did_newline) {
                        // ioCard.printString("\n" + sb.toString(p));
                        // ioCard.outputFlush(p);
                        // }
                        p.ioCard.printString(sb.toString());
                        p.ioCard.outputFlush();
                        continue;
                    }
                    else
                    {
                        p.ioCard.outputFlush();
                        sb = new StringBuffer();
                        termChar = 0;
                        break;
                    }
                }
                else
                    // Not a timeout
                    break;
            }
        }
        else
        {
            termChar = p.ioCard.readLine(sb, 0);

            if( termChar == -1 )
            {
               // p.pause(); //TODO: fix!
                return;
            }
        }
 
        s = sb.toString();

        // If V1-4, just store the line. If V5+, possibly
        // store it after other characters in the buffer.
        if( p.version <= 4 )
        {
            curaddr = baddr1 + 1;
            len = s.length();
            for( int i = 0; i < len; i++ )
            {
                p.memory.putByte(curaddr, Character.toLowerCase(s.charAt(i)));
                curaddr++;
            }
            p.memory.putByte(curaddr, 0);
        }
        else
        {
            int nchars = p.memory.fetchByte(baddr1 + 1);
            curaddr = baddr1 + 2 + nchars;
            len = s.length();
            for( int i = 0; i < len; i++ )
            {
                p.memory.putByte(curaddr, Character.toLowerCase(s.charAt(i)));
                curaddr++;
            }
            p.memory.putByte(baddr1 + 1, (nchars + len));
        }

        // Tokenize input
        if( baddr2 != 0 )
        {
            p.numvops = 2;
            p.vops[0] = baddr1;
            p.vops[1] = baddr2;

            p.call(TOKENIZE.TYPE, TOKENIZE.CODE);
        }

        // If V5+, store result
        if( p.version >= 5 )
        {
            p.putVariable(p.curResult, termChar);
        }
    }
}
