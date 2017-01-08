/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.sync;

/**
 * @author jon
 * 
 */
public class SyncString
{
  private String string = "";

  public SyncString()
  {
  }

  public SyncString( String inital_value )
  {
    this.string = inital_value;
  }

  /**
   * get with no wait
   * 
   * @return
   */
  public synchronized String get()
  {
    return this.string;
  }

  /**
   * get with wait timeout. Use a timeout of 0 to wait forever.
   * 
   * @param timeout
   * @return
   */
  public synchronized String get( int timeout )
  {
    try
    {
      wait(timeout);
    }
    catch( InterruptedException booga )
    {
    }

    return string;
  }

  public synchronized void set( String string )
  {
    this.string = string;
    notify();
  }
}
