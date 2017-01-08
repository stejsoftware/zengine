/**
 * 
 */
package com.stejsoftware.z_engine.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mortbay.jetty.security.Credential.MD5;

import com.stejsoftware.z_engine.shared.model.Story;

/**
 * @author jon
 * 
 */
public class SaveDatabase extends FileDatabase
{
    private final Logger        log       = Logger.getLogger(ZEngineServiceImpl.class.getName());

    // key: story id; value: a story obj
    private Map<String, Story>  saves_map = new HashMap<String, Story>();

    // key: story id; value: full path to story
    private Map<String, String> file_map  = new HashMap<String, String>();

    public void load_saves(String saves_folder)
    {
        File[] files = super.load(saves_folder, ".dat");

        log.log(Level.INFO, "load_saves");

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
