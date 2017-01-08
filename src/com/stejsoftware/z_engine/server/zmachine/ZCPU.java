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
import java.util.Vector;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * The ZCPU class implements the Central Processing Unit of a ZMachine, and is
 * the ZMachine's interface to the outside world. With the assistance of other
 * classes in the zmachine package, and of a class supplied by the programmer
 * that implements the ZUserInterface interface, this class loads and executes
 * Z-code programs in the standard Infocom/Inform story-file format.
 * 
 * @author Matt Kimmel
 */
public class ZCPU extends Object
{
    // Private constants
    private enum RUNSTATE
    {
        NONE,
        INIT,
        RUN,
        PAUSE,
        STOP,
        ERROR
    };

    // Argument types
    public static final int     ARGTYPE_BYTE      = 0;                      // Byte
    public static final int     ARGTYPE_WORD      = 1;                      // Word

    // The story we're using
    private ZStory              curStory          = null;
    // User interface supplied to constructor
    protected ZUserInterface    zui               = null;
    // This ZMachine's memory
    protected ZMemory           memory            = null;
    // This ZMachine's object table
    protected ZObjectTable      objTable          = null;
    // This ZMachine's call stack
    protected Stack<ZCallFrame> callStack         = new Stack<ZCallFrame>();
    // This ZMachine's random number generator
    protected ZRandom           rndgen            = new ZRandom();
    // This ZMachine's I/O card
    protected ZIOCard           ioCard            = null;
    // the current running state of this program
    private RUNSTATE            run_state         = RUNSTATE.NONE;
    // Version of the game we're playing.
    protected int               version           = 0;
    // Scaling factor for this program
    protected int               programScale      = 0;
    // Current call frame
    protected ZCallFrame        curCallFrame      = null;
    // Instruction currently being executed
    private int                 curInstruction    = 0;
    // Opcode (untyped instruction) being executed
    private int                 curOpcode         = 0;
    // Type of current opcode;
    private OPCODE_TYPE         curOpcodeType     = OPCODE_TYPE.None;
    // Current operands for 1OPs and 2OPs and their types.
    protected int               op1, op2, op1type, op2type;
    // Current operands for VARs and EXTs
    protected int[]             vops              = new int[8];
    // Type of current VAR/EXT operands
    protected int[]             voptypes          = new int[8];
    // Number of operands for VARs and EXTs
    protected int               numvops           = 0;
    // Current branch argument
    protected int               curBranch         = 0;
    // Current branch logic reversed?
    protected boolean           curBranchReversed = false;
    // Current result argument
    protected int               curResult         = 0;
    // Current string argument
    protected String            curString         = "";
    // Set to true when decodeLoop must return
    protected boolean           decode_ret_flag   = false;
    // Value from last RET instruction, if returning from interrupt
    protected int               ret_value         = 0;
    // Location in memory of abbreviation table.
    protected int               abbrevTable       = 0;
    // Location in memory of global variables
    private int                 globalVars        = 0;
    // Size of dynamic memory
    protected int               dynamicMemorySize = 0;
    // true if this is a restart
    protected boolean           restartFlag       = false;
    // Address of main dictionary
    protected int               mainDictionary    = 0;
    // Current undo state
    protected byte[]            undoState         = null;
    // Set to true whenever NEW_LINE called--used by READ
    protected boolean           did_newline       = false;
    // True if we're using an alternate character set
    protected boolean           altCharSet        = false;

    /**
     * The constructor takes an object that implements the ZUserInterface
     * interface as an argument, and initializes various variables and objects
     * (but does not load or start a game).
     * 
     * @param ui
     */
    public ZCPU(ZUserInterface ui)
    {
        zui = ui;
        memory = new ZMemory(this);
        objTable = new ZObjectTable(this);
        ioCard = new ZIOCard(this);
    }

    public void Run()
    {
        if( isPaused() )
        {
            decodeLoop();
        }
    }

