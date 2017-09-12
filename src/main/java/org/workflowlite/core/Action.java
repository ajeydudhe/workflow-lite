/********************************************************************
 * File Name:    Action.java
 *
 * Date Created: Aug 5, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public interface Action<TContext extends ExecutionContext, TResult>
{
  public String getName();
  public TResult execute(TContext context);
}

