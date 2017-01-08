/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import java.util.Vector;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;


/**
 * @author jon
 * 
 *         // TOKENIZE baddr1 baddr2 [baddr3 bit] V5+
 * 
 *         // This code could definitely be improved.
 */
public class TOKENIZE extends ZInstructionBase
{
    public static final int         CODE = 0x1b;
    public static final OPCODE_TYPE TYPE = OPCODE_TYPE.VAR;

    public TOKENIZE()
    {
        m_code = 0x1b;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {
        int dictaddr, numseparators, dictentrysize, numdictentries, bit;
        int maxtokens, numtokens, maxtokenlen;
        String s, delimiters, thistoken;
        int c, len, curaddr, strpos;
        StringTokenizer tokens;
        Vector<Integer> encodedtoken;
        int i, j, k, curword, curzchar, curindex;
        boolean match = true;
        Integer n;

        if( p.numvops > 2 )
        {
            dictaddr = p.vops[2];
            bit = p.vops[3];
        }
        else
        {
            dictaddr = p.mainDictionary;
            bit = 0;
        }

        // Get the maximum number of tokens
        maxtokens = p.memory.fetchByte(p.vops[1]);

        // Set maximum token length, in words
        if( p.version < 4 )
            maxtokenlen = 2;
        else
            maxtokenlen = 3;

        // Construct a Java string from the input string.
        s = new String();
        if( p.version <= 4 )
        { // Null-terminated input string
            curaddr = p.vops[0] + 1;
            c = p.memory.fetchByte(curaddr);
            while( c != 0 )
            {
                s = s + String.valueOf((char) c);
                curaddr++;
                c = p.memory.fetchByte(curaddr);
            }
        }
        else
        { // String with length value
            len = p.memory.fetchByte(p.vops[0] + 1);
            curaddr = p.vops[0] + 2;
            for( i = 0; i < len; i++ )
            {
                s = s + String.valueOf((char) p.memory.fetchByte(curaddr));
                curaddr++;
            }
        }

        // Create a string containing separators.
        delimiters = new String(" "); // Space is always a delimiter.
        numseparators = p.memory.fetchByte(dictaddr);
        for( i = 1; i <= numseparators; i++ )
            delimiters = delimiters + String.valueOf((char) p.memory.fetchByte(dictaddr + i));

        // Get the number and length of dictionary entries.
        curaddr = dictaddr + 1 + numseparators;
        dictentrysize = p.memory.fetchByte(curaddr);
        curaddr++;
        numdictentries = signedWord(p.memory.fetchWord(curaddr));
        curaddr += 2;
        if( numdictentries < 0 ) numdictentries = Math.abs(numdictentries); // We
                                                                            // don't
                                                                            // care
                                                                            // whether
                                                                            // the
                                                                            // entries
                                                                            // are
                                                                            // sorted.

        // Parse through the input string. I think the Java StringTokenizer
        // class is neat.
        tokens = new StringTokenizer(s, delimiters, true);
        numtokens = 0;
        strpos = 0; // Since the StringTokenizer isn't discarding any
                    // characters, we use
                    // strpos to keep track of our position within the string,
                    // so we can
                    // store the location of each token within the input string.
        while( tokens.hasMoreTokens() && (numtokens < maxtokens) )
        {
            thistoken = tokens.nextToken();
            if( thistoken.equals(" ") )
            { // Ignore spaces
                strpos++;
                continue;
            }
            encodedtoken = encodeZString(p, thistoken);

            // Now, loop through the dictionary and check this token against
            // each one
            // in the dictionary. Remember that curaddr points to the first
            // dictionary
            // entry at this point.
            for( i = 0; i < numdictentries; i++ )
            {
                // Compare this dictionary entry against the encoded string.
                match = true;
                curindex = 0;
                for( j = 0; j < maxtokenlen; j++ )
                {
                    curword = p.memory.fetchWord(curaddr + (i * dictentrysize) + (j * 2));
                    for( k = 2; k >= 0; k-- )
                    {
                        curzchar = ((curword >> (k * 5)) & 0x1f);
                        if( curindex == encodedtoken.size() )
                        {
                            if( curzchar != 5 )
                            {
                                match = false;
                                break;
                            }
                            else
                                continue;
                        }
                        else
                        { // curindex valid
                            n = (Integer) encodedtoken.elementAt(curindex);
                            curindex++;
                            if( curzchar != n.intValue() )
                            {
                                match = false;
                                break;
                            }
                        }
                    }

                    // Break out of this token comparison if we know it's false.
                    if( !match ) break;
                }

                // Break out of the dictionary walk if this token matches.
                if( match ) break;
            }

            // This is a kludge. It is possible for match to be true if
            // the loop exits at the end of the dictionary.
            if( i >= numdictentries ) match = false;

            // Store the token. i still is the dictionary entry number of the
            // matched entry.
            if( match )
            {
                p.memory.putWord((p.vops[1] + 2 + (numtokens * 4)), (curaddr + (i * dictentrysize))); // Memory
                                                                                                      // location
                                                                                                      // of
                                                                                                      // dictionary
                                                                                                      // entry
                p.memory.putByte((p.vops[1] + 2 + (numtokens * 4) + 2), thistoken.length()); // Length
                                                                                             // of
                                                                                             // word
                p.memory.putByte((p.vops[1] + 2 + (numtokens * 4) + 3), (strpos + 2));// 1));
                                                                                      // //
                                                                                      // Position
                                                                                      // in
                                                                                      // input
                                                                                      // buffer;
                                                                                      // see
                                                                                      // above
            }
            else if( bit == 0 )
            { // If bit is set, leave the slot alone
                p.memory.putWord((p.vops[1] + 2 + (numtokens * 4)), 0);
                p.memory.putByte((p.vops[1] + 2 + (numtokens * 4) + 2), thistoken.length()); // Length
                                                                                             // of
                                                                                             // word
                p.memory.putByte((p.vops[1] + 2 + (numtokens * 4) + 3), (strpos + 2));// 1));
                                                                                      // //
                                                                                      // Position
                                                                                      // in
                                                                                      // input
                                                                                      // buffer;
                                                                                      // see
                                                                                      // above
            }

            strpos += thistoken.length();
            numtokens++;
        }

        // Finally, store the number of tokens tokenized in the parse buffer.
        p.memory.putByte((p.vops[1] + 1), numtokens);
    }
}
