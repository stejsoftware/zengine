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
package com.stejsoftware.z_engine.server.zmachine;

import java.util.Stack;

/**
 * A frame on the ZMachine's call stack.
 * 
 * @author Matt Kimmel
 */
public class ZCallFrame
{
    // Constants used with calltype;
    public static final int FUNCTION  = 0;
    public static final int PROCEDURE = 1;
    public static final int INTERRUPT = 2;

    // Variables
    // Program counter
    public int              pc;
    // Routine stack
    public Stack<Integer>   routineStack;
    // Local variables
    public int[]            localVars = new int[15];
    // Number of local variables
    public int              numLocalVars;
    // How this routine was called
    public int              callType;
    // Argument count
    public int              argCount;
    // Used in CATCH and THROW. First frame is 0, increases from there.
    public int              frameNumber;
}
