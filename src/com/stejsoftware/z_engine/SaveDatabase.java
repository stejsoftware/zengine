/**
 * 
 */
package com.stejsoftware.z_engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.mortbay.jetty.security.Credential.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stejsoftware.z_engine.shared.model.Story;

/**
 * @author jon
 * 
 */
public class SaveDatabase extends FileDatabase
{
    private static Logger       log       = LoggerFactory.getLogger(SaveDatabase.class);

    // key: story id; value: a story obj
    private Map<String, Story>  saves_map = new HashMap<String, Story>();

    // key: story id; value: full path to story
    private Map<String, String> file_map  = new HashMap<String, String>();

    public void load_saves(String saves_folder)
    {
        File[] files = super.load(saves_folder, ".dat");

        log.info("load_saves");

        for( int x = 0; x < files.length; x++ )
        {
            String id = MD5.digest(files[x].getAbsolutePath());
            Story story = new Story();

            story.name(files[x].getName());
            story.id(id);

            file_map.put(id, files[x].getAbsolutePath());
            saves_map.put(id, story);
        }
    }
}
