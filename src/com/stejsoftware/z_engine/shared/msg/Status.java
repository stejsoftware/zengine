/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

/**
 * @author jon
 * 
 */
public class Status extends Message
{
    private static final long serialVersionUID = 7244180335830148189L;
    private String            location         = "";
    private int               score            = 0;
    private int               turns            = 0;

    protected Status()
    {
    }

    public Status(String location, int score, int turns)
    {
        this.location = location;
        this.score = score;
        this.turns = turns;
    }

    public String get_location()
    {
        return this.location;
    }

    public int get_score()
    {
        return this.score;
    }

    public int get_turns()
    {
        return this.turns;
    }

    @Override
    public String toString()
    {
        return "Status";
    }
}
