/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class NeedLine extends Message
{
    private static final long serialVersionUID = 8792367868433242918L;
    private int               m_timeout        = 0;

    protected NeedLine()
    {
    }

    public NeedLine(int timeout)
    {
        m_timeout = timeout;
    }

    public String toString()
    {
        return "Need_Line";
    }

    public int timeout()
    {
        return m_timeout;
    }
}