    /**
     * 
     * The start method starts execution of the story-file as a separate thread.
     * 
     * @param state
     */
    public void Start()
    {
        // Reinitialize if this is a restart
        if( restartFlag && curStory != null )
        {
            Load(curStory);
        }

        if( isInitialized() )
        {
            // Create an initial call-stack frame
            curCallFrame = new ZCallFrame();

            curCallFrame.pc = memory.fetchWord(0x06);
            curCallFrame.routineStack = new Stack<Integer>();
            curCallFrame.numLocalVars = 0;

            // This should never be examined.
            curCallFrame.callType = ZCallFrame.INTERRUPT;
            curCallFrame.argCount = 0;
            curCallFrame.frameNumber = 0;

            callStack = new Stack<ZCallFrame>();

            Run();
        }
    }

    /**
     * This is the main loop of the ZMachine. It decodes instructions and
     * executes them. It may be called recursively during interrupts.
     * 
     * @return
     */
    private void decodeLoop()
    {
        int v;
        int typebyte;
        int maxops;
        boolean done;

        while( isRunning() )
        {
            // Decode in an endless loop
            // Grab the opcode, adjust the PC accordingly. It is
            // up to the implementation of each instruction to get
            // its own result, branch and string arguments using
            // utility functions.

            // this line will slow things down too!!!
            // DebugConsole.log( "CPU State: " +state.toString(p) + "; pc:" +
            // Integer.toHexString(curCallFrame.pc)); // debug

            curInstruction = memory.fetchByte(curCallFrame.pc);
            curCallFrame.pc++;

            // /////////////////////////////////////////////////////////
            // Get the operands for this instruction, and type it.
            // /////////////////////////////////////////////////////////
            if( (curInstruction >= 0x00) && (curInstruction <= 0x7f) )
            {
                // A non-variable 2OP.
                // Get first operand
                if( (curInstruction & 0x40) == 0x40 )
                { // A variable number
                    v = memory.fetchByte(curCallFrame.pc);
                    op1 = getVariable(v);
                    op1type = ARGTYPE_WORD;
                }
                else
                { // A byte constant
                    op1 = memory.fetchByte(curCallFrame.pc);
                    op1type = ARGTYPE_BYTE;
                }
                curCallFrame.pc++;

                // Get second operand
                if( (curInstruction & 0x20) == 0x20 )
                { // A variable number
                    v = memory.fetchByte(curCallFrame.pc);
                    op2 = getVariable(v);
                    op2type = ARGTYPE_WORD;
                }
                else
                { // A byte constant
                    op2 = memory.fetchByte(curCallFrame.pc);
                    op2type = ARGTYPE_BYTE;
                }
                curCallFrame.pc++;

                curOpcodeType = OPCODE_TYPE.OP2;// OPTYPE_2OP;
                curOpcode = (curInstruction & 0x1f);
            }
            else if( (curInstruction >= 0x80) && (curInstruction <= 0xaf) )
            {
                // A 1OP.
                switch( curInstruction & 0x30 )
                {
                case 0x00: // A word constant
                    op1 = memory.fetchWord(curCallFrame.pc);
                    op1type = ARGTYPE_WORD;
                    curCallFrame.pc += 2;
                    break;
                case 0x10: // A byte constant
                    op1 = memory.fetchByte(curCallFrame.pc);
                    op1type = ARGTYPE_BYTE;
                    curCallFrame.pc++;
                    break;
                case 0x20: // A variable
                    v = memory.fetchByte(curCallFrame.pc);
                    op1 = getVariable(v);
                    op1type = ARGTYPE_WORD;
                    curCallFrame.pc++;
                    break;
                }

                curOpcodeType = OPCODE_TYPE.OP1;// OPTYPE_1OP;
                curOpcode = (curInstruction & 0x0f);
            }
            else if( (curInstruction >= 0xb0) && (curInstruction <= 0xbf) && (curInstruction != 0xbe) )
            {
                // A 0OP.
                curOpcodeType = OPCODE_TYPE.OP0;// OPTYPE_0OP;
                curOpcode = (curInstruction & 0x0f);
            }
            else if( (curInstruction >= 0xc0) && (curInstruction <= 0xdf) && (curInstruction != 0xc1) )
            {
                // A variable 2OP.
                // Get the type byte.
                typebyte = memory.fetchByte(curCallFrame.pc);
                curCallFrame.pc++;

                // Get the first operand
                switch( typebyte & 0xc0 )
                {
                case 0x00: // Word constant
                    op1 = memory.fetchWord(curCallFrame.pc);
                    op1type = ARGTYPE_WORD;
                    curCallFrame.pc += 2;
                    break;
                case 0x40: // A byte constant
                    op1 = memory.fetchByte(curCallFrame.pc);
                    op1type = ARGTYPE_BYTE;
                    curCallFrame.pc++;
                    break;
                case 0x80: // A variable
                    v = memory.fetchByte(curCallFrame.pc);
                    op1 = getVariable(v);
                    op1type = ARGTYPE_WORD;
                    curCallFrame.pc++;
                    break;
                case 0xc0: // An error
                    zui.fatal("Error: Variable 2OP with no ops.");
                }

                // Get the second operand
                switch( typebyte & 0x30 )
                {
                case 0x00: // Word constant
                    op2 = memory.fetchWord(curCallFrame.pc);
                    op2type = ARGTYPE_WORD;
                    curCallFrame.pc += 2;
                    break;
                case 0x10: // A byte constant
                    op2 = memory.fetchByte(curCallFrame.pc);
                    op2type = ARGTYPE_BYTE;
                    curCallFrame.pc++;
                    break;
                case 0x20: // A variable
                    v = memory.fetchByte(curCallFrame.pc);
                    op2 = getVariable(v);
                    op2type = ARGTYPE_WORD;
                    curCallFrame.pc++;
                    break;
                case 0x30: // An error
                    zui.fatal("Error: Variable 2OP with one o");
                }

                curOpcodeType = OPCODE_TYPE.OP2;// OPTYPE_2OP;
                curOpcode = (curInstruction & 0x1f);
            }
            else if( ((curInstruction >= 0xe0) && (curInstruction <= 0xff)) || (curInstruction == 0xc1) )
            {
                // Variable instruction, or 0xc1 (JE with up to 4 operands)
                // Get the operands
                numvops = 0;
                if( (curInstruction == 0xec) || (curInstruction == 0xfa) )
                {
                    // Double-variables
                    typebyte = memory.fetchWord(curCallFrame.pc);
                    curCallFrame.pc += 2;
                    maxops = 8;
                }
                else
                {
                    typebyte = memory.fetchByte(curCallFrame.pc);
                    curCallFrame.pc++;
                    maxops = 4;
                }
                done = false;
                for( int i = 0; ((i < maxops) && (!done)); i++ )
                {
                    switch( (typebyte >> ((maxops - 1 - i) * 2)) & 0x03 )
                    {
                    case 0x00: // Word constant
                        vops[i] = memory.fetchWord(curCallFrame.pc);
                        voptypes[i] = ARGTYPE_WORD;
                        curCallFrame.pc += 2;
                        numvops++;
                        break;
                    case 0x01: // Byte constant
                        vops[i] = memory.fetchByte(curCallFrame.pc);
                        voptypes[i] = ARGTYPE_BYTE;
                        curCallFrame.pc++;
                        numvops++;
                        break;
                    case 0x02: // A variable
                        v = memory.fetchByte(curCallFrame.pc);
                        vops[i] = getVariable(v);
                        voptypes[i] = ARGTYPE_WORD;
                        curCallFrame.pc++;
                        numvops++;
                        break;
                    case 0x03: // End of arguments
                        done = true;
                        break;
                    }
                }

                if( curInstruction == 0xc1 )
                {
                    curOpcodeType = OPCODE_TYPE.OP2;// OPTYPE_2OP;
                    curOpcode = 0x01;
                }
                else
                {
                    curOpcodeType = OPCODE_TYPE.VAR;// OPTYPE_VAR;
                    curOpcode = (curInstruction & 0x1f);
                }
            }
            else if( curInstruction == 0xbe )
            {
                // Extended instruction. Decode similarly to a variable
                // instruction.
                curOpcodeType = OPCODE_TYPE.EXT;// OPTYPE_EXT;
                curOpcode = memory.fetchByte(curCallFrame.pc);
                curCallFrame.pc++;

                numvops = 0;
                typebyte = memory.fetchByte(curCallFrame.pc);
                curCallFrame.pc++;
                done = false;
                for( int i = 0; ((i < 4) && (!done)); i++ )
                {
                    switch( (typebyte >> ((3 - i) * 2)) & 0x03 )
                    {
                    case 0x00: // Word constant
                        vops[i] = memory.fetchWord(curCallFrame.pc);
                        voptypes[i] = ARGTYPE_WORD;
                        curCallFrame.pc += 2;
                        numvops++;
                        break;
                    case 0x01: // Byte constant
                        vops[i] = memory.fetchByte(curCallFrame.pc);
                        voptypes[i] = ARGTYPE_BYTE;
                        curCallFrame.pc++;
                        numvops++;
                        break;
                    case 0x02: // A variable
                        v = memory.fetchByte(curCallFrame.pc);
                        vops[i] = getVariable(v);
                        voptypes[i] = ARGTYPE_WORD;
                        curCallFrame.pc++;
                        numvops++;
                        break;
                    case 0x03: // End of arguments
                        done = true;
                        break;
                    }
                }
            }
            else
            {
                // This should never happen.
                zui.fatal("Malformed instruction: " + curInstruction);
            }

            // /////////////////////////////////////////////////////////
            // Dispatch the instruction.
            // /////////////////////////////////////////////////////////

            call(this, curOpcodeType, curOpcode);

            if( decode_ret_flag )
            {
                // An instruction has indicated that this decodeLoop
                // should return.
                decode_ret_flag = false;
            }
        } // end while loop
    }

