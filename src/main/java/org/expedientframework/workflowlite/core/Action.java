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

package org.expedientframework.workflowlite.core;
  
/**
 * The action interface defining the task in a workflow.
 * Consider extending from {@link AbstractAction} or {@link AbstractAsyncAction} 
 * instead of directly implementing this interface. 
 * 
 * @author Ajey_Dudhe
 *
 * @param <TContext> Type extending {@link ExecutionContext}
 * @param <TResult> Type of result.
 */
public interface Action<TContext extends ExecutionContext, TResult>
{
  /**
   * @return The name of the workflow action.
   */
  public String getName();
  
  /**
   * Called by the framework to execute the task.
   * 
   * @param context The execution context for the workflow. Avoiding using this context for passing parameters. Use dependency injection instead.
   * 
   * @return The result of the execution.
   */
  public TResult execute(TContext context);
}

