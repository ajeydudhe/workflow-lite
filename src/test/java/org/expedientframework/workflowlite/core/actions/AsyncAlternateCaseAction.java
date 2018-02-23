/********************************************************************
 * File Name:    AsyncAlternateCaseAction.java
 *
 * Date Created: Aug 22, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.actions;

import java.util.concurrent.CompletableFuture;

import org.expedientframework.workflowlite.core.AbstractAsyncAction;
import org.expedientframework.workflowlite.core.DefaultExecutionContext;
  
public class AsyncAlternateCaseAction extends AbstractAsyncAction<DefaultExecutionContext, String>
{
  public AsyncAlternateCaseAction(final String value)
  {
    this.value = value;
  }

  @Override
  public CompletableFuture<String> execute(DefaultExecutionContext context)
  {
    final StringBuilder builder = new StringBuilder();

    for (int nIndex = 0; nIndex < this.value.length(); ++nIndex)
    {
      final Character character = this.value.charAt(nIndex);      
      builder.append( (nIndex % 2 == 0 ? Character.toLowerCase(character) : Character.toUpperCase(character))  );
    }

    final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> builder.toString());
    
    return future;
  }

  // Private
  private String value;
}