    // /////////////////////////////////////////////////////////////////
    // Utility functions
    // /////////////////////////////////////////////////////////////////

    // Save the state of the Z-Machine--that is, the current call frame
    // and the call frame stack.
    protected void dumpState(ZOutputStream dos)
    {
        int i;
        Stack<ZCallFrame> tmpstack;
        int n = 0;
        ZCallFrame thisframe;

        // First, save the current call frame.
        dos.writeInt(curCallFrame.pc);
        for( i = 0; i < 15; i++ )
            dos.writeInt(curCallFrame.localVars[i]);
        dos.writeInt(curCallFrame.numLocalVars);
        dos.writeInt(curCallFrame.callType);
        dos.writeInt(curCallFrame.argCount);
        dos.writeInt(curCallFrame.frameNumber);
        dumpStack(dos, curCallFrame.routineStack);

        // Now, to save things in the proper order, we deconstruct the original
        // call stack and put in in a new stack, in reverse order. It is
        // reconstructed as it is written out.
        tmpstack = new Stack<ZCallFrame>();
        while( !callStack.empty() )
        {
            tmpstack.push(callStack.pop());
            n++;
        }

        // Write the number of stack entries, then each entry.
        dos.writeInt(n);
        while( !tmpstack.empty() )
        {
            thisframe = (ZCallFrame) tmpstack.pop();
            dos.writeInt(thisframe.pc);
            for( i = 0; i < 15; i++ )
                dos.writeInt(thisframe.localVars[i]);
            dos.writeInt(thisframe.numLocalVars);
            dos.writeInt(thisframe.callType);
            dos.writeInt(thisframe.argCount);
            dos.writeInt(thisframe.frameNumber);
            dumpStack(dos, thisframe.routineStack);
            callStack.push(thisframe);
        }
    }

