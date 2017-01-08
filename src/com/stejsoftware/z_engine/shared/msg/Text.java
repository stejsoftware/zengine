/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class Text extends Message
{
    private String m_text       = null;
    private int    m_forground  = 0;
    private int    m_background = 0;
    private int    m_style      = 0;
    private int    m_font       = 0;

    protected Text()
    {
    }

    /**
     * 
     * @param text
     */
    public Text(String text)
    {
        this(text, 0, 0, 0, 0);
    }

    /**
     * @param s
     * @param forground
     * @param background
     * @param style
     * @param font
     */
    public Text(String text, int forground, int background, int style, int font)
    {
        m_text = text;
        m_forground = forground;
        m_background = background;
        m_style = style;
        m_font = font;
    }

    public boolean is_empty()
    {
        return (m_text == null) || (m_text.length() == 0);
    }

    public String get()
    {
        if( m_text == null )
        {
            return "";
        }

        return m_text;
    }

    public String get_text()
    {
        return get().replace("\n>", "\n");
    }

    @Override
    public String toString()
    {
        return "Text";
    }

    private static final long serialVersionUID = 145797990130287754L;
}
