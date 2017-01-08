/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode.OPCODE_TYPE;

/**
 * @author jon
 * 
 *         GET_PROP_LEN baddr <result>
 */
public class GET_PROP_LEN extends ZInstructionBase
{
    public GET_PROP_LEN()
    {
        m_code = 0x04;
        m_type = OPCODE_TYPE.OP1;
    }

    @Override
    public void call(ZCPU p)
    {
        getResult(p);

        int len = p.objTable.getPropertyLength(p.op1);
        p.putVariable(p.curResult, len);
    }}