    // Read a game state from a file, as saved by dumpState(p).
    protected void readState(ZInputStream dis)
    {
        int i, j;
        int nframes;
        ZCallFrame thisframe;

        // Get the current call frame
        curCallFrame = new ZCallFrame();
        curCallFrame.pc = dis.readInt();
        for( i = 0; i < 15; i++ )
            curCallFrame.localVars[i] = dis.readInt();
        curCallFrame.numLocalVars = dis.readInt();
        curCallFrame.callType = dis.readInt();
        curCallFrame.argCount = dis.readInt();
        curCallFrame.frameNumber = dis.readInt();
        curCallFrame.routineStack = new Stack<Integer>();
        readStack(dis, curCallFrame.routineStack);

        // Now get the call stack
        callStack = new Stack<ZCallFrame>();
        nframes = dis.readInt();
        for( j = 0; j < nframes; j++ )
        {
            thisframe = new ZCallFrame();
            thisframe.pc = dis.readInt();
            for( i = 0; i < 15; i++ )
                thisframe.localVars[i] = dis.readInt();
            thisframe.numLocalVars = dis.readInt();
            thisframe.callType = dis.readInt();
            thisframe.argCount = dis.readInt();
            thisframe.frameNumber = dis.readInt();
            thisframe.routineStack = new Stack<Integer>();
            readStack(dis, thisframe.routineStack);
            callStack.push(thisframe);
        }
    }

