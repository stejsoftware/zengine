/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         INSERT_OBJ obj1 obj2
 */
public class INSERT_OBJ extends ZInstructionBase
{
    public INSERT_OBJ()
    {
        m_code = 0x0e;
        m_type = OPCODE_TYPE.OP2;
    }

    @Override
    public void call(ZCPU p)
    {
        p.objTable.insertObject(p.op1, p.op2);
    }
}
