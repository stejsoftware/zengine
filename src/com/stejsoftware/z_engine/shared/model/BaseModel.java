/**
 * 
 */
package com.stejsoftware.z_engine.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jon
 *
 */

public class BaseModel implements Serializable
{

    Map<String, String> map = new HashMap<String, String>();

    protected void set(String key, String value)
    {
        map.put(key, value);
    }

    protected void set(String key, byte[] value)
    {
        map.put(key, value.toString());
    }

    protected String get(String key, String value)
    {
        String v;

        if( map.containsKey(key) )
        {
            v = map.get(key);
        }
        else
        {
            v = value;
        }

        return v;
    }

    protected byte[] get(String key)
    {
        return new byte[0];
    }

    private static final long serialVersionUID = -3297867173842587108L;
}