    // Called by dumpState--dumps the contents of a stack of Integers.
    public static void dumpStack(ZOutputStream dos, Stack<Integer> st)
    {
        Stack<Integer> tmpstack;
        int n = 0;
        Integer i;

        // We put the contents of the stack in another stack to reverse
        // their order before writing them.
        tmpstack = new Stack<Integer>();
        while( !st.empty() )
        {
            tmpstack.push(st.pop());
            n++;
        }

        // Then we write the contents to a file, starting with the number of
        // elements on the stack.
        dos.writeInt(n);
        while( !tmpstack.empty() )
        {
            i = (Integer) tmpstack.pop();
            dos.writeInt(i.intValue());
            st.push(i);
        }
    }

    // Called by readState--reads a stack saved by dumpStack(p).
    public static void readStack(ZInputStream dis, Stack<Integer> st)
    {
        int nelements;
        Integer tmp;
        int i;

        // Get the number of elements
        nelements = dis.readInt();

        // Get the elements
        for( i = 0; i < nelements; i++ )
        {
            tmp = new Integer(dis.readInt());
            st.push(tmp);
        }
    }

    /**
     * This operation will call a zop code
     * 
     */
    protected void call(OPCODE_TYPE type, int opcode)
    {
        ZInstructionSet.GetInstance().call(this, type, opcode);
    }

    protected static void call(ZCPU cpu, OPCODE_TYPE type, int opcode)
    {
        ZInstructionSet.GetInstance().call(cpu, type, opcode);
    }

    public boolean isInitialized()
    {
        return run_state == RUNSTATE.INIT;
    }

    public boolean isRunning()
    {
        return run_state == RUNSTATE.RUN;
    }

    public boolean isPaused()
    {
        return run_state == RUNSTATE.PAUSE;
    }

    public boolean isStopped()
    {
        return run_state == RUNSTATE.STOP;
    }

