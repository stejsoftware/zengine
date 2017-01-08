/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.shared.msg;

import com.stejsoftware.z_engine.shared.ErrorCode;

/**
 * @author jon
 * 
 */
public class Log extends Message
{
    private static final long serialVersionUID = -6633989121034465668L;
    private long              code             = ErrorCode.UNKNOWN;
    private String            message          = "";

    public static Log         SUCCESS          = new Log(ErrorCode.SUCCESS, "No Error");

    protected Log()
    {
    }

    public Log(String message)
    {
        this(ErrorCode.SYS_MSG, message);
    }

    public Log(long code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public long get_code()
    {
        return this.code;
    }

    public String get_message()
    {
        return this.message;
    }

    public String get_text()
    {
        return "System: " + message;
    }

    public String toString()
    {
        return "Status_Msg";
    }

    public boolean isError()
    {
        return code != ErrorCode.SUCCESS;
    }
}
