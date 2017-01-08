/**
 * 
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class Erase extends Message
{
    private enum WHAT
    {
        DONTKNOW,
        WINDOW,
        LINE
    }

    private WHAT m_what  = WHAT.DONTKNOW;
    private int  m_value = 0;

    private Erase(WHAT what, int value)
    {
        m_what = what;
        m_value = value;
    }

    static public Erase Line(int line)
    {
        return new Erase(WHAT.LINE, line);
    }

    static public Erase Window(int window)
    {
        return new Erase(WHAT.WINDOW, window);
    }

    public boolean isLine()
    {
        return m_what == WHAT.LINE;
    }

    public boolean isWindow()
    {
        return m_what == WHAT.WINDOW;
    }

    public int getValue()
    {
        return m_value;
    }

    private static final long serialVersionUID = 4272375408721793661L;
}
