/********************************************************************
 * File Name:    AbstractActivity.java
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
public abstract class AbstractActivity<TContext extends ExecutionContext, TResult> implements Activity<TContext, TResult>
{
  public String getName()
  {
    return this.getClass().getSimpleName();
  }
}

