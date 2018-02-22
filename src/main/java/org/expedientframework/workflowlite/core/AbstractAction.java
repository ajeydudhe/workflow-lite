/********************************************************************
 * File Name:    AbstractAction.java
 *
 * Date Created: Aug 5, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public abstract class AbstractAction<TContext extends ExecutionContext, TResult> implements Action<TContext, TResult>
{
  public String getName()
  {
    return this.getClass().getSimpleName();
  }
}

