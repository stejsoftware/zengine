/**
 * Modifications
 * Copyright (c) 2011 Jonathan J. Meyer <jon@stej.com>
 */

/**
 * Copyright (c) 2008 Matthew E. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.stejsoftware.z_engine.zmachine;

import java.io.IOException;

/**
 * 
 * 
 *
 */
public class ZMemory
{
    private ZUserInterface zui        = null;
    private int            dataLength = 0;
    private byte[]         data       = null;

    /**
     * 
     * @param cpu
     */
    ZMemory(ZCPU cpu)
    {
        zui = cpu.zui;
    }

    /**
     * Returns if the memory is empty or not.
     * 
     * @return
     */
    boolean isEmpty()
    {
        return (dataLength - 1) < 0;
    }

    /**
     * The initialize routine sets things up and loads a game into memory. It is
     * passed the ZUserInterface object for this ZMachine and the filename of
     * the story-file.
     * 
     * @param story
     */
    void initialize(ZStory story)
    {
        dataLength = story.size();
        data = new byte[dataLength];

        story.get_data(data);
    }

    /**
     * Fetch a byte from the specified address
     * 
     * @param addr
     * @return
     */
    public int fetchByte(int addr)
    {
        if( addr > (dataLength - 1) )
        {
            zui.fatal("Memory fault: address " + addr);
        }

        int i = (data[addr] & 0xff);

        return i;
    }

    /**
     * Store a byte at the specified address
     * 
     * @param addr
     * @param b
     */
    public void putByte(int addr, int b)
    {
        if( addr > (dataLength - 1) )
        {
            zui.fatal("Memory fault: address " + addr);
        }

        data[addr] = (byte) (b & 0xff);
    }

    /**
     * Fetch a word from the specified address
     * 
     * @param addr
     * @return
     */
    public int fetchWord(int addr)
    {
        int i;

        if( addr > (dataLength - 1) )
        {
            zui.fatal("Memory fault: address " + addr);
        }

        i = (((data[addr] << 8) | (data[addr + 1] & 0xff)) & 0xffff);

        return i;
    }

    /**
     * Store a word at the specified address
     * 
     * @param addr
     * @param w
     */
    public void putWord(int addr, int w)
    {
        if( addr > (dataLength - 1) )
        {
            zui.fatal("Memory fault: address " + addr);
        }

        data[addr] = (byte) ((w >> 8) & 0xff);
        data[addr + 1] = (byte) (w & 0xff);
    }

    /**
     * Dump the specified amount of memory, starting at the specified address,
     * to the specified DataOutputStream.
     */
    public void dumpMemory(ZOutputStream dos, int addr, int len) throws IOException
    {
        dos.write(data, addr, len);
    }

    /**
     * Read in memory stored by dumpMemory.
     * 
     * @param dis
     * @param addr
     * @param len
     * @throws IOException
     */
    public void readMemory(ZInputStream dis, int addr, int len) throws IOException
    {
        dis.read(data, addr, len);
    }
}
