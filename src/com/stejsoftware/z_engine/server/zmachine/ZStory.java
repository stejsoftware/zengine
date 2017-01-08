/**
 * Copyright (c) 2011 Jonathan J. Meyer <jon@stej.com>
 */
package com.stejsoftware.z_engine.server.zmachine;

import java.io.Serializable;
import java.util.List;

/**
 * @author jon
 * 
 */
public class ZStory implements Serializable
{
    private int    m_length     = 0;
    private byte[] m_data       = new byte[m_length];
    private String m_zcode_file = "-";
    private String m_id         = "-";
    private String m_name       = "Unnamed Story";

    public ZStory()
    {
    }

    public void zcode_file(String filename)
    {
        m_zcode_file = filename;
    }

    public String zcode_file()
    {
        return m_zcode_file;
    }

    public void id(String id)
    {
        m_id = id;
    }

    public String id()
    {
        return m_id;
    }

    public void name(String name)
    {
        m_name = name;
    }

    public String name()
    {
        return m_name;
    }

    public int size()
    {
        return m_data.length;
    }

    public void get_data(byte[] data)
    {
        if( (data != null) && (data.length >= m_data.length) )
        {
            int x;

            // copy data
            for( x = 0; x < m_data.length; x++ )
            {
                data[x] = m_data[x];
            }

            // finish the passed in buffer with zeros
            for( ; x < data.length; x++ )
            {
                data[x] = 0;
            }
        }
    }

    public void set_data(byte[] data)
    {
        if( data != null )
        {
            m_length = data.length;
            m_data = new byte[data.length];

            for( int x = 0; x < data.length; x++ )
            {
                m_data[x] = data[x];
            }
        }
    }

    public void get_data(List<Byte> data)
    {
        if( data != null )
        {
            data.clear();

            for( int x = 0; x < m_data.length; x++ )
            {
                data.set(x, new Byte(m_data[x]));
            }
        }
    }

    public void set_data(List<Byte> data)
    {
        if( data != null )
        {
            m_data = new byte[data.size()];

            for( int x = 0; x < m_data.length; x++ )
            {
                m_data[x] = data.get(x).byteValue();
            }
        }
    }

    private static final long serialVersionUID = 8387906854282175558L;
}
