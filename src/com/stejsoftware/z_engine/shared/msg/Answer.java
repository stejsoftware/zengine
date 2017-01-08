/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class Answer extends Message
{
    private static final long serialVersionUID = -3239746138734688179L;
    private String            m_answer         = null;

    protected Answer()
    {
    }

    public Answer(String answer)
    {
        m_answer = answer;
    }

    public String get()
    {
        if( m_answer == null )
        {
            return "";
        }

        return m_answer;
    }

    public String toString()
    {
        return "Answer";
    }
}
