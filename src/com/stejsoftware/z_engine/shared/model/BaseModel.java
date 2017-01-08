/**
 * 
 */
package com.stejsoftware.z_engine.shared.model;

import java.io.Serializable;

/**
 * @author jon
 *
 */

public class BaseModel implements Serializable
{
    private static final long serialVersionUID = -3297867173842587108L;

    Object                    value            = null;

    protected void set(String key, Object value)
    {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(String key, T value)
    {
        T v;

        if( null != this.value )
        {
            try
            {
                v = (T) this.value;
            }
            catch( Exception ex )
            {
                v = value;
            }
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
}
