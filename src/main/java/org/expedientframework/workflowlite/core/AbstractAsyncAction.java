/********************************************************************
 * File Name:    AbstractAsyncAction.java
 *
 * Date Created: Aug 24, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core;

import java.util.concurrent.CompletableFuture;
  
/**
 * Abstract class returning a {@link CompletableFuture}.
 * Extend this class for having asynchronous actions in a workflow.
 * 
 * @author Ajey_Dudhe
 *
 * @param <TContext> Type extending {@link ExecutionContext}
 * @param <TResult> Type of result.
 */
public abstract class AbstractAsyncAction<TContext extends ExecutionContext, TResult> extends AbstractAction<TContext, CompletableFuture<TResult>>
{
}

