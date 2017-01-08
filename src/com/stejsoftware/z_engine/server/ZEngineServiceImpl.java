/**
 * 
 */
package com.stejsoftware.z_engine.server;

import java.util.List;
import java.util.logging.Logger;

import com.stejsoftware.z_engine.server.zmachine.ZStory;
import com.stejsoftware.z_engine.shared.model.Save;
import com.stejsoftware.z_engine.shared.model.Story;
import com.stejsoftware.z_engine.shared.msg.Message;

/**
 * @author "Jonathan Meyer" <insixdays@gmail.com>
 */
public class ZEngineServiceImpl
{
    private final Logger  log      = Logger.getLogger(ZEngineServiceImpl.class.getName());
    private ZEngineServer server   = new ZEngineServer();
    private StoryDatabase story_db = new StoryDatabase();
    private SaveDatabase  save_db  = new SaveDatabase();

    public ZEngineServiceImpl()
    {
    }

    public Story get_story(String id)
    {
        log.info("get_story(" + id + ")");

        Story story = new Story();
        ZStory zstory = story_db.get_story(id);

        if( zstory != null )
        {
            story.name(zstory.name());
            story.id(zstory.id());
        }

        return story;
    }

    public Save get_game(String id)
    {
        // TODO Auto-generated method stub
        return new Save("", "");
    }

    public Boolean add_story(Story story)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Boolean save_game(Save game)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean run_story(String story_id)
    {
        ZStory story = story_db.get_story(story_id);

        if( story == null )
        {
            return false;
        }

        return server.StartStory(story);
    }

    public List<Message> send(Message to_server)
    {
        return server.ProcessMessage(to_server);
    }

    // private static final long serialVersionUID = -2530425027026070951L;
}
