/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import java.util.Vector;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 */
public abstract class ZInstructionBase implements ZInstruction
{
    protected int         m_code = -1;
    protected OPCODE_TYPE m_type = OPCODE_TYPE.None;

    @Override
    public ZOpCode opCode()
    {
        return new ZOpCode(m_type, m_code);
    }

    // This method gets the <branch> argument of the current
    // instruction. It stores the value of the argument in the
    // global variable curBranch, and sets curBranchReversed to
    // true if the logic of the branch is reversed. curCallFrame.pc
    // should be pointing at the argument when this is called;
    // it is adjusted accordingly.
    protected void getBranch(ZCPU p)
    {
        int b1, b2;
        int sval;

        // Get the first byte of the branch
        b1 = p.memory.fetchByte(p.curCallFrame.pc);
        p.curCallFrame.pc++;

        // Check to see if logic is reversed
        if( (b1 & 0x80) == 0x80 )
            p.curBranchReversed = false;
        else
            p.curBranchReversed = true;

        // If the branch is only one byte long, just set its
        // value and return.
        if( (b1 & 0x40) == 0x40 )
        {
            p.curBranch = (b1 & 0x3f);
            return;
        }

        // Otherwise, construct a signed branch value.
        b2 = p.memory.fetchByte(p.curCallFrame.pc);
        p.curCallFrame.pc++;
        sval = (((((b1 & 0x3f) << 8) & 0x3f00) | b2) & 0x3fff);
        // If the following makes no sense, see the Z-Machine spec
        // on signed numbers.
        if( (sval & 0x2000) == 0x2000 )
            p.curBranch = (sval - 16384);
        else
            p.curBranch = sval;
    }

    /**
     * Do a branch, based on the values of curBranch and curBranchReversed.
     * 
     * @param p
     */
    protected void doBranch(ZCPU p)
    {
        if( p.curBranchReversed )
            return;
        else
        {
            switch( p.curBranch )
            {
            case 0:
                // zop_rfalse(p);
                break;
            case 1:
                // zop_rtrue(p);
                break;
            default:
                p.curCallFrame.pc = p.curCallFrame.pc + p.curBranch - 2;
                break;
            }
            return;
        }
    }

    /**
     * Don't do a branch, based on the values of curBranch and
     * curBranchReversed. (If curBranchReversed is true, this implies a branch).
     */
    protected void dontBranch(ZCPU p)
    {
        if( p.curBranchReversed )
        {
            switch( p.curBranch )
            {
            case 0:
                // zop_rfalse(p);
                break;
            case 1:
                // zop_rtrue(p);
                break;
            default:
                p.curCallFrame.pc = p.curCallFrame.pc + p.curBranch - 2;
                break;
            }
            return;
        }
        else
            return;
    }

    // This method gets the <result> argument of the current
    // instruction and stores it in the global variable curResult.
    // curCallFrame.pc should be pointing at the argument when
    // this is called; it is adjusted accordingly.
    protected void getResult(ZCPU p)
    {
        p.curResult = p.memory.fetchByte(p.curCallFrame.pc);
        p.curCallFrame.pc++;
    }

    // This method gets the <string> argument of the current
    // instruction, decodes it, and stores it in the global variable
    // curString. curCallFrame.pc should be pointing at the start
    // of the string; it is adjusted accordingly.
    protected void getString(ZCPU p)
    {
        int w;

        // First, decode the string
        p.curString = decodeZString(p, p.curCallFrame.pc);

        // Now, adjust the PC.
        w = p.memory.fetchWord(p.curCallFrame.pc);
        p.curCallFrame.pc += 2;

        while( (w & 0x8000) == 0 )
        {
            w = p.memory.fetchWord(p.curCallFrame.pc);
            p.curCallFrame.pc += 2;
        }
    }

