/**
 * 
 */
package com.stejsoftware.z_engine.server;

import java.util.List;

import com.stejsoftware.z_engine.server.zmachine.ZCPU;
import com.stejsoftware.z_engine.server.zmachine.ZStory;
import com.stejsoftware.z_engine.shared.msg.Message;

/**
 * @author jon
 * 
 */
public class ZEngineServer
{
    private ZEngineServerInterface zui = new ZEngineServerInterface();
    private ZCPU                   cpu = new ZCPU(zui);

    /**
     * 
     * @param story_id
     * @return
     */
    public boolean StartStory(ZStory story)
    {
        cpu.Load(story);
        cpu.Start();
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
