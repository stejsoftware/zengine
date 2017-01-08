package com.stejsoftware.z_engine;

import javax.servlet.http.HttpSession;

public class SessionVar<T>
{
    private HttpSession session       = null;
    private String      name          = null;
    private T           default_value = null;

    public SessionVar(HttpSession session, String name, T default_value)
    {
        this.session = session;
        this.name = name;
        this.default_value = default_value;
    }

    public SessionVar(HttpSession session, String name)
    {
        this.session = session;
        this.name = name;
    }

    /**
     * get the current value of the variable.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get()
    {
        try
        {
            return (T) session.getAttribute(name);
        }
        catch( Exception ex )
        {
            return default_value;
        }
    }

    public void set(T value)
    {
        if( value == null )
        {
            session.removeAttribute(name);
        }
        else
        {
            session.setAttribute(name, value);
        }
    }
}
