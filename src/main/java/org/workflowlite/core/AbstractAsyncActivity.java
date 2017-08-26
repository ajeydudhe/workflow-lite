/********************************************************************
 * File Name:    AbstractAsyncActivity.java
 *
 * Date Created: Aug 24, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.concurrent.CompletableFuture;
  
/**
 * Abstract activity class returning a {@link CompletableFuture}.
 * @author Ajey_Dudhe
 *
 * @param <TContext> Type extending {@link ExecutableFuture}
 * @param <TResult> Type of result.
 */
public abstract class AbstractAsyncActivity<TContext extends ExecutionContext, TResult> extends AbstractActivity<TContext, CompletableFuture<TResult>>
{
}

