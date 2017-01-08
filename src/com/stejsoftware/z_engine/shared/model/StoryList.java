/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author jon
 * 
 */
public class StoryList extends ArrayList<Story>
{
    public StoryList()
    {
    }

    /**
     * @param values
     */
    public StoryList(Collection<Story> values)
    {
        for( Iterator<Story> ptr = values.iterator(); ptr.hasNext(); )
        {
            super.add(ptr.next());
        }
    }

    @Override
    public String toString()
    {
        return "StoryList";
    }

    public Story find(String id)
    {
        Story story = null;

        for( int x = 0; (story == null && x < size()); x++ )
        {
            if( get(x).id().equals(id) )
            {
                story = get(x);
            }
        }

        return story;
    }

    private static final long serialVersionUID = -2828197336418717951L;
}