    // The initialize method does several things: loads a game;
    // calls the initialize methods of the other objects; modifies
    // IROM as appropriate to the capabilities of the ZMachine and
    // the ZUserInterface.
    public boolean Load(ZStory story)
    {
        int i;
        boolean transcriptOn = false;
        Dimension s;
        int termChars;

        // If this is a restart, remember the value of the printer
        // transcript bit.

        if( (memory.fetchWord(0x10) & 0x01) == 0x01 )
        {
            transcriptOn = true;
        }
        else
        {
            transcriptOn = false;
        }

        // First, initialize all of the objects. For the ZMemory
        // object, this includes loading the game file.
        curStory = story;
        memory.initialize(story);
        version = memory.fetchByte(0x00);

        if( (version < 1) || (version > 8) || (version == 6) )
        {
            zui.fatal("Unsupported storyfile version: " + String.valueOf(version) + ".");
        }

        zui.initialize(version);
        ioCard.initialize(true);
        objTable.initialize();

        // Get the program scale
        if( version <= 3 )
        {
            programScale = 2;
        }
        else if( (version == 4) || (version == 5) )
        {
            programScale = 4;
        }
        else
        {
            programScale = 8;
        }

        // Now do necessary modifications to the IROM.
        // First the byte at 0x01 (the only byte used in V1-3).
        i = memory.fetchByte(0x01);

        // Set bits at 0x01 for V1-3 games
        if( version <= 3 )
        {
            i = i & ~0x08; // Tandy bit off
            if( zui.hasStatusLine() )
                i = i & ~0x10; // Status line not not available
            else
                i = i | 0x10; // Status line not available
            if( zui.hasUpperWindow() )
                i = i | 0x20; // Upper window available
            else
                i = i & ~0x20; // Upper window not available
            if( zui.defaultFontProportional() )
                i = i | 0x40; // Default font is proportional
            else
                i = i & ~0x40; // Default font is fixed-width
        }
        else
        { // Set bits for V4+ games
            if( (version >= 5) && (zui.hasColors()) ) i = i | 0x01;
            // V6 picture bit at bit 1
            if( zui.hasBoldface() ) i = i | 0x04;
            if( zui.hasItalic() ) i = i | 0x08;
            if( zui.hasFixedWidth() ) i = i | 0x10;
            // V6 sound bit at bit 5
            if( zui.hasTimedInput() ) i = i | 0x80;
        }

        memory.putByte(0x01, i);

        // In V4+, set various other IROM bytes
        if( version >= 4 )
        {
            memory.putByte(0x1e, 6); // We'll say we're an MS-DOS
                                     // interpreter
            memory.putByte(0x1f, (byte) 'A'); // Interpreter version

            // Screen height and width in characters
            s = zui.getScreenCharacters();
            memory.putByte(0x20, s.height);
            memory.putByte(0x21, s.width);

            // Screen height and width in units, font size in units, colors
            // (V5+)
            if( version >= 5 )
            {
                s = zui.getScreenUnits();
                memory.putWord(0x22, s.width);
                memory.putWord(0x24, s.height);
                s = zui.getFontSize();
                memory.putByte(0x26, s.height);
                memory.putByte(0x27, s.width);
                memory.putByte(0x2c, zui.getDefaultBackground());
                memory.putByte(0x2d, zui.getDefaultForeground());
            }
        }

        // If we're restarting, restore the printer transcript bit
        if( restartFlag )
        {
            i = memory.fetchWord(0x10);
            if( transcriptOn )
                i = i | 0x01;
            else
                i = i & ~0x01;
            memory.putWord(0x10, i);
            restartFlag = false; // From here on, it's a new program
        }

        // Get the location of the abbreviation table
        if( version > 1 ) abbrevTable = memory.fetchWord(0x18);

        // Get the location of the global variable table
        globalVars = memory.fetchWord(0x0c);

        // Get the location of the main dictionary
        mainDictionary = memory.fetchWord(0x08);

        // Get size of dynamic memory
        dynamicMemorySize = memory.fetchWord(0x0e);

        // Get any additional terminating characters, and pass them to
        // the user interface. (V5+)
        if( version >= 5 )
        {
            termChars = memory.fetchWord(0x2e);

            if( termChars != 0 )
            {
                i = 0;
                int tc = memory.fetchByte(termChars);

                Vector<Integer> terminators = new Vector<Integer>();

                while( tc != 0 )
                {
                    terminators.addElement(new Integer(tc));
                    i++;
                    tc = memory.fetchByte(termChars + i);
                }

                zui.setTerminatingCharacters(terminators);
            }
        }

        // Get the alternate character set, if there is one,
        // in V5+ games. (currently not implemented)

        return isInitialized();
    }

