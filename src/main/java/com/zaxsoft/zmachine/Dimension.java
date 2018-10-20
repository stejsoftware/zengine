package com.zaxsoft.zmachine;

import java.io.Serializable;

public class Dimension implements Serializable
{
    private static final long serialVersionUID = -996638044677487206L;

    Dimension()
    {
    }

    public int width  = 0;
    public int height = 0;

    public Dimension(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
}
