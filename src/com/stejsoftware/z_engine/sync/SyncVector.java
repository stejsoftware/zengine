package com.stejsoftware.z_engine.sync;

import java.util.Vector;

/**
 * This is a blocking synchronized vector. It should be Thread safe.
 * 
 */
public class SyncVector extends Vector<String>
{
  private static final long serialVersionUID = 1710098618297750717L;

  public synchronized boolean isEmpty()
  {
    return super.isEmpty();
  }

  /**
   * no wait
   * 
   * @return
   */
  public synchronized String syncPopFirstElement()
  {
    String first = syncFirstElement();

    if( first != null ) removeElementAt(0);

    return first;
  }

  public synchronized String syncPopFirstElement(long timeout)
  {
    String first = syncFirstElement(timeout);

    if( first != null ) removeElementAt(0);

    return first;
  }

  /**
   * no wait
   * 
   * @return
   */
  public synchronized String syncFirstElement()
  {
    if( !super.isEmpty() )
    {
      return super.firstElement();
    }

    return null;
  }

  public synchronized String syncFirstElement(long timeout)
  {
    try
    {
      wait(timeout);
    }
    catch( InterruptedException ex )
    {
      return null;
    }

    return syncFirstElement();
  }

  public synchronized void syncAddElement(String element)
  {
    super.addElement(element);
    notify();
  }
}
