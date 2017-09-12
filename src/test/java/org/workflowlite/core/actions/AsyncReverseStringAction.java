/********************************************************************
 * File Name:    AsyncReverseStringAction.java
 *
 * Date Created: Aug 24, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.actions;

import java.util.concurrent.CompletableFuture;


import org.workflowlite.core.AbstractAsyncAction;
import org.workflowlite.core.DefaultExecutionContext;
  
public class AsyncReverseStringAction extends AbstractAsyncAction<DefaultExecutionContext, String>
{
  protected AsyncReverseStringAction(final String value)
  {
    this.value = value;
  }
  
  @Override
  public CompletableFuture<String> execute(DefaultExecutionContext context)
  {
    final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        
        if(this.bThrowException)
        {
          throw new RuntimeException("Dummy exception thrown from " + this.getClass().getSimpleName() + ".");
        } 
        
        return new StringBuilder(this.value).reverse().toString();
      });
    
    if(this.bCancel)
    {
      future.cancel(true);
    }
    
    return future;
  }

  public void setThrowException(boolean bThrowException)
  {
    this.bThrowException = bThrowException;
  }

  public void setCancel(boolean bCancel)
  {
    this.bCancel = bCancel;
  }
  
  // Private
  private String value;
  private boolean bThrowException;
  private boolean bCancel;
}

