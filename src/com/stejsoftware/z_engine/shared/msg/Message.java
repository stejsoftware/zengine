/**
 * 
 */
package com.stejsoftware.z_engine.shared.msg;

import java.io.Serializable;

/**
 * @author jon
 * 
 */
public abstract class Message implements Serializable
{
    private static final long serialVersionUID = 202607834735172356L;
    private String            m_game_id        = "-";

    public void game_id(String game_id)
    {
        m_game_id = game_id;
    }

    public String game_id()
    {
        return m_game_id;
    }

    public String toString()
    {
        return "Message";
    }
}
