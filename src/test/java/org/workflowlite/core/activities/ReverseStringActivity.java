/********************************************************************
 * File Name:    ReverseStringActivity.java
 *
 * Date Created: Aug 6, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.activities;

import javax.inject.Inject;

import org.workflowlite.core.AbstractActivity;
import org.workflowlite.core.ExecutionContext;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class ReverseStringActivity extends AbstractActivity
{
  @Inject
  public ReverseStringActivity(final String value)
  {
    super(ReverseStringActivity.class.getSimpleName());
    
    this.value = value;
  }

  public Object execute(final ExecutionContext context)
  {
    return new StringBuilder(this.value).reverse().toString();
  }
  
  // Private
  private final String value;
}

