/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         REMOVE_OBJ obj V1+
 */
public class REMOVE_OBJ extends ZInstructionBase
{
    public REMOVE_OBJ()
    {
        m_code = 0x09;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        int parent = p.objTable.getParent(p.op1);
        
        if( p.op1 == 0 )
        {
            return; // No parent, no service.
        }
        
        p.objTable.removeObject(parent, p.op1);
    }
}