    // This function decodes the Z-String at the specified
    // address, and returns it as a Java String object.
    protected String decodeZString(ZCPU p, int addr)
    {
        ZAlphabet alphabet = ZAlphabet.Get(p.version);
        StringBuffer decodedstr = new StringBuffer();
        int w, tmpaddr;
        ALPHABET currentAlphabet;
        ALPHABET lockAlphabet;
        int abbrevAddr;
        char c, c2, c3;
        int zlen, curindex;
        int[] zchars;

        // First, throw all of the Z-characters, unprocessed, into
        // an array for easy access.
        // First, count the zcharacters.
        tmpaddr = addr;
        zlen = 0;
        do
        {
            w = p.memory.fetchWord(tmpaddr);
            tmpaddr += 2;
            zlen += 3;
        }
        while( (w & 0x8000) != 0x8000 );
        // Then, allocate an array and put them in.
        zchars = new int[zlen];
        curindex = 0;
        tmpaddr = addr;
        w = p.memory.fetchWord(tmpaddr);
        tmpaddr += 2;
        zchars[curindex] = ((w >> 10) & 0x1f);
        zchars[curindex + 1] = ((w >> 5) & 0x1f);
        zchars[curindex + 2] = (w & 0x1f);
        curindex += 3;
        while( (w & 0x8000) == 0 )
        {
            w = p.memory.fetchWord(tmpaddr);
            tmpaddr += 2;
            zchars[curindex] = ((w >> 10) & 0x1f);
            zchars[curindex + 1] = ((w >> 5) & 0x1f);
            zchars[curindex + 2] = (w & 0x1f);
            curindex += 3;
        }

        // Now, decode the sequence of Z-characters.
        c = 0;
        c2 = 0;
        c3 = 0;
        currentAlphabet = ALPHABET.L;
        lockAlphabet = ALPHABET.L;
        for( int i = 0; i < zlen; i++ )
        {
            c = (char) zchars[i];
            // Decode character -- handle special characters as
            // necessary. A bit of code is repeated here for
            // the sake of cutting down on the number of comparisons.
            switch( c )
            {
            case 1:
                if( p.version == 1 )
                { // Newline in V1
                    decodedstr.append("\n");
                    currentAlphabet = lockAlphabet;
                }
                else
                { // Abbreviation in V2+
                    i++;
                    if( i >= zlen ) // This is all we're getting.
                        break;
                    c2 = (char) zchars[i];
                    abbrevAddr = p.memory.fetchWord(p.abbrevTable + (((((int) c) - 1) * 32 + ((int) c2)) * 2));
                    abbrevAddr *= 2; // Word address
                    decodedstr.append(decodeZString(p, abbrevAddr));
                }
                break;
            case 2:
                if( p.version <= 2 )
                { // Shift up
                    currentAlphabet = alphabet.shiftUp(currentAlphabet);
                }
                else
                { // An abbreviation
                    i++;
                    if( i >= zlen ) break;
                    c2 = (char) zchars[i];
                    abbrevAddr = p.memory.fetchWord(p.abbrevTable + (((((int) c) - 1) * 32 + ((int) c2)) * 2));
                    abbrevAddr *= 2; // Word address
                    decodedstr.append(decodeZString(p, abbrevAddr));
                }
                break;
            case 3:
                if( p.version <= 2 )
                { // Shift down
                    currentAlphabet = alphabet.shiftDown(currentAlphabet);
                }
                else
                { // Abbreviation
                    i++;
                    if( i >= zlen ) break;
                    c2 = (char) zchars[i];
                    abbrevAddr = p.memory.fetchWord(p.abbrevTable + (((((int) c) - 1) * 32 + ((int) c2)) * 2));
                    abbrevAddr *= 2; // Word address
                    decodedstr.append(decodeZString(p, abbrevAddr));
                }
                break;
            case 4: // Always a shift up
                currentAlphabet = alphabet.shiftUp(currentAlphabet);
                if( p.version <= 2 ) lockAlphabet = currentAlphabet;
                break;
            case 5: // Always a shift down
                currentAlphabet = alphabet.shiftDown(currentAlphabet);
                if( p.version <= 2 ) lockAlphabet = currentAlphabet;
                break;
            case 6: // Literal output character if alphabet is P.
                if( currentAlphabet == ALPHABET.P )
                {
                    i++;
                    if( i >= zlen ) break;
                    c2 = (char) zchars[i];
                    i++;
                    if( i >= zlen ) break;
                    c3 = (char) zchars[i];
                    w = ((((int) c2 << 5) & 0x03e0) | ((int) c3 & 0x1f));
                    decodedstr.append(String.valueOf((char) w));
                    currentAlphabet = lockAlphabet;
                }
                else
                {
                    decodedstr.append(alphabet.getChar(currentAlphabet, c));
                    currentAlphabet = lockAlphabet;
                }
                break;
            default:
                decodedstr.append(alphabet.getChar(currentAlphabet, c));
                currentAlphabet = lockAlphabet;
                break;
            }
        }

        // We're done!
        return decodedstr.toString();
    }

