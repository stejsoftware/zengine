/**
 * 
 */
package com.stejsoftware.z_engine.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stejsoftware.z_engine.StoryDatabase;
import com.stejsoftware.z_engine.ZEngine;
import com.stejsoftware.z_engine.zmachine.ZStory;

/**
 *  
 *
 */
public class Test
{

    private static Logger log = LoggerFactory.getLogger(Test.class);

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        ZEngine engine = new ZEngine();
        StoryDatabase story_db = new StoryDatabase();
        // SaveDatabase save_db = new SaveDatabase();

        if( story_db.load_stories("C:\\Projects\\z_engine\\archive") )
        {
            log.info("Stories: {}", story_db.get_story_list());

            ZStory story = story_db.get_story("MD5:6b5f6592c99a7ae83156241989be2972");

            log.info("Got story: {}", story);

            engine.StartStory(story);
            log.info("Story Started");
           
           // Text from_client = new Text("Hello");
           // List<Message> to_client = engine.ProcessMessage(from_client);

           // for( Message msg : to_client )
           // {
           //     log.info("To Client: {}", msg);
           // }
        }
    }
}
