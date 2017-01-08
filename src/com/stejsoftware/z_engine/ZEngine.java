/**
 * 
 */
package com.stejsoftware.z_engine;

import java.util.List;

import com.stejsoftware.z_engine.shared.msg.Message;
import com.stejsoftware.z_engine.zmachine.ZCPU;
import com.stejsoftware.z_engine.zmachine.ZStory;

/**
 * @author jon
 * 
 */
public class ZEngine
{
    private ZEngineInterface zui = new ZEngineInterface();
    private ZCPU             cpu = new ZCPU(zui);

    /**
     * 
     * @param story_id
     * @return
     */
    public boolean StartStory(ZStory story)
    {
        if( story != null )
        {
            cpu.Load(story);
            cpu.Start();
        }

        return cpu.isPaused();
    }

    /**
     * 
     * @param from_client
     * @return
     */
    public List<Message> ProcessMessage(Message from_client)
    {
        zui.AddMessage(from_client);
        cpu.Run();
        return zui.GetMessages();
    }
}
