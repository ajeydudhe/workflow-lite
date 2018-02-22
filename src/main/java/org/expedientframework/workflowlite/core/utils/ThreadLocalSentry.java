/********************************************************************
 * File Name:    ThreadLocalSentry.java
 *
 * Date Created: Aug 14, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.utils;

import java.io.Closeable;
  
/**
 * Wrapper to use the thread local variable setting and removal using try-with-resource syntax.
 *
 */
public class ThreadLocalSentry<T> implements Closeable
{
  public ThreadLocalSentry(final T initialValue)
  {
    this.threadLocal = new ThreadLocal<>();
    this.threadLocal.set(initialValue);
  }

  public ThreadLocalSentry<T> set(final T value)
  {
    this.threadLocal.set(value);
    return this;
  }
  
  public T get()
  {
    return this.threadLocal.get();
  }
  
  @Override
  public void close()
  {
    this.threadLocal.remove();
  }
  
  // Private
  private ThreadLocal<T> threadLocal;
}

