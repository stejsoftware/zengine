package com.stejsoftware.z_engine.shared.msg;

public class Download extends Message
{
    private static final long serialVersionUID = -5368650142032687052L;
    private String            m_url;

    protected Download()
    {
    }

    public Download(String url)
    {
        m_url = url;
    }

    public String url()
    {
        return m_url;
    }

    public String get_html()
    {
        return String.format("<a href=\"%s\"></a>", m_url);
    }

    public String toString()
    {
        return "Download";
    }
}
