/**
 * 
 */
package com.stejsoftware.z_engine;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jon
 * 
 */
public class FileDatabase
{
    private static Logger log   = LoggerFactory.getLogger(FileDatabase.class);

    private File[]        files = new File[0];

    public File[] load(String folder, final String ext)
    {
        if( files.length == 0 )
        {
            File path = new File(folder);

            log.info("Loading files from: " + path.getAbsolutePath());

            if( path.isDirectory() )
            {
                files = path.listFiles(new FilenameFilter()
                {
                    public boolean accept(File dir, String name)
                    {
                        return (ext.length() == 0) || name.endsWith(ext);
                    }
                });

                log.info("Found " + Integer.toString(files.length) + " file" + (files.length != 1 ? "s." : "."));
            }
            else
            {
                log.warn("Counldn't find folder: " + path.getAbsoluteFile());
            }
        }

        return files;
    }

    boolean is_empty()
    {
        return files.length == 0;
    }
}