    /**
     * 
     * This function handles requests to get the value of a variable. Variable 0
     * refers to the top of the routine stack; variables 1-15 refer to local
     * variables; and variables 16-255 refer to global variables.
     * 
     * @param v
     * @return
     */
    protected int getVariable(int v)
    {
        if( v == 0 )
        { // The top of the routine stack
            if( curCallFrame.routineStack.empty() )
                zui.fatal("Routine stack underflow");
            else
            {
                Integer i = curCallFrame.routineStack.pop();
                return (i.intValue());
            }
        }
        else if( (v >= 1) && (v <= 15) ) // Local variable
            // We don't bother checking whether the variable
            // exists -- the caller just gets a 0 if a non-existant
            // local variable is referenced.
            return (curCallFrame.localVars[v - 1]);
        else if( (v >= 16) && (v <= 255) ) // Global variable
            return (memory.fetchWord(globalVars + ((v - 16) * 2)));

        // If we get here, something's wrong.
        zui.fatal("Unspecified variable referenced");
        return (0); // To make javac happy
    }

    /**
     * This function handles requests to put the value of a variable, as above.
     * 
     * @param v
     * @param value
     */
    protected void putVariable(int v, int value)
    {
        value = value & 0xffff;

        if( v == 0 )
        { // Push this value onto the routine stack
            Integer i = new Integer(value);
            curCallFrame.routineStack.push(i);
        }
        else if( (v >= 1) && (v <= 15) ) // Local variable
            // Again, we don't bother checking the validity of
            // the local variable number.
            curCallFrame.localVars[v - 1] = value;
        else if( (v >= 16) && (v <= 255) ) // Global variable
            memory.putWord((globalVars + ((v - 16) * 2)), value);
        else
            zui.fatal("Unspecified variable referenced");
    }

    /**
     * Unpack a packed address. raddr is true if this is a routine address.
     * 
     * @param paddr
     * @param raddr
     * @return
     */
    protected int unpackAddr(int paddr, boolean raddr)
    {
        int addr = 0;
        int offset = 0;

        switch( version )
        {
        case 1:
        case 2:
        case 3:
            addr = 2 * paddr;
            break;
        case 4:
        case 5:
            addr = 4 * paddr;
            break;
        case 6:
        case 7:
            if( raddr )
                offset = memory.fetchWord(0x28);
            else
                offset = memory.fetchWord(0x2a);
            addr = (4 * addr) + (8 * offset);
            break;
        case 8:
            addr = 8 * paddr;
            break;
        }

        return addr;
    }

    /*
     * Call the routine at the given routine address as an interrupt.
     * 
     * Return the return value of the routine.
     */
    protected int interrupt(int raddr)
    {
        int addr;
        int newFrameAddr;
        int numvars;

        // Unpack the routine address and get number of local variables
        addr = unpackAddr(raddr, true);
        numvars = memory.fetchByte(addr);
        addr++;

        // Get a number for the new frame
        newFrameAddr = curCallFrame.frameNumber + 1;

        // Push the current call frame onto the stack
        callStack.push(curCallFrame);

        // Initialize a new call frame
        curCallFrame = new ZCallFrame();

        // Set pc to the beginning of the routine's code
        if( version < 5 )
            curCallFrame.pc = addr + (numvars * 2);
        else
            curCallFrame.pc = addr;

        // Get a new routine stack
        curCallFrame.routineStack = new Stack<Integer>();

        // Initialize local variables
        for( int i = 0; i < numvars; i++ )
        {
            if( version < 5 )
                curCallFrame.localVars[i] = memory.fetchWord(addr + (i * 2));
            else
                curCallFrame.localVars[i] = 0;
        }

        // Indicate that this routine was called as an interrupt
        curCallFrame.callType = ZCallFrame.INTERRUPT;

        // No arguments
        curCallFrame.argCount = 0;

        // Store frame number
        curCallFrame.frameNumber = newFrameAddr;

        // Now call decodeLoop recursively.
        // decodeLoop(p);

        // When we're done, ret_value will contain the routine's return value.
        return ret_value;
    }
}
