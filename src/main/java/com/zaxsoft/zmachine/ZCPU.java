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
package com.zaxsoft.zmachine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ZCPU class implements the Central Processing Unit of a ZMachine, and is
 * the ZMachine's interface to the outside world. With the assistance of other
 * classes in the zmachine package, and of a class supplied by the programmer
 * that implements the ZUserInterface interface, this class loads and executes
 * Z-code programs in the standard Infocom/Inform story-file format.
 *
 * @author Matt Kimmel
 */
public class ZCPU extends Object implements Runnable
{
    // Private constants
    // Opcode types
    private final int         OPTYPE_0OP      = 0;           // 0OP opcode type
    private final int         OPTYPE_1OP      = 1;           // 1OP opcode type
    private final int         OPTYPE_2OP      = 2;           // 2OP opcode type
    private final int         OPTYPE_VAR      = 3;           // Variable opcode
                                                             // type
    private final int         OPTYPE_EXT      = 4;           // Extended opcode
                                                             // type

    // Argument types
    private final int         ARGTYPE_BYTE    = 0;           // Byte
    private final int         ARGTYPE_WORD    = 1;           // Word

    // Other objects associated with this ZMachine
    private ZMemory           memory;                        // This
                                                             // ZMachine's
                                                             // memory
    private ZObjectTable      objTable;                      // This
                                                             // ZMachine's
                                                             // object
                                                             // table
    private Stack<ZCallFrame> callStack;                     // This
                                                             // ZMachine's
                                                             // call
                                                             // stack
    private ZRandom           rndgen;                        // This
                                                             // ZMachine's
                                                             // random
                                                             // number
                                                             // generator
    private ZIOCard           ioCard;                        // This
                                                             // ZMachine's
                                                             // I/O
                                                             // card
    private ZUserInterface    zui;                           // User
                                                             // interface
                                                             // supplied
                                                             // to
                                                             // constructor

    // Private variables
    private String            curStoryFile;                  // The
                                                             // story-file
                                                             // we're
                                                             // using
    private int               version         = 0;           // Version
                                                             // of
                                                             // the
                                                             // game
                                                             // we're
                                                             // playing.
    private int               programScale;                  // Scaling
                                                             // factor
                                                             // for
                                                             // this
                                                             // program
    private ZCallFrame        curCallFrame;                  // Current
                                                             // call
                                                             // frame
    private int               curInstruction;                // Instruction
                                                             // currently
                                                             // being
                                                             // executed
    private int               curOpcode;                     // Opcode
                                                             // (untyped
                                                             // instruction)
                                                             // being
                                                             // executed
    private int               curOpcodeType;                 // Type
                                                             // of
                                                             // current
                                                             // opcode;
    private int               op1, op2, op1type, op2type;    // Current
                                                             // operands
                                                             // for
                                                             // 1OPs
                                                             // and
                                                             // 2OPs
                                                             // and
                                                             // their
                                                             // types.
    private int[]             vops            = new int[8];  // Current
                                                             // operands
                                                             // for
                                                             // VARs
                                                             // and
                                                             // EXTs
    private int[]             voptypes        = new int[8];  // Type
                                                             // of
                                                             // current
                                                             // VAR/EXT
                                                             // operands
    private int               numvops;                       // Number
                                                             // of
                                                             // operands
                                                             // for
                                                             // VARs
                                                             // and
                                                             // EXTs
    private int               curBranch;                     // Current
                                                             // branch
                                                             // argument
    private boolean           curBranchReversed;             // Current
                                                             // branch
                                                             // logic
                                                             // reversed?
    private int               curResult;                     // Current
                                                             // result
                                                             // argument
    private String            curString;                     // Current
                                                             // string
                                                             // argument
    private boolean           decode_ret_flag = false;       // Set
                                                             // to
                                                             // true
                                                             // when
                                                             // decodeLoop
                                                             // must
                                                             // return
    private int               ret_value;                     // Value
                                                             // from
                                                             // last
                                                             // RET
                                                             // instruction,
                                                             // if
                                                             // returning
                                                             // from
                                                             // interrupt
    private int               abbrevTable;                   // Location
                                                             // in
                                                             // memory
                                                             // of
                                                             // abbreviation
                                                             // table.
    private int               globalVars;                    // Location
                                                             // in
                                                             // memory
                                                             // of
                                                             // global
                                                             // variables
    private int               dynamicMemorySize;             // Size
                                                             // of
                                                             // dynamic
                                                             // memory
    private boolean           restartFlag;                   // true
                                                             // if
                                                             // this
                                                             // is
                                                             // a
                                                             // restart
    private int               mainDictionary;                // Address
                                                             // of
                                                             // main
                                                             // dictionary
    private byte[]            undoState;                     // Current
                                                             // undo
                                                             // state
    private boolean           did_newline     = false;       // Set
                                                             // to
                                                             // true
                                                             // whenever
                                                             // NEW_LINE
                                                             // called--used
                                                             // by
                                                             // READ
    private volatile Thread   execThread      = null;
    private Object            execThread_lock = new Object();

    // Default alphabets for decoding Z-Strings
    private boolean           altCharSet      = false;       // True if we're
                                                             // using an
                                                             // alternate
                                                             // character set
    private int               alphabetL       = 0;
    private int               alphabetU       = 1;
    private int               alphabetP       = 2;           /*
                                                              * Set to 3 in V1
                                                              */

