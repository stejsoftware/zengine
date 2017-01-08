/**
 * 
 */
package com.stejsoftware.z_engine.server;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Logger;

/**
 * @author jon
 * 
 */
public class FileDatabase
{
    private final Logger log   = Logger.getLogger(ZEngineServiceImpl.class.getName());
    private File[]       files = new File[0];

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
                log.warning("Counldn't find folder: " + path.getAbsoluteFile());
            }
        }

        return files;
    }

    boolean is_empty()
    {
        return files.length == 0;
    }
}
