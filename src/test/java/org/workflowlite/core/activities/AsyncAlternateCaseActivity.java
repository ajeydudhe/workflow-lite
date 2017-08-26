//CHECKSTYLE:OFF
/********************************************************************
 * File Name:    AlternateCaseActivity.java
 *
 * Date Created: Aug 22, 2017
 *
 * ------------------------------------------------------------------
 * SYMANTEC: Copyright © 2017 Symantec Corporation. All rights reserved.
 * THIS SOFTWARE CONTAINS CONFIDENTIAL INFORMATION AND TRADE SECRETS OF
 * SYMANTEC CORPORATION.USE, DISCLOSURE OR REPRODUCTION IS PROHIBITED
 * WITHOUT THE PRIOR EXPRESS WRITTEN PERMISSION OF SYMANTEC CORPORATION.
 * The Licensed Software and Documentation are deemed to be commercial
 * computer software as defined in FAR 12.212 and subject to restricted
 * rights as defined in FAR Section 52.227-19 "Commercial Computer Software
 * - Restricted Rights" and DFARS 227.7202, "Rights in Commercial Computer
 * Software or Commercial Computer Software Documentation", as applicable,
 * and any successor regulations.  Any use, modification, reproduction
 * release, performance, display or disclosure of the Licensed Software
 * and Documentation by the U.S. Government shall be solely in accordance
 * with the terms of this Agreement.
 *******************************************************************/
//CHECKSTYLE:ON

// PACKAGE/IMPORTS --------------------------------------------------
package org.workflowlite.core.activities;

import java.util.concurrent.CompletableFuture;

import org.workflowlite.core.AbstractAsyncActivity;
import org.workflowlite.core.DefaultExecutionContext;
  
public class AsyncAlternateCaseActivity extends AbstractAsyncActivity<DefaultExecutionContext, String>
{
  public AsyncAlternateCaseActivity(final String value)
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

