/**
 * 
 */
package com.stejsoftware.z_engine.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.mortbay.jetty.security.Credential.MD5;

import com.stejsoftware.z_engine.server.zmachine.ZStory;
import com.stejsoftware.z_engine.shared.model.Story;
import com.stejsoftware.z_engine.shared.model.StoryList;

/**
 * @author jon
 * 
 */
public class StoryDatabase extends FileDatabase
{
    private final Logger        log        = Logger.getLogger(ZEngineServiceImpl.class.getName());
    
    // key: story id; value: a story obj
    private Map<String, Story>  story_map  = new HashMap<String, Story>();
    
    // key: story id; value: full path to story
    private Map<String, String> file_map   = new HashMap<String, String>();
     
    // key: story id; value: a zstory obj
    private Map<String, ZStory> zstory_map = new HashMap<String, ZStory>();

    public void load_stories(String story_folder)
    {
        File[] files = super.load(story_folder, ".z3");

        for( int x = 0; x < files.length; x++ )
        {
            String id = MD5.digest(files[x].getAbsolutePath());
            Story story = new Story();

            story.name(files[x].getName());
            story.id(id);

            file_map.put(id, files[x].getAbsolutePath());
            story_map.put(id, story);
        }
    }

    @Override
    boolean is_empty()
    {
        return file_map.size() == 0;
    }

    public StoryList get_story_list()
    {
        return new StoryList(story_map.values());
    }

    /**
     * @param id
     * @return
     */
    public ZStory get_story(String id)
    {
        log.info("Getting z-story: " + id);
        ZStory zstory = zstory_map.get(id);

        if( zstory == null )
        {
            log.info("Getting story: " + id);
            Story story = story_map.get(id);

            if( story != null )
            {
                zstory = new ZStory();

                zstory.name(story.name());
                zstory.id(id);
                zstory.set_data(load_story(file_map.get(id)));

                zstory_map.put(id, zstory);
            }
        }

        return zstory;
    }

    /**
     * 
     * @param filename
     * @return
     */
    private byte[] load_story(String filename)
    {
        log.info("Loading story file: " + filename);
        byte[] data = null;
        File f = new File(filename);

        if( f.canRead() && f.isFile() )
        {
            int dataLength = (int) f.length();
            data = new byte[dataLength];

            try
            {
                FileInputStream fis = new FileInputStream(f);
                DataInputStream dis = new DataInputStream(fis);

                dis.readFully(data, 0, dataLength);

                fis.close();
            }
            catch( IOException ex )
            {
                log.warning("Error: " + filename + ": " + ex.getMessage());
            }

        }
        else
        {
            log.warning("Error: file not readable: " + filename);
        }

        log.info("*** Done loading story file: " + filename);
        return data;
    }

}
