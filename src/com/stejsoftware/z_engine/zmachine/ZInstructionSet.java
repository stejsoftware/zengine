/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;
import com.stejsoftware.z_engine.zmachine.zop.ZOP;

/**
 * @author jon
 * 
 */
public class ZInstructionSet
{
    private static Logger          log      = LoggerFactory.getLogger(ZCPU.class);
    private static ZInstructionSet instance = null;

    public static ZInstructionSet GetInstance()
    {
        if( instance == null )
        {
            instance = new ZInstructionSet();
        }

        return instance;
    }

    // map of instructions
    private Map<ZOpCode, ZInstruction> instructions = new HashMap<ZOpCode, ZInstruction>();

    private ZInstructionSet()
    {
        // add all known instructions to the set.

        add(new ADD());
        add(new AND());
        add(new ART_SHIFT());
        add(new BUFFER_MODE());
        add(new CALL_F0());
        add(new CALL_F1());
        add(new CALL_FD());
        add(new CALL_FV());
        add(new CALL_P0());
        add(new CALL_P1());
        add(new CALL_PD());
        add(new CALL_PV());
        add(new CATCH());
        add(new CHECK_ARG_COUNT());
        add(new CLEAR_ATTR());
        add(new COPY_TABLE());
        add(new DEC());
        add(new DEC_CHK());
        add(new DEC_JL());
        add(new DIV());
        add(new DRAW_PICTURE());
        add(new ENCODE_TEXT());
        add(new ERASE_LINE());
        add(new ERASE_PICTURE());
        add(new ERASE_WINDOW());
        add(new EXT_RESTORE());
        add(new EXT_SAVE());
        add(new GET_CHILD());
        add(new GET_CURSOR());
        add(new GET_NEXT_PROP());
        add(new GET_PARENT());
        add(new GET_PROP());
        add(new GET_PROP_ADDR());
        add(new GET_PROP_LEN());
        add(new GET_SIBLING());
        add(new GET_WIND_PROP());
        add(new INC());
        add(new INC_CHK());
        add(new INC_JG());
        add(new INPUT_STREAM());
        add(new INSERT_OBJ());
        add(new JE());
        add(new JG());
        add(new JIN());
        add(new JL());
        add(new JUMP());
        add(new JZ());
        add(new LOAD());
        add(new LOADB());
        add(new LOADW());
        add(new LOG_SHIFT());
        add(new MAKE_MENU());
        add(new MOD());
        add(new MOUSE_WINDOW());
        add(new MOVE_WINDOW());
        add(new MUL());
        add(new NEW_LINE());
        add(new NOP());
        add(new NOT());
        add(new NOTV());
        add(new OR());
        add(new OUTPUT_STREAM());
        add(new PICTURE_DATA());
        add(new PICTURE_TABLE());
        add(new PIRACY());
        add(new POP_STACK());
        add(new PRINT());
        add(new PRINT_ADDR());
        add(new PRINT_CHAR());
        add(new PRINT_FORM());
        add(new PRINT_NUM());
        add(new PRINT_OBJ());
        add(new PRINT_PADDR());
        add(new PRINT_RTRUE());
        add(new PRINT_TABLE());
        add(new PULL());
        add(new PUSH());
        add(new PUSH_STACK());
        add(new PUT_PROP());
        add(new PUT_WIND_PROP());
        add(new QUIT());
        add(new RANDOM());
        add(new READ());
        add(new READ_CHAR());
        add(new READ_MOUSE());
        add(new REMOVE_OBJ());
        add(new RESTART());
        add(new RESTORE());
        add(new RESTORE_UNDO());
        add(new RET());
        add(new RET_PULLED());
        add(new RFALSE());
        add(new RTRUE());
        add(new SAVE());
        add(new SAVE_UNDO());
        add(new SCAN_TABLE());
        add(new SCROLL_WINDOW());
        add(new SET_ATTR());
        add(new SET_COLOUR());
        add(new SET_CURSOR());
        add(new SET_FONT());
        add(new SET_MARGINS());
        add(new SET_TEXT_STYLE());
        add(new SET_WINDOW());
        add(new SHOW_STATUS());
        add(new SOUND());
        add(new SPLIT_SCREEN());
        add(new STORE());
        add(new STOREB());
        add(new STOREW());
        add(new SUB());
        add(new TEST());
        add(new TEST_ATTR());
        add(new THROW());
        add(new TOKENIZE());
        add(new VERIFY());
        add(new WINDOW_SIZE());
        add(new WINDOW_STYLE());
        add(new ZOP());
    }

    private void add(ZInstruction instruction)
    {
        if( instructions.containsKey(instruction.opCode()) )
        {
            System.out.println("OpCode:" + instruction.opCode() + " already used.\n");
        }

        instructions.put(instruction.opCode(), instruction);
    }

    public void call(ZCPU cpu, OPCODE_TYPE op_type, int op_code)
    {
        log.info("call[Type: {}, Code: {}]", op_type, op_code);

        ZInstruction instruction = instructions.get(new ZOpCode(op_type, op_code));

        if( instruction != null )
        {
            instruction.call(cpu);
        }
        else
        {
            cpu.zui.fatal("Unknown instruction: " + op_type + ":" + op_code);
        }
    }
}