    // Encode text into a Z-String, represented as a vector of Integers.
    protected Vector<Integer> encodeZString(ZCPU p, String text)
    {
        ZAlphabet alphabet = ZAlphabet.Get(p.version);
        Vector<Integer> outbuf;
        int curtextindex, textlen;
        char curchar;
        int i;
        boolean found;

        outbuf = new Vector<Integer>();
        curtextindex = 0;
        textlen = text.length();

        // Go through the string, converting characters as we go.
        for( curtextindex = 0; curtextindex < textlen; curtextindex++ )
        {
            curchar = text.charAt(curtextindex);

            // First, try some shortcuts if we're not using an alternate
            // character set.
            if( !p.altCharSet )
            {
                if( Character.isLowerCase(curchar) )
                { // Alphabet L
                    outbuf.addElement(new Integer(((int) ((curchar - 'a') + 6))));
                    continue;
                }
                else if( Character.isUpperCase(curchar) )
                {
                    if( p.version < 3 )
                        outbuf.addElement(new Integer(2));
                    else
                        outbuf.addElement(new Integer(4));
                    outbuf.addElement(new Integer(((int) ((curchar - 'A') + 6))));
                    continue;
                }
            }

            // If the character is a cr or lf, encode it as a newline.
            if( (curchar == '\r') || (curchar == '\n') )
            {
                if( p.version == 1 )
                { // Only needs one character in V1
                    outbuf.addElement(new Integer(1));
                    continue;
                }

                // Otherwise, two bytes are needed.
                if( p.version < 3 )
                    outbuf.addElement(new Integer(3));
                else
                    outbuf.addElement(new Integer(5));
                outbuf.addElement(new Integer(7));
                continue;
            }

            // See if the character is in each alphabet. This means either it's
            // punctuation or we have an alternate character set.
            found = false;
            for( i = 6; (i < 32); i++ )
            {
                if( alphabet.getChar(ALPHABET.L, i) == curchar )
                {
                    outbuf.addElement(new Integer(i));
                    found = true;
                }
                else if( alphabet.getChar(ALPHABET.U, i) == curchar )
                {
                    if( p.version < 3 )
                        outbuf.addElement(new Integer(2));
                    else
                        outbuf.addElement(new Integer(4));
                    outbuf.addElement(new Integer(i));
                    found = true;
                }
                else if( alphabet.getChar(ALPHABET.P, i) == curchar )
                {
                    if( p.version < 3 )
                        outbuf.addElement(new Integer(3));
                    else
                        outbuf.addElement(new Integer(5));
                    outbuf.addElement(new Integer(i));
                    found = true;
                }
            }

            // If the character was found, continue. Otherwise, store it as a
            // literal.
            if( found ) continue;

            if( p.version < 3 ) // Shift to alphabetP
                outbuf.addElement(new Integer(3));
            else
                outbuf.addElement(new Integer(5));
            outbuf.addElement(new Integer(6)); // Literal escape
            outbuf.addElement(new Integer(((((int) curchar) >> 5) & 0x1f))); // Top
                                                                             // 5
                                                                             // bits
            outbuf.addElement(new Integer(((int) curchar) & 0x1f)); // Bottom 5
                                                                    // bits
        }

        // Return the encoded string.
        return outbuf;
    }

    // Return a signed version of a word
    protected int signedWord(int w)
    {
        if( (w & 0x8000) == 0x8000 )
        {
            return (w - 65536);
        }
        else
        {
            return (w);
        }
    }

    // Encode a signed word
    protected int unsignedWord(int w)
    {
        w = w & 0xffff;

        if( w < 0 )
        {
            return (65536 - (-w));
        }
        else
        {
            return (w);
        }
    }

}
