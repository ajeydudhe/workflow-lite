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
 * Abstract class returning the {@link Class#getSimpleName()} as name.
 * 
 * @author Ajey_Dudhe
 *
 * @param <TContext> Type extending {@link ExecutionContext}
 * @param <TResult> Type of result.
 */
public abstract class AbstractAction<TContext extends ExecutionContext, TResult> implements Action<TContext, TResult>
{
  @Override
  public String getName()
  {
    return this.getClass().getSimpleName();
  }
}

