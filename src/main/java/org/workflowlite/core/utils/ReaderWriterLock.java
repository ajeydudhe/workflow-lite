/********************************************************************
 * File Name:    ReaderWriterLockSentry.java
 *
 * Date Created: Aug 17, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.utils;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
  
/**
 * Wrapper over the ReadWriter lock to close the lock using try-with-resource syntax.
 * @author Ajey_Dudhe
 *
 */
public class ReaderWriterLock
{
  public LockSentry readLock()
  {
    this.readerWriteLock.readLock().lock();
    return new LockSentry(this.readerWriteLock.readLock());
  }

  public LockSentry writeLock()
  {
    this.readerWriteLock.writeLock().lock();
    return new LockSentry(this.readerWriteLock.writeLock());
  }
  
  // Private
  private final ReadWriteLock readerWriteLock = new ReentrantReadWriteLock(); // Can have ctor accepting the lock or other params if required.
}