    //@formatter:off
    public static void a(){};
    private char[][] alphabet = { 
      { ' ', '\0', '\0', '\0', '\0', '\0',  'a',  'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',  'r', 's', 't',  'u',   'v', 'w', 'x', 'y', 'z' },
      { ' ', '\0', '\0', '\0', '\0', '\0',  'A',  'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',  'R', 'S', 'T',  'U',   'V', 'W', 'X', 'Y', 'Z' },
      { ' ', '\0', '\0', '\0', '\0', '\0', '\0', '\n', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ',', '!', '?', '_',  '#', '\'', '\"', '/', '\\', '-', ':', '(', ')' },
      { ' ', '\0', '\0', '\0', '\0', '\0', '\0',  '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ',', '!', '?', '_', '#', '\'', '\"', '/', '\\',  '<', '-', ':', '(', ')' } 
    };
    //@formatter:on

    // The constructor takes an object that implements the
    // ZUserInterface interface as an argument, and initializes
    // various variables and objects (but does not load or start
    // a game).
    public ZCPU(ZUserInterface ui)
    {
        zui = ui;
        memory = new ZMemory();
        callStack = new Stack<ZCallFrame>();
        rndgen = new ZRandom();
        ioCard = new ZIOCard();
        objTable = new ZObjectTable();
    }

    // The initialize method does several things: loads a game;
    // calls the initialize methods of the other objects; modifies
    // IROM as appropriate to the capabilities of the ZMachine and
    // the ZUserInterface.
    public void initialize(String storyFile)
    {
        int i;
        boolean transcriptOn = false;
        Dimension s;
        int termChars;

        // If this is a restart, remember the value of the printer
        // transcript bit.
        if( restartFlag )
        {
            if( (memory.fetchWord(0x10) & 0x01) == 0x01 )
            {
                transcriptOn = true;
            }
            else
            {
                transcriptOn = false;
            }
        }

        // First, initialize all of the objects. For the ZMemory
        // object, this includes loading the game file.
        curStoryFile = storyFile;
        memory.initialize(zui, storyFile);
        version = memory.fetchByte(0x00);

        if( (version < 1) || (version > 8) || (version == 6) )
        {
            zui.fatal("Unsupported storyfile version: " + String.valueOf(version) + ".");
        }

        zui.initialize(version);
        rndgen.initialize(zui);
        ioCard.initialize(zui, memory, version, true);
        objTable.initialize(zui, memory, version);

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

        // If this is a V1 game, we need to use the V1 P alhabet.
        if( version == 1 )
        {
            alphabetP = 3;
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
            memory.putByte(0x1e, 6); // We'll say we're an MS-DOS interpreter
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
    }

    // The start method starts execution of the story-file as a separate thread.
    public boolean start()
    {
        // If the version number is 0, then we haven't loaded
        // a story-file yet. For now, this just means an immediate
        // return.
        if( version == 0 )
        {
            log.info("No story loaded");
            return false;
        }

        // Otherwise, start a new thread, which starts with this object's run()
        // method, and return a handle to the thread.
        Thread thread = new Thread(this, "ZMachine");
        thread.start();

        synchronized( execThread_lock )
        {
            execThread = thread;
        }

        return true;
    }

    public void stop()
    {
        synchronized( execThread_lock )
        {
            execThread = null;
        }
    }

    public boolean is_running()
    {
        boolean running;

        synchronized( execThread_lock )
        {
            running = (execThread != null);
        }

        return running;
    }

    // This method is called when the ZMachine thread is started.
    public void run()
    {
        do
        {
            // Reinitialize if this is a restart
            if( restartFlag )
            {
                initialize(curStoryFile);
                restartFlag = false;
            }

            // Create an initial call-stack frame
            curCallFrame = new ZCallFrame();
            curCallFrame.pc = memory.fetchWord(0x06);
            curCallFrame.routineStack = new Stack<Integer>();
            curCallFrame.numLocalVars = 0;
            curCallFrame.callType = ZCallFrame.INTERRUPT; // This should never
                                                          // be examined.
            curCallFrame.argCount = 0;
            curCallFrame.frameNumber = 0;
            callStack = new Stack<ZCallFrame>();

            // Now start executing code. The
            // return--if it does, we'll just return as well.
            decodeLoop();
        }
        while( restartFlag );

        return;
    }

    // This is the main loop of the ZMachine. It decodes instructions
    // and executes them. It may be called recursively during
    // interrupts.
    private void decodeLoop()
    {
        int v;
        int typebyte;
        int maxops;
        boolean done;

        Thread thread = Thread.currentThread();

        while( execThread == thread )
        {
            // Decode in an endless loop
            // Grab the opcode, adjust the PC accordingly. It is
            // up to the implementation of each instruction to get
            // its own result, branch and string arguments using
            // utility functions.
            curInstruction = memory.fetchByte(curCallFrame.pc);

            curCallFrame.pc++;

            ///////////////////////////////////////////////////////////
            // Get the operands for this instruction, and type it.
            ///////////////////////////////////////////////////////////
            if( (curInstruction >= 0x00) && (curInstruction <= 0x7f) )
            {
                // A non-variable 2OP.

                // Get first operand
                if( (curInstruction & 0x40) == 0x40 )
                {
                    // A variable number
                    v = memory.fetchByte(curCallFrame.pc);
                    op1 = getVariable(v);
                    op1type = ARGTYPE_WORD;
                }
                else
                {
                    // A byte constant
                    op1 = memory.fetchByte(curCallFrame.pc);
                    op1type = ARGTYPE_BYTE;
                }

                curCallFrame.pc++;

                // Get second operand
                if( (curInstruction & 0x20) == 0x20 )
                {
                    // A variable number
                    v = memory.fetchByte(curCallFrame.pc);
                    op2 = getVariable(v);
                    op2type = ARGTYPE_WORD;
                }
                else
                {
                    // A byte constant
                    op2 = memory.fetchByte(curCallFrame.pc);
                    op2type = ARGTYPE_BYTE;
                }

                curCallFrame.pc++;

                curOpcodeType = OPTYPE_2OP;
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

                curOpcodeType = OPTYPE_1OP;
                curOpcode = (curInstruction & 0x0f);
            }
            else if( (curInstruction >= 0xb0) && (curInstruction <= 0xbf) && (curInstruction != 0xbe) )
            {
                // A 0OP.
                curOpcodeType = OPTYPE_0OP;
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
                    zui.fatal("Error: Variable 2OP with one op.");
                }

                curOpcodeType = OPTYPE_2OP;
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
                    curOpcodeType = OPTYPE_2OP;
                    curOpcode = 0x01;
                }
                else
                {
                    curOpcodeType = OPTYPE_VAR;
                    curOpcode = (curInstruction & 0x1f);
                }
            }
            else if( curInstruction == 0xbe )
            {
                // Extended instruction. Decode similarly to a variable
                // instruction.
                curOpcodeType = OPTYPE_EXT;
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

            log.debug("{} [{}] {} {}", curCallFrame, String.format("0x%04X", curInstruction), opcodeTypeToString(curOpcodeType), String.format("0x%04X", curOpcode));

            ///////////////////////////////////////////////////////////
            // Dispatch the instruction.
            ///////////////////////////////////////////////////////////
            if( curOpcodeType == OPTYPE_0OP )
            {
                // 0OP opcodes.
                switch( curOpcode )
                {
                case 0x00:
                    zop_rtrue();
                    break;
                case 0x01:
                    zop_rfalse();
                    break;
                case 0x02:
                    getString();
                    zop_print();
                    break;
                case 0x03:
                    getString();
                    zop_print_rtrue();
                    break;
                case 0x04:
                    zop_nop();
                    break;
                case 0x05:
                    if( version < 4 )
                        getBranch();
                    else if( version == 4 )
                        getResult();
                    else
                        zui.fatal("SAVE 0OP unsupported after version 4.");
                    zop_save();
                    break;
                case 0x06:
                    if( version < 4 )
                        getBranch();
                    else if( version == 4 )
                        getResult();
                    else
                        zui.fatal("RESTORE 0OP unsupported after version 4.");
                    zop_restore();
                    break;
                case 0x07:
                    zop_restart();
                    break;
                case 0x08:
                    zop_ret_pulled();
                    break;
                case 0x09:
                    if( version < 5 )
                        zop_pop();
                    else
                    {
                        getResult();
                        zop_catch();
                    }
                    break;
                case 0x0a:
                    zop_quit();
                    break;
                case 0x0b:
                    zop_new_line();
                    break;
                case 0x0c:
                    zop_show_status();
                    break;
                case 0x0d:
                    getBranch();
                    zop_verify();
                    break;
                case 0x0e: // Start of extended instruction
                    zui.fatal("Found opcode 0xBE in 0OP dispatcher");
                case 0x0f:
                    getBranch();
                    zop_piracy();
                    break;
                default:
                    zui.fatal("Unknown 0OP - probably a bug.");
                }
            }
            else if( curOpcodeType == OPTYPE_1OP )
            {
                // 1OP opcodes
                switch( curOpcode )
                {
                case 0x00:
                    getBranch();
                    zop_jz();
                    break;
                case 0x01:
                    getResult();
                    getBranch();
                    zop_get_sibling();
                    break;
                case 0x02:
                    getResult();
                    getBranch();
                    zop_get_child();
                    break;
                case 0x03:
                    getResult();
                    zop_get_parent();
                    break;
                case 0x04:
                    getResult();
                    zop_get_prop_len();
                    break;
                case 0x05:
                    zop_inc();
                    break;
                case 0x06:
                    zop_dec();
                    break;
                case 0x07:
                    zop_print_addr();
                    break;
                case 0x08:
                    getResult();
                    zop_call_f0();
                    break;
                case 0x09:
                    zop_remove_obj();
                    break;
                case 0x0a:
                    zop_print_obj();
                    break;
                case 0x0b:
                    zop_ret();
                    break;
                case 0x0c:
                    zop_jump();
                    break;
                case 0x0d:
                    zop_print_paddr();
                    break;
                case 0x0e:
                    getResult();
                    zop_load();
                    break;
                case 0x0f:
                    if( version < 5 )
                    {
                        getResult();
                        zop_not();
                    }
                    else
                        zop_call_p0();
                    break;
                default:
                    zui.fatal("Unknown 1OP - probably a bug.");
                }
            }
            else if( curOpcodeType == OPTYPE_2OP )
            {
                // 2OP opcodes
                switch( curOpcode )
                {
                case 0x00:
                    zui.fatal("Unspecified instruction: " + curInstruction);
                case 0x01:
                    getBranch();
                    zop_je();
                    break;
                case 0x02:
                    getBranch();
                    zop_jl();
                    break;
                case 0x03:
                    getBranch();
                    zop_jg();
                    break;
                case 0x04:
                    getBranch();
                    zop_dec_jl();
                    break;
                case 0x05:
                    getBranch();
                    zop_inc_jg();
                    break;
                case 0x06:
                    getBranch();
                    zop_jin();
                    break;
                case 0x07:
                    getBranch();
                    zop_test();
                    break;
                case 0x08:
                    getResult();
                    zop_or();
                    break;
                case 0x09:
                    getResult();
                    zop_and();
                    break;
                case 0x0a:
                    getBranch();
                    zop_test_attr();
                    break;
                case 0x0b:
                    zop_set_attr();
                    break;
                case 0x0c:
                    zop_clear_attr();
                    break;
                case 0x0d:
                    zop_store();
                    break;
                case 0x0e:
                    zop_insert_obj();
                    break;
                case 0x0f:
                    getResult();
                    zop_loadw();
                    break;
                case 0x10:
                    getResult();
                    zop_loadb();
                    break;
                case 0x11:
                    getResult();
                    zop_get_prop();
                    break;
                case 0x12:
                    getResult();
                    zop_get_prop_addr();
                    break;
                case 0x13:
                    getResult();
                    zop_get_next_prop();
                    break;
                case 0x14:
                    getResult();
                    zop_add();
                    break;
                case 0x15:
                    getResult();
                    zop_sub();
                    break;
                case 0x16:
                    getResult();
                    zop_mul();
                    break;
                case 0x17:
                    getResult();
                    zop_div();
                    break;
                case 0x18:
                    getResult();
                    zop_mod();
                    break;
                case 0x19:
                    getResult();
                    zop_call_f1();
                    break;
                case 0x1a:
                    zop_call_p1();
                    break;
                case 0x1b:
                    zop_set_colour();
                    break;
                case 0x1c:
                    zop_throw();
                    break;
                case 0x1d:
                case 0x1e:
                case 0x1f:
                    zui.fatal("Unspecified instruction: " + curInstruction);
                default:
                    zui.fatal("Unknown 2OP.  Probably a bug.");
                }
            }
            else if( curOpcodeType == OPTYPE_VAR )
            {
                // VAR instructions
                switch( curOpcode )
                {
                case 0x00:
                    getResult();
                    zop_call_fv();
                    break;
                case 0x01:
                    zop_storew();
                    break;
                case 0x02:
                    zop_storeb();
                    break;
                case 0x03:
                    zop_put_prop();
                    break;
                case 0x04:
                    if( version >= 5 ) getResult();
                    zop_read();
                    break;
                case 0x05:
                    zop_print_char();
                    break;
                case 0x06:
                    zop_print_num();
                    break;
                case 0x07:
                    getResult();
                    zop_random();
                    break;
                case 0x08:
                    zop_push();
                    break;
                case 0x09:
                    zop_pull();
                    break;
                case 0x0a:
                    zop_split_screen();
                    break;
                case 0x0b:
                    zop_set_window();
                    break;
                case 0x0c:
                    getResult();
                    zop_call_fd();
                    break;
                case 0x0d:
                    zop_erase_window();
                    break;
                case 0x0e:
                    zop_erase_line();
                    break;
                case 0x0f:
                    zop_set_cursor();
                    break;
                case 0x10:
                    zop_get_cursor();
                    break;
                case 0x11:
                    zop_set_text_style();
                    break;
                case 0x12:
                    zop_buffer_mode();
                    break;
                case 0x13:
                    zop_output_stream();
                    break;
                case 0x14:
                    zop_input_stream();
                    break;
                case 0x15:
                    zop_sound();
                    break;
                case 0x16:
                    getResult();
                    zop_read_char();
                    break;
                case 0x17:
                    getResult();
                    getBranch();
                    zop_scan_table();
                    break;
                case 0x18:
                    getResult();
                    op1 = vops[0];
                    op1type = voptypes[0];
                    zop_not();
                    break;
                case 0x19:
                    zop_call_pv();
                    break;
                case 0x1a:
                    zop_call_pv();
                    break;
                case 0x1b:
                    zop_tokenise();
                    break;
                case 0x1c:
                    zop_encode_text();
                    break;
                case 0x1d:
                    zop_copy_table();
                    break;
                case 0x1e:
                    zop_print_table();
                    break;
                case 0x1f:
                    getBranch();
                    zop_check_arg_count();
                    break;
                default:
                    zui.fatal("Unknown VAR - probably a bug.");
                }
            }
            else if( curOpcodeType == OPTYPE_EXT )
            {
                // Extended instructions
                switch( curOpcode )
                {
                case 0x00:
                    getResult();
                    zop_ext_save();
                    break;
                case 0x01:
                    getResult();
                    zop_ext_restore();
                    break;
                case 0x02:
                    getResult();
                    zop_log_shift();
                    break;
                case 0x03:
                    getResult();
                    zop_art_shift();
                    break;
                case 0x04:
                    getResult();
                    zop_set_font();
                    break;
                case 0x05:
                    zop_draw_picture();
                    break;
                case 0x06:
                    getBranch();
                    zop_picture_data();
                    break;
                case 0x07:
                    zop_erase_picture();
                    break;
                case 0x08:
                    zop_set_margins();
                    break;
                case 0x09:
                    getResult();
                    zop_save_undo();
                    break;
                case 0x0a:
                    getResult();
                    zop_restore_undo();
                    break;
                case 0x0b:
                case 0x0c:
                case 0x0d:
                case 0x0e:
                case 0x0f:
                    zui.fatal("Unspecified EXT instruction: " + curOpcode);
                case 0x10:
                    zop_move_window();
                    break;
                case 0x11:
                    zop_window_size();
                    break;
                case 0x12:
                    zop_window_style();
                    break;
                case 0x13:
                    getResult();
                    zop_get_wind_prop();
                    break;
                case 0x14:
                    zop_scroll_window();
                    break;
                case 0x15:
                    zop_pop_stack();
                    break;
                case 0x16:
                    zop_read_mouse();
                    break;
                case 0x17:
                    zop_mouse_window();
                    break;
                case 0x18:
                    getBranch();
                    zop_push_stack();
                    break;
                case 0x19:
                    zop_put_wind_prop();
                    break;
                case 0x1a:
                    zop_print_form();
                    break;
                case 0x1b:
                    getBranch();
                    zop_make_menu();
                    break;
                case 0x1c:
                    zop_picture_table();
                    break;
                default:
                    zui.fatal("Unspecified EXT instruction: " + curOpcode);
                }
            }
            else
                zui.fatal("Unknown instruction: " + curInstruction);

            if( decode_ret_flag )
            {
                // An instruction has indicated that this decodeLoop
                // should return.
                decode_ret_flag = false;
                return;
            }

            if( restartFlag ) return; // Also return during a restart
        }
    }

    ///////////////////////////////////////////////////////////////////
    // Utility functions
    ///////////////////////////////////////////////////////////////////

    // This method gets the <branch> argument of the current
    // instruction. It stores the value of the argument in the
    // global variable curBranch, and sets curBranchReversed to
    // true if the logic of the branch is reversed. curCallFrame.pc
    // should be pointing at the argument when this is called;
    // it is adjusted accordingly.
    private void getBranch()
    {
        int b1, b2;
        int sval;

        // Get the first byte of the branch
        b1 = memory.fetchByte(curCallFrame.pc);
        curCallFrame.pc++;

        // Check to see if logic is reversed
        if( (b1 & 0x80) == 0x80 )
            curBranchReversed = false;
        else
            curBranchReversed = true;

        // If the branch is only one byte long, just set its
        // value and return.
        if( (b1 & 0x40) == 0x40 )
        {
            curBranch = (b1 & 0x3f);
            return;
        }

        // Otherwise, construct a signed branch value.
        b2 = memory.fetchByte(curCallFrame.pc);
        curCallFrame.pc++;
        sval = (((((b1 & 0x3f) << 8) & 0x3f00) | b2) & 0x3fff);
        // If the following makes no sense, see the Z-Machine spec
        // on signed numbers.
        if( (sval & 0x2000) == 0x2000 )
            curBranch = (sval - 16384);
        else
            curBranch = sval;
    }

    // Do a branch, based on the values of curBranch and
    // curBranchReversed.
    private void doBranch()
    {
        log.debug("doBranch()");

        if( curBranchReversed )
            return;
        else
        {
            switch( curBranch )
            {
            case 0:
                zop_rfalse();
                break;
            case 1:
                zop_rtrue();
                break;
            default:
                curCallFrame.pc = curCallFrame.pc + curBranch - 2;
                break;
            }
            return;
        }
    }

    // Don't do a branch, based on the values of curBranch and
    // curBranchReversed. (If curBranchReversed is true, this
    // implies a branch).
    private void dontBranch()
    {
        log.debug("dontBranch()");

        if( curBranchReversed )
        {
            switch( curBranch )
            {
            case 0:
                zop_rfalse();
                break;
            case 1:
                zop_rtrue();
                break;
            default:
                curCallFrame.pc = curCallFrame.pc + curBranch - 2;
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
    private void getResult()
    {
        curResult = memory.fetchByte(curCallFrame.pc);
        curCallFrame.pc++;
    }

    // This method gets the <string> argument of the current
    // instruction, decodes it, and stores it in the global variable
    // curString. curCallFrame.pc should be pointing at the start
    // of the string; it is adjusted accordingly.
    private void getString()
    {
        int w;

        // First, decode the string
        curString = decodeZString(curCallFrame.pc);

        // Now, adjust the PC.
        w = memory.fetchWord(curCallFrame.pc);
        curCallFrame.pc += 2;
        while( (w & 0x8000) == 0 )
        {
            w = memory.fetchWord(curCallFrame.pc);
            curCallFrame.pc += 2;
        }
    }

    // This function decodes the Z-String at the specified
    // address, and returns it as a Java String object.
    private String decodeZString(int addr)
    {
        StringBuffer decodedstr = new StringBuffer();
        int w, tmpaddr;
        int currentAlphabet, lockAlphabet;
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
            w = memory.fetchWord(tmpaddr);
            tmpaddr += 2;
            zlen += 3;
        }
        while( (w & 0x8000) != 0x8000 );
        // Then, allocate an array and put them in.
        zchars = new int[zlen];
        curindex = 0;
        tmpaddr = addr;
        w = memory.fetchWord(tmpaddr);
        tmpaddr += 2;
        zchars[curindex] = ((w >> 10) & 0x1f);
        zchars[curindex + 1] = ((w >> 5) & 0x1f);
        zchars[curindex + 2] = (w & 0x1f);
        curindex += 3;
        while( (w & 0x8000) == 0 )
        {
            w = memory.fetchWord(tmpaddr);
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
        currentAlphabet = alphabetL;
        lockAlphabet = alphabetL;
        for( int i = 0; i < zlen; i++ )
        {
            c = (char) zchars[i];
            // Decode character -- handle special characters as
            // necessary. A bit of code is repeated here for
            // the sake of cutting down on the number of comparisons.
            switch( c )
            {
            case 1:
                if( version == 1 )
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
                    abbrevAddr = memory.fetchWord(abbrevTable + (((((int) c) - 1) * 32 + ((int) c2)) * 2));
                    abbrevAddr *= 2; // Word address
                    decodedstr.append(decodeZString(abbrevAddr));
                }
                break;
            case 2:
                if( version <= 2 )
                { // Shift up
                    if( currentAlphabet == alphabetP )
                        currentAlphabet = alphabetL;
                    else
                        currentAlphabet++;
                }
                else
                { // An abbreviation
                    i++;
                    if( i >= zlen ) break;
                    c2 = (char) zchars[i];
                    abbrevAddr = memory.fetchWord(abbrevTable + (((((int) c) - 1) * 32 + ((int) c2)) * 2));
                    abbrevAddr *= 2; // Word address
                    decodedstr.append(decodeZString(abbrevAddr));
                }
                break;
            case 3:
                if( version <= 2 )
                { // Shift down
                    if( currentAlphabet == alphabetL )
                        currentAlphabet = alphabetP;
                    else if( currentAlphabet == alphabetP )
                        currentAlphabet = alphabetU;
                    else
                        currentAlphabet = alphabetL;
                }
                else
                { // Abbreviation
                    i++;
                    if( i >= zlen ) break;
                    c2 = (char) zchars[i];
                    abbrevAddr = memory.fetchWord(abbrevTable + (((((int) c) - 1) * 32 + ((int) c2)) * 2));
                    abbrevAddr *= 2; // Word address
                    decodedstr.append(decodeZString(abbrevAddr));
                }
                break;
            case 4: // Always a shift up
                if( currentAlphabet == alphabetP )
                    currentAlphabet = alphabetL;
                else
                    currentAlphabet++;
                if( version <= 2 ) lockAlphabet = currentAlphabet;
                break;
            case 5: // Always a shift down
                if( currentAlphabet == alphabetL )
                    currentAlphabet = alphabetP;
                else if( currentAlphabet == alphabetP )
                    currentAlphabet = alphabetU;
                else
                    currentAlphabet = alphabetL;
                if( version <= 2 ) lockAlphabet = currentAlphabet;
                break;
            case 6: // Literal output character if alphabet is P.
                if( currentAlphabet == alphabetP )
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
                    decodedstr.append(String.valueOf(alphabet[currentAlphabet][(int) c]));
                    currentAlphabet = lockAlphabet;
                }
                break;
            default:
                decodedstr.append(String.valueOf(alphabet[currentAlphabet][(int) c]));
                currentAlphabet = lockAlphabet;
                break;
            }
        }

        // We're done!
        return decodedstr.toString();
    }

    // Encode text into a Z-String, represented as a vector of Integers.
    private Vector<Integer> encodeZString(String text)
    {
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
            if( !altCharSet )
            {
                if( Character.isLowerCase(curchar) )
                { // Alphabet L
                    outbuf.addElement(new Integer(((int) ((curchar - 'a') + 6))));
                    continue;
                }
                else if( Character.isUpperCase(curchar) )
                {
                    if( version < 3 )
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
                if( version == 1 )
                { // Only needs one character in V1
                    outbuf.addElement(new Integer(1));
                    continue;
                }

                // Otherwise, two bytes are needed.
                if( version < 3 )
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
                if( alphabet[alphabetL][i] == curchar )
                {
                    outbuf.addElement(new Integer(i));
                    found = true;
                }
                else if( alphabet[alphabetU][i] == curchar )
                {
                    if( version < 3 )
                        outbuf.addElement(new Integer(2));
                    else
                        outbuf.addElement(new Integer(4));
                    outbuf.addElement(new Integer(i));
                    found = true;
                }
                else if( alphabet[alphabetP][i] == curchar )
                {
                    if( version < 3 )
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

            if( version < 3 ) // Shift to alphabetP
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

    // This function handles requests to get the value of a
    // variable. Variable 0 refers to the top of the routine
    // stack; variables 1-15 refer to local variables; and
    // variables 16-255 refer to global variables.
    private int getVariable(int v)
    {
        if( v == 0 )
        { // The top of the routine stack
            if( curCallFrame.routineStack.empty() )
                zui.fatal("Routine stack underflow");
            else
            {
                Integer i = (Integer) curCallFrame.routineStack.pop();
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

    // This function handles requests to put the value of a variable,
    // as above.
    private void putVariable(int v, int value)
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

    // Unpack a packed address. raddr is true if this is a routine
    // address.
    private int unpackAddr(int paddr, boolean raddr)
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

    // Return a signed version of a word
    private int signedWord(int w)
    {
        if( (w & 0x8000) == 0x8000 )
            return (w - 65536);
        else
            return (w);
    }

    // Encode a signed word
    private int unsignedWord(int w)
    {
        w = w & 0xffff;
        if( w < 0 )
            return (65536 - (-w));
        else
            return (w);
    }

    // Call the routine at the given routine address as an interrupt.
    // Return the return value of the routine.
    private int interrupt(int raddr)
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
        decodeLoop();

        // When we're done, ret_value will contain the routine's return value.
        return ret_value;
    }

    // Save the state of the Z-Machine--that is, the current call frame
    // and the call frame stack.
    private void dumpState(DataOutputStream dos) throws IOException
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

    // Read a game state from a file, as saved by dumpState().
    private void readState(DataInputStream dis) throws IOException
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
    private void dumpStack(DataOutputStream dos, Stack<Integer> st) throws IOException
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

    // Called by readState--reads a stack saved by dumpStack().
    private void readStack(DataInputStream dis, Stack<Integer> st) throws IOException
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

    ///////////////////////////////////////////////////////////////////
    // Instruction implementations
    //
    // All operands and arguments of an instruction have been parsed
    // before any of these methods are called. curCallFrame.pc
    // is pointing at the next instruction.
    //
    // For more information on these methods, see the Z-Machine spec.
    ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    // 0OPs
    ///////////////////////////////////////////////////////////////////

    // RTRUE
    private void zop_rtrue()
    {
        log.debug("RTRUE");
        // This is equivalent to RET 1
        op1 = 1;
        zop_ret();
    }

    // RFALSE
    private void zop_rfalse()
    {
        log.debug("RFALSE");
        // This is equivalent to RET 0
        op1 = 0;
        zop_ret();
    }

    // PRINT <string>
    private void zop_print()
    {
        log.debug("PRINT");

        // The spec says to output the string as if a sequence
        // of PRINT_CHAR instructions were executed. However,
        // we output an entire string at a time for maximum
        // drawing efficiency.
        ioCard.printString(curString);
    }

    // PRINT_RTRUE <string>
    private void zop_print_rtrue()
    {
        log.debug("PRINT_RTRUE");

        zop_print();
        zop_new_line();
        zop_rtrue();
    }

    // NOP
    private void zop_nop()
    {
        log.debug("NOP");

        // No op.
        return;
    }

    // SAVE <branch> V1-3
    // SAVE <result> V4+
    private void zop_save()
    {
        log.debug("SAVE");

        String fn;
        FileOutputStream fos;
        DataOutputStream dos;

        // Get a filename to save under
        fn = zui.getFilename("Save Game", null, true);
        if( fn == null )
        { // An error-probably user cancelled.
            if( version <= 3 )
                dontBranch();
            else
                putVariable(curResult, 0);
            return;
        }

        try
        {
            fos = new FileOutputStream(fn);
            dos = new DataOutputStream(fos);
            dumpState(dos);
            memory.dumpMemory(dos, 0, dynamicMemorySize);
            fos.close();
        }
        catch( IOException ex1 )
        {
            if( version <= 3 )
                dontBranch();
            else
                putVariable(curResult, 0);
            return;
        }

        // We did it!
        if( version <= 3 )
            doBranch();
        else
            putVariable(curResult, 1);
    }

    // RESTORE <branch> V1-3
    // RESTORE <result> V4
    private void zop_restore()
    {
        log.debug("RESTORE");

        String fn;
        FileInputStream fis;
        DataInputStream dis;
        int tsBit;

        // Get a filename to restore from
        fn = zui.getFilename("Restore Game", null, false);
        if( fn == null )
        { // An error-probably user cancelled.
            if( version >= 4 ) putVariable(curResult, 0);
            return;
        }

        // Remember the transcript bit
        tsBit = memory.fetchWord(0x10) & 0x0001;

        try
        {
            fis = new FileInputStream(fn);
            dis = new DataInputStream(fis);
            readState(dis);
            memory.readMemory(dis, 0, dynamicMemorySize);
            fis.close();
        }
        catch( IOException ex1 )
        {
            if( version >= 4 ) putVariable(curResult, 0);
            return;
        }

        // We did it!
        memory.putWord(0x10, memory.fetchWord(0x10) | tsBit);
        if( version >= 3 )
        {
            curResult = memory.fetchByte(curCallFrame.pc - 1);
            putVariable(curResult, 2); // Is this correct?
        }
    }

    // RESTART
    private void zop_restart()
    {
        log.debug("RESTART");

        // This will cause the decoder to exit and the ZMachine to restart
        zui.restart();
        restartFlag = true;
        return;
    }

    // RET_PULLED
    private void zop_ret_pulled()
    {
        log.debug("RET_PULLED");

        op1 = getVariable(0);
        zop_ret();
    }

    // POP
    private void zop_pop()
    {
        log.debug("POP");
        getVariable(0);
    }

    // CATCH <result> V5+
    private void zop_catch()
    {
        log.debug("CATCH");
        putVariable(curResult, curCallFrame.frameNumber);
    }

    // QUIT
    private void zop_quit()
    {
        log.debug("QUIT");
        zui.quit();
    }

    // NEW_LINE
    private void zop_new_line()
    {
        log.debug("NEW_LINE");
        did_newline = true;
        ioCard.printString("\n");
    }

    // SHOW_STATUS V3
    private void zop_show_status()
    {
        log.debug("SHOW_STATUS");
        boolean timegame;
        String s;
        int a, b, name;

        // This instruction is known to appear spuriously in some
        // V5 games (notably Wishbringer Solid Gold), so if this
        // storyfile is not V1-3, we'll ignore it.
        if( version > 3 ) return;

        // Find out if this is a time game or not. Can this change
        // during a game? I'm assuming it can.
        if( (memory.fetchByte(0x01) & 0x02) == 0x02 )
            timegame = true;
        else
            timegame = false;

        // Get the current location name
        name = objTable.getObjectName(getVariable(16));
        s = decodeZString(name);

        // Get the two integers
        a = signedWord(getVariable(17));
        b = signedWord(getVariable(18));

        // Pass it on to the user interface.
        zui.showStatusBar(s, a, b, timegame);
    }

    // VERIFY <branch>
    private void zop_verify()
    {
        log.debug("VERIFY");
        // VERIFY is always successful for now. I've had problems getting it
        // working,
        // and it's not a high priority.
        doBranch();
    }

    // PIRACY <branch> V5+
    private void zop_piracy()
    {
        log.debug("PIRACY");
        // This always branches.
        doBranch();
    }

    ///////////////////////////////////////////////////////////////////
    // 1OPs
    ///////////////////////////////////////////////////////////////////

    // JZ a <branch>
    private void zop_jz()
    {
        log.debug("JZ {}", String.format("0x%04X", op1));

        if( op1 == 0 )
            doBranch();
        else
            dontBranch();
    }

    // GET_SIBLING obj <result> <branch>
    private void zop_get_sibling()
    {
        log.debug("GET_SIBLING {} {}", String.format("0x%04X", op1), String.format("0x%04X", curResult));

        int sib;

        sib = objTable.getSibling(op1);
        putVariable(curResult, sib);
        if( sib != 0 )
            doBranch();
        else
            dontBranch();
    }

    // GET_CHILD obj <result> <branch>
    private void zop_get_child()
    {
        log.debug("GET_CHILD {} {}", String.format("0x%04X", op1), String.format("0x%04X", curResult));

        int child;

        child = objTable.getChild(op1);
        putVariable(curResult, child);
        if( child != 0 )
            doBranch();
        else
            dontBranch();
    }

    // GET_PARENT obj <result>
    private void zop_get_parent()
    {
        log.debug("GET_PARENT {} {}", String.format("0x%04X", op1), String.format("0x%04X", curResult));

        int parent;

        parent = objTable.getParent(op1);
        putVariable(curResult, parent);
    }

    // GET_PROP_LEN baddr <result>
    private void zop_get_prop_len()
    {
        log.debug("GET_PROP_LEN {} {}", String.format("0x%04X", op1), String.format("0x%04X", curResult));

        int len;

        len = objTable.getPropertyLength(op1);
        putVariable(curResult, len);
    }

    // INC var
    private void zop_inc()
    {
        log.debug("INC {}", String.format("0x%04X", op1));

        int w;

        w = signedWord(getVariable(op1));
        w = ((w + 1) % 0x10000);
        putVariable(op1, w);
    }

    // DEC var
    private void zop_dec()
    {
        log.debug("DEC {}", String.format("0x%04X", op1));

        int w;

        w = signedWord(getVariable(op1));
        w = ((w - 1) % 0x10000);
        putVariable(op1, w);
    }

    // PRINT_ADDR addr
    private void zop_print_addr()
    {
        log.debug("PRINT_ADDR {}", String.format("0x%04X", op1));

        String s;

        s = decodeZString(op1);
        log.info("print address: {} [{}]", op1, s);
        ioCard.printString(s);
    }

    // CALL_F0 raddr <result> V4+
    private void zop_call_f0()
    {
        log.debug("CALL_F0 {}", String.format("0x%04X", op1));

        numvops = 1;
        vops[0] = op1;
        zop_call_fv();
    }

    // REMOVE_OBJ obj
    private void zop_remove_obj()
    {
        log.debug("REMOVE_OBJ {}", String.format("0x%04X", op1));

        int parent;

        parent = objTable.getParent(op1);
        if( op1 == 0 ) return; // No parent, no service.
        objTable.removeObject(parent, op1);
    }

    // PRINT_OBJ obj
    private void zop_print_obj()
    {
        log.debug("PRINT_OBJ {}", String.format("0x%04X", op1));

        int addr;
        String s;

        addr = objTable.getObjectName(op1);
        s = decodeZString(addr);
        log.info("object: [{}]", s);
        ioCard.printString(s);
    }

    // RET a
    private void zop_ret()
    {
        log.debug("RET {}", String.format("0x%04X", op1));

        // First, make sure we *can* return.
        if( callStack.empty() ) zui.fatal("Call stack underflow");

        // Now do the appropriate thing for each call type.
        if( curCallFrame.callType == ZCallFrame.PROCEDURE )
        {
            curCallFrame = (ZCallFrame) callStack.pop();
            return;
        }
        else if( curCallFrame.callType == ZCallFrame.FUNCTION )
        {
            curCallFrame = (ZCallFrame) callStack.pop();
            curResult = memory.fetchByte(curCallFrame.pc);
            curCallFrame.pc++;
            putVariable(curResult, op1);
            return;
        }
        else if( curCallFrame.callType == ZCallFrame.INTERRUPT )
        {
            curCallFrame = (ZCallFrame) callStack.pop();
            decode_ret_flag = true;
            ret_value = op1;
            return;
        }

        // If we make it here, something is wrong.
        zui.fatal("Corrupted call frame");
        return;
    }

    // JUMP s
    private void zop_jump()
    {
        log.debug("JUMP {}", String.format("0x%04X", op1));

        int sop1;

        sop1 = signedWord(op1);

        curCallFrame.pc = curCallFrame.pc + sop1 - 2;
    }

    // PRINT_PADDR saddr
    private void zop_print_paddr()
    {
        log.debug("PRINT_PADDR {}", String.format("0x%04X", op1));

        int addr;
        String s;

        addr = unpackAddr(op1, false);
        s = decodeZString(addr);
        ioCard.printString(s);
    }

    // LOAD var <result>
    private void zop_load()
    {
        log.debug("LOAD {}", String.format("0x%04X", op1));

        int w;

        w = getVariable(op1);
        putVariable(curResult, w);
    }

    // NOT a <result>
    private void zop_not()
    {
        log.debug("NOT {}", String.format("0x%04X", op1));

        int val;

        if( op1type == ARGTYPE_WORD )
            val = ((~op1) & 0xffff);
        else
            val = ((~op1) & 0xff);
        putVariable(curResult, val);
    }

    // CALL_P0 raddr V5+
    private void zop_call_p0()
    {
        log.debug("CALL_P0 {}", String.format("0x%04X", op1));

        numvops = 1;
        vops[0] = op1;
        zop_call_pv();
    }

    ///////////////////////////////////////////////////////////////////
    // 2OPs
    ///////////////////////////////////////////////////////////////////

    // JE a [b1 b2 b3] <branch>
    private void zop_je()
    {
        log.debug("JE {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        if( curInstruction == 0xc1 )
        { // The variable version
            for( int i = 1; i < numvops; i++ )
            {
                if( vops[0] == vops[i] )
                {
                    doBranch();
                    return;
                }
            }
            // If we get here, there were no matches.
            dontBranch();
            return;
        }
        else
        { // The two-operand version
            if( op1 == op2 )
                doBranch();
            else
                dontBranch();
            return;
        }
    }

    // JL s t <branch>
    private void zop_jl()
    {
        log.debug("JL {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        if( sop1 < sop2 )
            doBranch();
        else
            dontBranch();
    }

    // JG s t <branch>
    private void zop_jg()
    {
        log.debug("JG {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        if( sop1 > sop2 )
            doBranch();
        else
            dontBranch();
    }

    // DEC_JL var s <branch>
    private void zop_dec_jl()
    {
        log.debug("DEC_JL {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        // DEC var
        zop_dec();

        // JL var s
        op1 = getVariable(op1);
        op1type = ARGTYPE_WORD;
        zop_jl();
    }

    // INC_JG var t <branch>()
    private void zop_inc_jg()
    {
        log.debug("INC_JG {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        // INC var
        zop_inc();

        // JG var t
        op1 = getVariable(op1);
        op1type = ARGTYPE_WORD;
        zop_jg();
    }

    // JIN obj n <branch>
    private void zop_jin()
    {
        log.debug("JIN {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int parent;

        parent = objTable.getParent(op1);

        if( parent == op2 )
            doBranch();
        else
            dontBranch();
    }

    // TEST a b <branch>
    private void zop_test()
    {
        log.debug("TEST {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        if( (op1 & op2) == op2 )
            doBranch();
        else
            dontBranch();
    }

    // OR a b <result>
    private void zop_or()
    {
        log.debug("OR {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        putVariable(curResult, (op1 | op2));
    }

    // AND a b <result>
    private void zop_and()
    {
        log.debug("AND {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        putVariable(curResult, (op1 & op2));
    }

    // TEST_ATTR obj attr <branch>
    private void zop_test_attr()
    {
        log.debug("TEST_ATTR {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        if( objTable.hasAttribute(op1, op2) )
            doBranch();
        else
            dontBranch();
    }

    // SET_ATTR obj attr
    private void zop_set_attr()
    {
        log.debug("SET_ATTR {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        objTable.setAttribute(op1, op2);
    }

    // CLEAR_ATTR obj attr
    private void zop_clear_attr()
    {
        log.debug("CLEAR_ATTR {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        objTable.clearAttribute(op1, op2);
    }

    // STORE var a
    private void zop_store()
    {
        log.debug("STORE {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        putVariable(op1, op2);
    }

    // INSERT_OBJ obj1 obj2
    private void zop_insert_obj()
    {
        log.debug("INSERT_OBJ {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        objTable.insertObject(op1, op2);
    }

    // LOADW baddr n <result>
    private void zop_loadw()
    {
        log.debug("LOADW {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        putVariable(curResult, memory.fetchWord(op1 + (2 * op2)));
    }

    // LOADB baddr n <result>
    private void zop_loadb()
    {
        log.debug("LOADB {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        putVariable(curResult, memory.fetchByte(op1 + op2));
    }

    // GET_PROP obj prop <result>
    private void zop_get_prop()
    {
        log.debug("GET_PROP {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int prop;

        prop = objTable.getProperty(op1, op2);
        putVariable(curResult, prop);
    }

    // GET_PROP_ADDR obj prop <result>
    private void zop_get_prop_addr()
    {
        log.debug("GET_PROP_ADDR {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int addr;

        addr = objTable.getPropertyAddress(op1, op2);
        putVariable(curResult, addr);
    }

    // GET_NEXT_PROP obj prop <result>
    private void zop_get_next_prop()
    {
        log.debug("GET_NEXT_PROP {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int num;

        num = objTable.getNextProperty(op1, op2);
        putVariable(curResult, num);
    }

    // ADD a b <result>
    private void zop_add()
    {
        log.debug("ADD {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        putVariable(curResult, unsignedWord(sop1 + sop2));
    }

    // SUB a b <result>
    private void zop_sub()
    {
        log.debug("SUB {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        putVariable(curResult, unsignedWord(sop1 - sop2));
    }

    // MUL a b <result>
    private void zop_mul()
    {
        log.debug("MUL {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        putVariable(curResult, unsignedWord(sop1 * sop2));
    }

    // DIV a b <result>
    private void zop_div()
    {
        log.debug("DIV {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        if( op2 == 0 ) zui.fatal("Divide by zero");

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        putVariable(curResult, unsignedWord(sop1 / sop2));
    }

    // MOD a b <result>
    private void zop_mod()
    {
        log.debug("MOD {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        int sop1, sop2;

        if( op2 == 0 )
        {
            putVariable(curResult, op1);
            return;
        }

        sop1 = signedWord(op1);

        sop2 = signedWord(op2);

        putVariable(curResult, unsignedWord(sop1 % sop2));
    }

    // CALL_F1 raddr a1 <result> V4+
    private void zop_call_f1()
    {
        log.debug("CALL_F1 {} {}", String.format("0x%04X", op1), String.format("0x%04X", op2));

        numvops = 2;
        vops[0] = op1;
        vops[1] = op2;
        zop_call_fv();
    }

    // CALL_P1 raddr a1 V5+
    private void zop_call_p1()
    {
        numvops = 2;
        vops[0] = op1;
        vops[1] = op2;
        zop_call_pv();
    }

    // SET_COLOUR f b V5+
    private void zop_set_colour()
    {
        if( op1 == 1 ) op1 = memory.fetchByte(0x2d);
        if( op2 == 1 ) op2 = memory.fetchByte(0x2c);
        zui.setColor(op1, op2);
    }

    // THROW a fp V5+
    private void zop_throw()
    {
        // Pop the stack until we either find the frame being referenced, or the
        // stack underflows (a fatal error).
        while( (curCallFrame.frameNumber != op2) && (!callStack.empty()) )
            curCallFrame = (ZCallFrame) callStack.pop();
        if( curCallFrame.frameNumber != op2 ) // Stack underflow
            zui.fatal("THROW: Call stack underflow");

        // We have the frame; now do a RET a
        zop_ret();
    }

    ///////////////////////////////////////////////////////////////////
    // VARs
    ///////////////////////////////////////////////////////////////////

    // CALL_FV raddr [a1 a2 a3] <result>
    // CALL_FD raddr [a1 a2 a3 a4 a5 a6 a7] <result> V4+
    private void zop_call_fv()
    {
        log.debug("CALL_FV {} ", toString(vops));

        int addr;
        int numvars;
        int numargs;
        int newFrameNumber;

        // First, make sure raddr is not 0
        if( vops[0] == 0 )
        {
            putVariable(curResult, 0);
            return;
        }

        // Get the number of arguments
        numargs = numvops - 1;

        // Unpack the routine address
        addr = unpackAddr(vops[0], true);

        // Get the number of local variables
        numvars = memory.fetchByte(addr);

        // Bump the address past the variables byte, in any version
        addr++;

        // Back up the PC to point to the result byte
        curCallFrame.pc--;

        // Get the number of the next call frame.
        newFrameNumber = curCallFrame.frameNumber + 1;

        // Push the current call frame onto the stack.
        callStack.push(curCallFrame);

        // Initialize a new call frame
        curCallFrame = new ZCallFrame();

        // Put the PC at the appropriate place, depending on
        // whether local variables are present.
        if( version < 5 )
            curCallFrame.pc = addr + (numvars * 2);
        else
            curCallFrame.pc = addr;

        // Create an empty routine stack
        curCallFrame.routineStack = new Stack<Integer>();

        // Initialize local variables
        curCallFrame.numLocalVars = numvars;
        for( int i = 0; i < numvars; i++ )
        {
            // Fill in an argument in this variable, if one exists.
            if( i < numargs )
            {
                curCallFrame.localVars[i] = vops[i + 1];
                continue;
            }

            // Otherwise, if this is a pre-V5 game, fill in
            // a local variable.
            if( version < 5 )
            {
                curCallFrame.localVars[i] = memory.fetchWord(addr + (i * 2));
                continue;
            }

            // Otherwise, just make this variable 0.
            curCallFrame.localVars[i] = 0;
        }

        // Store the call type (only strictly necessary in V3+)
        curCallFrame.callType = ZCallFrame.FUNCTION;

        // Store the number of arguments (only strictly necessary
        // in V5+)
        if( numargs > numvars )
            curCallFrame.argCount = numvars;
        else
            curCallFrame.argCount = numargs;

        // Stor the call frame number
        curCallFrame.frameNumber = newFrameNumber;
    }

    // STOREW baddr n a
    private void zop_storew()
    {
        log.debug("STOREW {} ", toString(vops));

        memory.putWord((vops[0] + (2 * vops[1])), vops[2]);
    }

    // STOREB baddr n byte
    private void zop_storeb()
    {
        log.debug("STOREB {} ", toString(vops));

        memory.putByte((vops[0] + vops[1]), vops[2]);
    }

    // PUT_PROP obj prop a
    private void zop_put_prop()
    {
        log.debug("PUT_PROP {} ", toString(vops));

        objTable.putProperty(vops[0], vops[1], vops[2]);
    }

    // READ baddr1 baddr2 V1-3
    // READ baddr1 baddr2 [time raddr] V4
    // READ baddr1 baddr2 [time raddr] <result> V5+
    private void zop_read()
    {
        log.debug("READ {} ", toString(vops));

        String s;
        StringBuffer sb;
        int termChar;
        int len;
        int curaddr;
        int baddr1, baddr2;
        int time = 0, raddr = 0;

        baddr1 = vops[0];
        baddr2 = vops[1];
        if( numvops > 2 )
        {
            time = vops[2];
            raddr = vops[3];
        }

        // Flush the I/O card's output buffer
        ioCard.outputFlush();

        // This implies a SHOW_STATUS in V1-3.
        if( version < 4 ) zop_show_status();

        // Read a line of text
        sb = new StringBuffer();
        if( (time > 0) && (raddr > 0) )
        { // A timed READ
            while( true )
            { // Ick.
                termChar = ioCard.readLine(sb, time);
                if( termChar == -1 )
                { // A timeout
                  // ioCard.outputFlush();
                  // did_newline = false;
                    for( int i = 0; i < sb.length(); i++ )
                        ioCard.printString("\b");
                    int rc = interrupt(raddr);
                    if( rc == 0 )
                    {
                        // if (did_newline) {
                        // ioCard.printString("\n" + sb.toString());
                        // ioCard.outputFlush();
                        // }
                        ioCard.printString(sb.toString());
                        ioCard.outputFlush();
                        continue;
                    }
                    else
                    {
                        ioCard.outputFlush();
                        sb = new StringBuffer();
                        termChar = 0;
                        break;
                    }
                }
                else // Not a timeout
                    break;
            }
        }
        else
            termChar = ioCard.readLine(sb, 0);
        log.info("input: {} {}", sb.toString(), sb.toString().getBytes());
        s = sb.toString();

        // If V1-4, just store the line. If V5+, possibly
        // store it after other characters in the buffer.
        if( version <= 4 )
        {
            curaddr = baddr1 + 1;
            len = s.length();
            for( int i = 0; i < len; i++ )
            {
                memory.putByte(curaddr, Character.toLowerCase(s.charAt(i)));
                curaddr++;
            }
            memory.putByte(curaddr, 0);
        }
        else
        {
            int nchars = memory.fetchByte(baddr1 + 1);
            curaddr = baddr1 + 2 + nchars;
            len = s.length();
            for( int i = 0; i < len; i++ )
            {
                memory.putByte(curaddr, Character.toLowerCase(s.charAt(i)));
                curaddr++;
            }
            memory.putByte(baddr1 + 1, (nchars + len));
        }

        // Tokenize input
        if( baddr2 != 0 )
        {
            vops[0] = baddr1;
            vops[1] = baddr2;
            numvops = 2;
            zop_tokenise();
        }

        // If V5+, store result
        if( version >= 5 ) putVariable(curResult, termChar);
    }

    // PRINT_CHAR n
    private void zop_print_char()
    {
        log.debug("PRINT_CHAR {} ", toString(vops));

        String s;

        s = new String(String.valueOf((char) vops[0]));
        log.info("print char: [{}]", s);
        ioCard.printString(s);
    }

    // PRINT_NUM s
    private void zop_print_num()
    {
        log.debug("PRINT_NUM {} ", toString(vops));

        int sop1;
        String s;

        sop1 = signedWord(vops[0]);

        s = new String(String.valueOf(sop1));
        ioCard.printString(s);
    }

    // RANDOM s <result>
    private void zop_random()
    {
        log.debug("RANDOM {} ", toString(vops));

        if( signedWord(vops[0]) > 0 )
            putVariable(curResult, rndgen.getRandom(signedWord(vops[0])));
        else
        {
            rndgen.seed(signedWord(vops[0]));
            putVariable(curResult, 0);
        }
    }

    // PUSH a
    private void zop_push()
    {
        log.debug("PUSH {} ", toString(vops));
        
        putVariable(0, vops[0]);
    }

    // PULL var V1-5,7-8
    // PULL [baddr] <result> V6
    private void zop_pull()
    {
        // This will need to be extended for V6 support
        putVariable(vops[0], getVariable(0));
    }

    // SPLIT_SCREEN n V3+
    private void zop_split_screen()
    {
        ioCard.outputFlush();
        zui.splitScreen(vops[0]);
    }

    // SET_WINDOW window V3+
    private void zop_set_window()
    {
        ioCard.outputFlush();

        // In V6, -3 represents the current window
        zui.setCurrentWindow(vops[0]);
    }

    // CALL_FD raddr [a1 a2 a3 a4 a5 a6 a7] <result> V4+
    private void zop_call_fd()
    {
        // CALL_FV actually handles this
        zop_call_fv();
    }

    // ERASE_WINDOW window V4+
    private void zop_erase_window()
    {
        int sop1;

        sop1 = signedWord(vops[0]);
        if( sop1 == -1 )
        { // Erase everything, do a SPLIT_SCREEN 0
            zui.eraseWindow(0);
            zui.eraseWindow(1);
            vops[0] = 0;
            numvops = 1;
            zop_split_screen();
            return;
        }
        else // In V6, we'll have to handle -2 explicitly
            zui.eraseWindow(vops[0]);
    }

    // ERASE_LINE V4-5,7-8
    // ERASE_LINE n V6
    private void zop_erase_line()
    {
        zui.eraseLine(1);
    }

    // SET_CURSOR s x V4-5,7-8
    // SET_CURSOR s x [window] V6
    private void zop_set_cursor()
    {
        ioCard.outputFlush();
        zui.setCursorPosition(vops[1], vops[0]);
    }

    // GET_CURSOR baddr V4+
    private void zop_get_cursor()
    {
        Point p;

        ioCard.outputFlush();
        p = zui.getCursorPosition();
        memory.putWord(vops[0], p.y);
        memory.putWord(vops[0] + 2, p.x);
    }

    // SET_TEXT_STYLE n V4+
    private void zop_set_text_style()
    {
        ioCard.outputFlush();
        zui.setTextStyle(vops[0]);
        Dimension s = zui.getFontSize();
        memory.putByte(0x26, s.height);
        memory.putByte(0x27, s.width);
    }

    // BUFFER_MODE bit V4+
    private void zop_buffer_mode()
    {
        // This doesn't really fit in with our buffering method, so we ignore
        // it.
        // Flush the buffer, though.
        ioCard.outputFlush();
    }

    // OUTPUT_STREAM s V3-4
    // OUTPUT_STREAM s [baddr] V5,7-8
    // OUTPUT_STREAM s [baddr w] V6
    private void zop_output_stream()
    {
        // int w;

        if( numvops == 3 )
            ioCard.setOutputStream(signedWord(vops[0]), vops[1], vops[2], true);
        else
            ioCard.setOutputStream(signedWord(vops[0]), vops[1], 0, false);
    }

    // INPUT_STREAM n V3+
    private void zop_input_stream()
    {
        ioCard.setInputStream(vops[0]);
    }

    // SOUND n [op time raddr] V3+
    private void zop_sound()
    {
        // Silently fail on this instruction if no raddr argument;
        // otherwise, go straight to raddr for now.
        if( numvops == 1 ) return;

        if( vops[1] != 2 ) return;

        // Pretend a CALL_P0 has just been executed.
        op1 = vops[3];
        op1type = voptypes[3];
        zop_call_p0();
    }

    // READ_CHAR 1 [time raddr] <result> V4+
    private void zop_read_char()
    {
        int c;

        ioCard.outputFlush();
        if( (numvops > 1) && (vops[1] != 0) && (vops[2] != 0) )
        { // A timed READ_CHAR
            while( true )
            { // Yuck.
                c = ioCard.readChar(vops[1]);
                if( c == -1 )
                { // A timeout
                    int rc = interrupt(vops[2]);
                    if( rc == 0 )
                        continue;
                    else
                    {
                        putVariable(curResult, 0);
                        return;
                    }
                }
                else
                { // A character
                    putVariable(curResult, c);
                    return;
                }
            }
        }
        else
        {
            c = ioCard.readChar(0);
            putVariable(curResult, c);
        }
    }

    // SCAN_TABLE a baddr n [byte] <result> <branch> V4+
    private void zop_scan_table()
    {
        int a, baddr, n, format;
        boolean searchWord; // Searching for a word (not a byte)?
        int tableWidth; // Width of table
        int testAddr; // Address to test
        int testData; // Data to test

        // Get operands
        a = vops[0];
        baddr = vops[1];
        n = vops[2];
        if( numvops == 4 )
            format = vops[3];
        else
            format = 0x82;
        if( (format & 0x80) == 0x80 )
            searchWord = true;
        else
            searchWord = false;
        tableWidth = (format & 0x7f);

        // Fail if it's a table of bytes and a is word-valued
        if( (tableWidth == 2) && (voptypes[0] == ARGTYPE_BYTE) )
        {
            putVariable(curResult, 0);
            dontBranch();
            return;
        }

        // Search the table
        for( int i = 0; i < n; i++ )
        {
            testAddr = (baddr + (i * tableWidth));
            if( searchWord )
                testData = memory.fetchWord(testAddr);
            else
                testData = memory.fetchByte(testAddr);
            if( testData == a )
            { // A match!
                putVariable(curResult, (baddr + (i * tableWidth)));
                doBranch();
                return;
            }
        }

        // If we get here, there was no match, or n < 1
        putVariable(curResult, 0);
        dontBranch();
        return;
    }

    // CALL_PV raddr [a1 a2 a3] V5+
    // CALL_PD raddr [a1 a2 a3 a4 a5 a6 a7] V5+
    private void zop_call_pv()
    {
        int addr;
        int numvars;
        int numargs;
        int newFrameNumber;

        // First, make sure raddr is not 0
        if( vops[0] == 0 )
        {
            putVariable(curResult, 0);
            return;
        }

        // Get the number of arguments
        numargs = numvops - 1;

        // Unpack the routine address
        addr = unpackAddr(vops[0], true);

        log.debug(Integer.toHexString(curCallFrame.pc) + ": CALL " + Integer.toHexString(addr) + " " + vops[1] + " " + vops[2] + " " + vops[3]);

        // Get the number of local variables
        numvars = memory.fetchByte(addr);

        // Bump the address past the variables byte, in any version
        addr++;

        // Get the number of the next call frame.
        newFrameNumber = curCallFrame.frameNumber + 1;

        // Push the current call frame onto the stack.
        callStack.push(curCallFrame);

        // Initialize a new call frame
        curCallFrame = new ZCallFrame();

        // Put the PC at the appropriate place, depending on
        // whether local variables are present.
        if( version < 5 )
            curCallFrame.pc = addr + (numvars * 2);
        else
            curCallFrame.pc = addr;

        // Create an empty routine stack
        curCallFrame.routineStack = new Stack<Integer>();

        // Initialize local variables
        curCallFrame.numLocalVars = numvars;
        for( int i = 0; i < numvars; i++ )
        {
            // Fill in an argument in this variable, if one exists.
            if( i < numargs )
            {
                curCallFrame.localVars[i] = vops[i + 1];
                continue;
            }

            // Otherwise, if this is a pre-V5 game, fill in
            // a local variable.
            if( version < 5 )
            {
                curCallFrame.localVars[i] = memory.fetchWord(addr + (i * 2));
                continue;
            }

            // Otherwise, just make this variable 0.
            curCallFrame.localVars[i] = 0;
        }

        // Store the call type (only strictly necessary in V3+)
        curCallFrame.callType = ZCallFrame.PROCEDURE;

        // Store the number of arguments (only strictly necessary
        // in V5+)
        if( numargs > numvars )
            curCallFrame.argCount = numvars;
        else
            curCallFrame.argCount = numargs;

        // Store the call frame number
        curCallFrame.frameNumber = newFrameNumber;
    }

    // TOKENISE baddr1 baddr2 [baddr3 bit] V5+
    // This code could definitely be improved.
    private void zop_tokenise()
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

        if( numvops > 2 )
        {
            dictaddr = vops[2];
            bit = vops[3];
        }
        else
        {
            dictaddr = mainDictionary;
            bit = 0;
        }

        // Get the maximum number of tokens
        maxtokens = memory.fetchByte(vops[1]);

        // Set maximum token length, in words
        if( version < 4 )
            maxtokenlen = 2;
        else
            maxtokenlen = 3;

        // Construct a Java string from the input string.
        s = new String();
        if( version <= 4 )
        { // Null-terminated input string
            curaddr = vops[0] + 1;
            c = memory.fetchByte(curaddr);
            while( c != 0 )
            {
                s = s + String.valueOf((char) c);
                curaddr++;
                c = memory.fetchByte(curaddr);
            }

            log.info("tokenize: [{}]", s);
        }
        else
        { // String with length value
            len = memory.fetchByte(vops[0] + 1);
            curaddr = vops[0] + 2;
            for( i = 0; i < len; i++ )
            {
                s = s + String.valueOf((char) memory.fetchByte(curaddr));
                curaddr++;
            }
        }

        // Create a string containing separators.
        delimiters = new String(" "); // Space is always a delimiter.
        numseparators = memory.fetchByte(dictaddr);
        for( i = 1; i <= numseparators; i++ )
            delimiters = delimiters + String.valueOf((char) memory.fetchByte(dictaddr + i));

        // Get the number and length of dictionary entries.
        curaddr = dictaddr + 1 + numseparators;
        dictentrysize = memory.fetchByte(curaddr);

        log.info("number of entries in dictionary: {}", dictentrysize);

        curaddr++;
        numdictentries = signedWord(memory.fetchWord(curaddr));
        curaddr += 2;

        if( numdictentries < 0 )
            // We don't care whether the entries are sorted.
            numdictentries = Math.abs(numdictentries);

        // Parse through the input string. I think the Java StringTokenizer
        // class is neat.
        tokens = new StringTokenizer(s, delimiters, false);
        numtokens = 0;
        strpos = 0; // Since the StringTokenizer isn't discarding any
                    // characters, we use
                    // strpos to keep track of our position within the string,
                    // so we can
                    // store the location of each token within the input string.

        while( tokens.hasMoreTokens() && (numtokens < maxtokens) )
        {
            thistoken = tokens.nextToken();

            log.info("token: [{}]", thistoken);

            if( thistoken.equals(" ") )
            { // Ignore spaces
                strpos++;
                continue;
            }

            encodedtoken = encodeZString(thistoken);

            log.info("encode: [{}] -> [{}]", thistoken, encodeZString(thistoken));

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
                    curword = memory.fetchWord(curaddr + (i * dictentrysize) + (j * 2));
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
                log.info("known token: [{}]", thistoken);

                // Memory location of dictionary entry
                memory.putWord((vops[1] + 2 + (numtokens * 4)), (curaddr + (i * dictentrysize)));
                // Length of word
                memory.putByte((vops[1] + 2 + (numtokens * 4) + 2), thistoken.length());
                // Position in input buffer; see above
                memory.putByte((vops[1] + 2 + (numtokens * 4) + 3), (strpos + 2));
            }
            else if( bit == 0 )
            {
                log.info("unknown token: [{}]", thistoken);

                // If bit is set, leave the slot alone
                memory.putWord((vops[1] + 2 + (numtokens * 4)), 0);
                // Length of word
                memory.putByte((vops[1] + 2 + (numtokens * 4) + 2), thistoken.length());
                // Position in input buffer; see above
                memory.putByte((vops[1] + 2 + (numtokens * 4) + 3), (strpos + 2));
            }

            strpos += thistoken.length();
            numtokens++;
        }

        // Finally, store the number of tokens tokenized in the parse buffer.
        memory.putByte((vops[1] + 1), numtokens);
    }

    // ENCODE_TEXT baddr1 p n baddr2 V5+
    private void zop_encode_text()
    {
        String s;
        Vector<Integer> encodedstr;
        int curindex, maxlen, encodedlen;
        int i, w;
        Integer n;

        // First, make a string out of the text to encode.
        s = new String();
        for( i = 0; i < vops[2]; i++ )
            s = s + String.valueOf((char) memory.fetchByte(vops[0] + vops[1] + i));

        // Encode it.
        encodedstr = encodeZString(s);

        // Now copy to memory, storing Z-characters appropriately and respecting
        // length limits.
        if( version < 4 )
            maxlen = 2;
        else
            maxlen = 3;

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
            memory.putWord((vops[2] + (i * 2)), w);
        }
    }

    // COPY_TABLE baddr1 baddr2 s V5+
    private void zop_copy_table()
    {
        int i;
        int s;

        s = signedWord(vops[2]);

        // If baddr2 is 0, zero the data at baddr1.
        if( vops[1] == 0 )
        {
            for( i = 0; i < s; i++ )
                memory.putByte(vops[0] + i, 0);
            return;
        }

        // If s < 0, copy forwards
        if( s < 0 )
        {
            for( i = 0; i < (-s); i++ )
                memory.putByte(vops[1] + i, memory.fetchByte(vops[0] + i));
            return;
        }

        // Otherwise, copy backwards
        for( i = (s - 1); i >= 0; i-- )
            memory.putByte(vops[1] + i, memory.fetchByte(vops[0] + i));
        return;
    }

    // PRINT_TABLE baddr x [y n] V5+
    private void zop_print_table()
    {
        int baddr, x, y, n;
        int lineAddr;
        int c;

        // Get operands
        baddr = vops[0];
        x = vops[1];
      
        if( numvops == 4 )
        {
            y = vops[2];
            n = vops[3];
        }
        else
        {
            y = 1;
            n = 0;
        }

        // If y == 0, forget it (recommended by Klooster)
        if( y == 0 ) return;

        // Print the table
        lineAddr = baddr;
        
        for( int i = 0; i < y; i++ )
        {
            for( int j = 0; j < x; j++ )
            {
                c = memory.fetchByte(lineAddr + j);
                ioCard.printString(String.valueOf((char) c));
            }
            
            lineAddr += x + n;
        }

        // Done!
        return;
    }

    // CHECK_ARG_COUNT n <branch> V5+
    private void zop_check_arg_count()
    {
        if( curCallFrame.argCount >= vops[0] )
            doBranch();
        else
            dontBranch();
    }

    ///////////////////////////////////////////////////////////////////
    // EXTs
    ///////////////////////////////////////////////////////////////////

    // SAVE [baddr1 n baddr2] <result> V5+
    private void zop_ext_save()
    {
        String fn;
        String suggested;
        FileOutputStream fos;
        DataOutputStream dos;
        int slen;

        // If there are no arguments, do a normal save
        if( numvops == 0 )
        {
            zop_save();
            return;
        }

        // Get a filename to save under
        suggested = null;
        if( (numvops > 2) && (vops[2] != 0) )
        {
            slen = memory.fetchByte(vops[2]);
            if( slen > 0 )
            {
                StringBuffer tmp = new StringBuffer();
                for( int i = 1; i <= slen; i++ )
                    tmp.append(String.valueOf((char) memory.fetchByte(vops[2] + i)));
                suggested = tmp.toString();
            }
        }
        fn = zui.getFilename("Save Auxiliary File", suggested, true);
        if( fn == null )
        { // An error-probably user cancelled.
            putVariable(curResult, 0);
            return;
        }

        try
        {
            fos = new FileOutputStream(fn);
            dos = new DataOutputStream(fos);
            memory.dumpMemory(dos, vops[0], vops[1]);
            fos.close();
        }
        catch( IOException ex1 )
        {
            putVariable(curResult, 0);
            return;
        }

        // We did it!
        putVariable(curResult, 1);
    }

    // RESTORE [baddr1 n baddr2] <result> V5+
    private void zop_ext_restore()
    {
        String fn;
        String suggested;
        FileInputStream fis;
        DataInputStream dis;
        int slen;

        // If there are no arguments, do a normal restore
        if( numvops == 0 )
        {
            zop_restore();
            return;
        }

        // Get a filename to save under
        suggested = null;
        if( (numvops > 2) && (vops[2] != 0) )
        {
            slen = memory.fetchByte(vops[2]);
            if( slen > 0 )
            {
                StringBuffer tmp = new StringBuffer();
                for( int i = 1; i <= slen; i++ )
                    tmp.append(String.valueOf((char) memory.fetchByte(vops[2] + i)));
                suggested = tmp.toString();
            }
        }
        fn = zui.getFilename("Load Auxiliary File", suggested, false);
        if( fn == null )
        { // An error-probably user cancelled.
            putVariable(curResult, 0);
            return;
        }

        try
        {
            fis = new FileInputStream(fn);
            dis = new DataInputStream(fis);
            memory.readMemory(dis, vops[0], vops[1]);
            fis.close();
        }
        catch( IOException ex1 )
        {
            putVariable(curResult, 0);
            return;
        }

        // We did it!
        if( version >= 3 )
        {
            putVariable(curResult, vops[1]);
        }
    }

    // LOG_SHIFT a s <result> V5+
    private void zop_log_shift()
    {
        int val;
        int sop2;

        sop2 = signedWord(vops[1]);

        if( sop2 < 0 )
            val = vops[0] >>> Math.abs(sop2);
        else
            val = vops[0] << sop2;

        putVariable(curResult, val);
    }

    // ART_SHIFT a s <result> V5+
    private void zop_art_shift()
    {
        int val;
        int sop2;

        sop2 = signedWord(vops[1]);

        if( sop2 < 0 )
        {
            if( ((voptypes[0] == ARGTYPE_WORD) && ((vops[0] & 0x8000) == 0x8000)) || ((voptypes[0] == ARGTYPE_BYTE) && ((vops[0] & 0x80) == 0x80)) )
                val = vops[0] >> Math.abs(sop2);
            else
                val = vops[0] >>> Math.abs(sop2);
        }
        else
            val = vops[0] << sop2;

        putVariable(curResult, val);
    }

    // SET_FONT n <result> V5,7-8
    // SET_FONT n [window] <result> V6
    private void zop_set_font()
    {
        zui.setFont(vops[0]);
        Dimension s = zui.getFontSize();
        memory.putByte(0x26, s.height);
        memory.putByte(0x27, s.width);
    }

    // DRAW_PICTURE pic [y x] V6
    private void zop_draw_picture()
    {
        zui.fatal("DRAW_PICTURE instruction unimplemented");
    }

    // PICTURE_DATA pic baddr <branch> V6
    private void zop_picture_data()
    {
        zui.fatal("PICTURE_DATA instruction unimplemented");
    }

    // ERASE_PICTURE pic [y x] V6
    private void zop_erase_picture()
    {
        zui.fatal("ERASE_PICTURE instruction unimplemented");
    }

    // SET_MARGINS xl xr window V6
    private void zop_set_margins()
    {
        zui.fatal("SET_MARGINS instruction unimplemented");
    }

    // SAVE_UNDO <result> V5+
    private void zop_save_undo()
    {
        ByteArrayOutputStream bos;
        DataOutputStream dos;

        try
        {
            bos = new ByteArrayOutputStream(65536);
            dos = new DataOutputStream(bos);
            dumpState(dos);
            memory.dumpMemory(dos, 0, dynamicMemorySize);
            undoState = bos.toByteArray();
        }
        catch( IOException ioex )
        {
            zui.fatal("I/O exception during SAVE_UNDO??");
        }

        // We did it!
        if( version <= 3 )
            doBranch();
        else
            putVariable(curResult, 1);
    }

    // RESTORE_UNDO <result> V5+
    private void zop_restore_undo()
    {
        ByteArrayInputStream bis;
        DataInputStream dis;
        int tsBit;

        // Remember the transcript bit
        tsBit = memory.fetchWord(0x10) & 0x0001;

        try
        {
            bis = new ByteArrayInputStream(undoState);
            dis = new DataInputStream(bis);
            readState(dis);
            memory.readMemory(dis, 0, dynamicMemorySize);
        }
        catch( IOException ex1 )
        {
            zui.fatal("I/O Exception during RESTORE_UNDO???");
        }

        // We did it!
        memory.putWord(0x10, memory.fetchWord(0x10) | tsBit);
        if( version >= 3 )
        {
            curResult = memory.fetchByte(curCallFrame.pc - 1);
            putVariable(curResult, 2); // Is this correct?
        }
    }

    // MOVE_WINDOW window y x V6
    private void zop_move_window()
    {
        zui.fatal("MOVE_WINDOW instruction unimplemented");
    }

    // WINDOW_SIZE window y x V6
    private void zop_window_size()
    {
        zui.fatal("WINDOW_SIZE instruction unimplemented");
    }

    // WINDOW_STYLE window flags op V6
    private void zop_window_style()
    {
        zui.fatal("WINDOW_STYLE instruction unimplemented");
    }

    // GET_WIND_PROP window p <result> V6
    private void zop_get_wind_prop()
    {
        zui.fatal("GET_WINDOW_PROP instruction unimplemented");
    }

    // SCROLL_WINDOW window s V6
    private void zop_scroll_window()
    {
        zui.fatal("SCROLL_WINDOW instruction unimplemented");
    }

    // POP_STACK n [baddr] V6
    private void zop_pop_stack()
    {
        zui.fatal("POP_STACK instruction unimplemented");
    }

    // READ_MOUSE baddr V6
    private void zop_read_mouse()
    {
        zui.fatal("READ_MOUSE instruction unimplemented");
    }

    // MOUSE_WINDOW window V6
    private void zop_mouse_window()
    {
        zui.fatal("MOUSE_WINDOW instruction unimplemented");
    }

    // PUSH_STACK a baddr <branch> V6
    private void zop_push_stack()
    {
        zui.fatal("PUSH_STACK instruction unimplemented");
    }

    // PUT_WIND_PROP window p a V6
    private void zop_put_wind_prop()
    {
        zui.fatal("PUT_WIND_PROP instruction unimplemented");
    }

    // PRINT_FORM baddr V6
    private void zop_print_form()
    {
        zui.fatal("PRINT_FORM instruction unimplemented");
    }

    // MAKE_MENU n baddr <branch> V6
    private void zop_make_menu()
    {
        zui.fatal("MAKE_MENU instruction unimplemented");
    }

    // PICTURE_TABLE baddr V6
    private void zop_picture_table()
    {
        zui.fatal("PICTURE_TABLE instruction unimplemented");
    }

    private static Logger log = LoggerFactory.getLogger(ZCPU.class);

    private String opcodeTypeToString(int opcodeType)
    {
        StringWriter sw = new StringWriter();

        switch( opcodeType )
        {
        case OPTYPE_0OP:
            sw.append("OPTYPE_0OP");
            break;
        case OPTYPE_1OP:
            sw.append("OPTYPE_1OP");
            break;
        case OPTYPE_2OP:
            sw.append("OPTYPE_2OP");
            break;
        case OPTYPE_VAR:
            sw.append("OPTYPE_VAR");
            break;
        case OPTYPE_EXT:
            sw.append("OPTYPE_EXT");
            break;
        }

        return sw.toString();
    }

    private String toString(int[] list)
    {
        StringBuilder sb = new StringBuilder();

        for( int i : list )
        {
            if( sb.length() > 0 )
            {
                sb.append(" ");
            }

            sb.append(String.format("0x%04X", i));
        }

        return sb.toString();
    }
}
