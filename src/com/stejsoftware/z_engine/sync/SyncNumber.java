/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.sync;

/**
 * @author jon
 * 
 */
public class SyncNumber
{
  private int number = 0;

  public SyncNumber()
  {
  }

  public SyncNumber( int inital_value )
  {
    number = inital_value;
  }

  /**
   * get with no wait
   * 
   * @return
   */
  public synchronized int get()
  {
    return number;
  }

  /**
   * get with wait timeout. Use a timeout of 0 to wait forever.
   * 
   * @param timeout
   * @return
   */
  public synchronized int get( int timeout )
  {
    try
    {
      wait(timeout);
    }
    catch( InterruptedException booga )
    {
    }

    return number;
  }

  public synchronized void set( int number )
  {
    this.number = number;
    notify();
  }
}
