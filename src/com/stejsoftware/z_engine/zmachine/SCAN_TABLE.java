/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         SCAN_TABLE a baddr n [byte] <result> <branch> V4+
 */
public class SCAN_TABLE extends ZInstructionBase
{
    public SCAN_TABLE()
    {
        m_code = 0x17;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);
        getBranch(p);

        int a, baddr, n, format;
        boolean searchWord; // Searching for a word (not a byte)?
        int tableWidth; // Width of table
        int testAddr; // Address to test
        int testData; // Data to test

        // Get operands
        a = p.vops[0];
        baddr = p.vops[1];
        n = p.vops[2];

        if( p.numvops == 4 )
        {
            format = p.vops[3];
        }
        else
        {
            format = 0x82;
        }

        if( (format & 0x80) == 0x80 )
        {
            searchWord = true;
        }
        else
        {
            searchWord = false;
        }

        tableWidth = (format & 0x7f);

        // Fail if it's a table of bytes and a is word-valued
        if( (tableWidth == 2) && (p.voptypes[0] == ZCPU.ARGTYPE_BYTE) )
        {
            p.putVariable(p.curResult, 0);
            dontBranch(p);

            return;
        }

        // Search the table
        for( int i = 0; i < n; i++ )
        {
            testAddr = (baddr + (i * tableWidth));

            if( searchWord )
            {
                testData = p.memory.fetchWord(testAddr);
            }
            else
            {
                testData = p.memory.fetchByte(testAddr);
            }

            if( testData == a )
            {
                // A match!
                p.putVariable(p.curResult, (baddr + (i * tableWidth)));
                doBranch(p);

                return;
            }
        }

        // If we get here, there was no match, or n < 1
        p.putVariable(p.curResult, 0);
        dontBranch(p);

        return;
    }
}
