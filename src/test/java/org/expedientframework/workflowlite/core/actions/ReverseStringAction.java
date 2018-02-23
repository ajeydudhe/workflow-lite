/********************************************************************
 * File Name:    ReverseStringAction.java
 *
 * Date Created: Aug 6, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.actions;

import javax.inject.Inject;

import org.expedientframework.workflowlite.core.AbstractAction;
import org.expedientframework.workflowlite.core.ExecutionContext;

public class ReverseStringAction extends AbstractAction<ExecutionContext, String>
{
  @Inject
  public ReverseStringAction(final String value)
  {
    this.value = value;
  }

  public String execute(final ExecutionContext context)
  {
    return new StringBuilder(this.value).reverse().toString();
  }
  
  // Private
  private final String value;
}

