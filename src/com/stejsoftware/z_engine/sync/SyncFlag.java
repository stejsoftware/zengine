/**
 * Copyright 2008 Jonathan Meyer <insixdays@gmail.com>
 */
package com.stejsoftware.z_engine.sync;

/**
 * @author jon
 * 
 */
public class SyncFlag
{
  private boolean bool = false;

  public SyncFlag()
  {
  }

  public SyncFlag(boolean inital_value)
  {
    this.bool = inital_value;
  }

  /**
   * get with no wait
   * 
   * @return
   */
  public synchronized boolean get()
  {
    return bool;
  }

  /**
   * get with wait timeout. Use a timeout of 0 to wait forever.
   * 
   * @param timeout
   * @return
   */
  public synchronized boolean get(int timeout)
  {
    try
    {
      wait(timeout);
    }
    catch( InterruptedException booga )
    {
    }

    return bool;
  }

  public synchronized void set(boolean bool)
  {
    this.bool = bool;
    notify();
  }
}
