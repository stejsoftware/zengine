/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author jon
 * 
 */
public class Question extends Message
{
    private static final long serialVersionUID = 4614948106553354567L;
    private String            m_question       = null;
    private List<Answer>      m_answers        = new ArrayList<Answer>();

    protected Question()
    {
    }

    public Question(String question)
    {
        m_question = question;
    }

    public String get()
    {
        if( m_question == null )
        {
            return "";
        }

        return m_question;
    }

    public void add(Answer answer)
    {
        m_answers.add(answer);
    }

    public Iterator<Answer> iterator()
    {
        return m_answers.iterator();
    }

    public String toString()
    {
        return "Question";
    }
}
