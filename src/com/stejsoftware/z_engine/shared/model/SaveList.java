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
public class SaveList extends ArrayList<Save>
{
    public SaveList()
    {
    }

    /**
     * @param values
     */
    public SaveList(Collection<Save> values)
    {
        for( Iterator<Save> ptr = values.iterator(); ptr.hasNext(); )
        {
            super.add(ptr.next());
        }
    }

    @Override
    public String toString()
    {
        return "SaveList";
    }

    public Save find(String game)
    {
        Save story = null;

        for( int x = 0; (story == null && x < size()); x++ )
        {
            if( get(x).game().equals(game) )
            {
                story = get(x);
            }
        }

        return story;
    }

    private static final long serialVersionUID = 5429239043733875472L;
}
