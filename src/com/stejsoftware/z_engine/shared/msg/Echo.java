package com.stejsoftware.z_engine.shared.msg;

import java.util.Date;

public class Echo extends Message
{
    private static final long serialVersionUID = 377875225947081959L;
    private String            m_msg            = "";

    public Echo()
    {
        m_msg = "Echo: " + new Date().toString();
    }

    public Echo(String message)
    {
        m_msg = message;
    }

    public String message()
    {
        return m_msg;
    }

    public String toString()
    {
        return "Echo";
    }
}
