/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

import com.stejsoftware.z_engine.server.zmachine.ZOpCode;

/**
 * @author jon
 * 
 */
public interface ZInstruction
{
    public void call(ZCPU cpu);

    public ZOpCode opCode();
}
