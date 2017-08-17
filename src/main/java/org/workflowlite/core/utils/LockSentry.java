/********************************************************************
 * File Name:    LockSentry.java
 *
 * Date Created: Aug 17, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.utils;

import java.io.Closeable;
import java.util.concurrent.locks.Lock;
  
/**
 * Wrapper to use the lock using try-with-resource syntax.
 *
 */
public class LockSentry implements Closeable
{
  public LockSentry(final Lock lock)
  {
    this.lock = lock;
  }
  
  @Override
  public void close()
  {
    if(this.lock != null)
    {
      this.lock.unlock();
      this.lock = null;
    }
  }
  
  // Private
  private Lock lock;
}

