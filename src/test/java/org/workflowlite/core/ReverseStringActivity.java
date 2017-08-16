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

package org.workflowlite.core;

import javax.inject.Inject;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class ReverseStringActivity extends AbstractActivity
{
  @Inject
  public ReverseStringActivity(final String name, final String value)
  {
    super(name);
    
    this.value = value;
  }

  public Object execute(final ExecutionContext context)
  {
    return new StringBuilder(this.value).reverse().toString();
  }
  
  // Private
  private final String value;
}

