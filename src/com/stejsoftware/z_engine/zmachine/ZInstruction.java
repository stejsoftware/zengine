/**
 * 
 */
package com.stejsoftware.z_engine.zmachine;

import com.stejsoftware.z_engine.zmachine.ZOpCode;

/**
 * @author jon
 * 
 */
public interface ZInstruction
{
    public void call(ZCPU cpu);

    public ZOpCode opCode();
}
