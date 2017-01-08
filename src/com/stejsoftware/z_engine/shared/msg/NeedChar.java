/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class NeedChar extends Message
{
    private static final long serialVersionUID = -1125805799033051965L;
    private int               m_timeout        = 0;

    protected NeedChar()
    {
    }

    public NeedChar(int timeout)
    {
        m_timeout = timeout;
    }

    public String toString()
    {
        return "Need_Char";
    }

    public int timeout()
    {
        return m_timeout;
    }
}
