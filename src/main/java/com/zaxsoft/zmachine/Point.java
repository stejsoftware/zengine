package com.zaxsoft.zmachine;

import java.io.Serializable;

public class Point implements Serializable
{
    private static final long serialVersionUID = -9092942223928149029L;

    Point()
    {
    }

    public int y = 0;
    public int x = 0;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
