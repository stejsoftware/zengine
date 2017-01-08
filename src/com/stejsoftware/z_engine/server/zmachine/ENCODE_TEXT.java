/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import java.util.Vector;
import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;


/**
 * @author jon
 * 
 *         ENCODE_TEXT baddr1 p n baddr2 V5+
 */
public class ENCODE_TEXT extends ZInstructionBase
{
    public ENCODE_TEXT()
    {
        m_code = 0x1c;
        m_type = OPCODE_TYPE.VAR;
    }

    @Override
    public void call(ZCPU p)
    {

        String s;
        Vector<Integer> encodedstr;
        int curindex, maxlen, encodedlen;
        int i, w;
        Integer n;

        // First, make a string out of the text to encode.
        s = new String();
        for( i = 0; i < p.vops[2]; i++ )
        {
            s = s + String.valueOf((char) p.memory.fetchByte(p.vops[0] + p.vops[1] + i));
        }

        // Encode it.
        encodedstr = encodeZString(p, s);

        // Now copy to memory, storing Z-characters appropriately and respecting
        // length limits.
        if( p.version < 4 )
        {
            maxlen = 2;
        }
        else
        {
            maxlen = 3;
        }

        curindex = 0;
        encodedlen = encodedstr.size();

        for( i = 0; i < maxlen; i++ )
        {
            w = 0;
            if( curindex < encodedlen )
            {
                n = (Integer) encodedstr.elementAt(curindex);
                w = w | ((n.intValue() << 10) & 0x7c00);
                curindex++;
            }
            else
                w = w | ((5 << 10) & 0x7c00);
            if( curindex < encodedlen )
            {
                n = (Integer) encodedstr.elementAt(curindex);
                w = w | ((n.intValue() << 5) & 0x03e0);
                curindex++;
            }
            else
                w = w | ((5 << 5) & 0x3e0);
            if( curindex < encodedlen )
            {
                n = (Integer) encodedstr.elementAt(curindex);
                w = w | (n.intValue() & 0x001f);
                curindex++;
            }
            else
                w = w | 5;
            p.memory.putWord((p.vops[2] + (i * 2)), w);
        }
    }
}
