package com.stejsoftware.z_engine.shared.model;

public class Story extends BaseModel
{
    public static final String STORY_ID = "story_id";
    public static final String NAME     = "name";
    public static final String DESC     = "desc";
    public static final String AUTHOR   = "author";
    public static final String VERSION  = "version";

    public Story()
    {
    }

    public void name(String name)
    {
        set(NAME, name);
    }

    public void id(String id)
    {
        set(STORY_ID, id);
    }

    public String name()
    {
        return get(NAME, "-");
    }

    public String id()
    {
        return get(STORY_ID, "-");
    }

    private static final long serialVersionUID = 8588303284215870299L;
}
