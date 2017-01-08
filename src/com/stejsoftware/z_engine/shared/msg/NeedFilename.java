/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class NeedFilename extends Message
{
    private static final long serialVersionUID = 3348193728924441689L;
    private String            m_name           = "save.dat";

    public void set_name(String name)
    {
        m_name = name;
    }

    public String get_name()
    {
        return m_name;
    }

    public String toString()
    {
        return "Need_Filename";
    }
}
