package com.stejsoftware.z_engine.shared.model;

public class Save extends BaseModel
{
    public static final String GAME_ID = "game";
    public static final String DATA    = "data";
    public static final String LABEL   = "label";

    Save()
    {
    }

    public Save(String label, String game_id)
    {
        set(LABEL, label);
        set(GAME_ID, game_id);
    }

    public String label()
    {
        return get(LABEL, "Saved Game");
    }

    public String game()
    {
        return get(GAME_ID, "");
    }

    public void data(byte[] data)
    {
        set(DATA, data);
    }

    public byte[] data()
    {
        return get(DATA);
    }

    private static final long serialVersionUID = 8588303284215870299L;
}
