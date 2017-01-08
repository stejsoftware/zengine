/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         PRINT_OBJ obj V1+
 */
public class PRINT_OBJ extends ZInstructionBase
{
    public PRINT_OBJ()
    {
        m_code = 0x0a;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        p.ioCard.printString(decodeZString(p, p.objTable.getObjectName(p.op1)));
    }
}
