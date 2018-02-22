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
 * Abstract activity class returning a {@link CompletableFuture}.
 * @author Ajey_Dudhe
 *
 * @param <TContext> Type extending {@link ExecutableFuture}
 * @param <TResult> Type of result.
 */
public abstract class AbstractAsyncAction<TContext extends ExecutionContext, TResult> extends AbstractAction<TContext, CompletableFuture<TResult>>
{
}

