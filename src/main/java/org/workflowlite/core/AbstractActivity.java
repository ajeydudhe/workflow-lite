/********************************************************************
 * File Name:    AbstractActivity.java
 *
 * Date Created: Aug 5, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public abstract class AbstractActivity implements Activity
{
  protected AbstractActivity(final String name)
  {
    // TODO: Ajey - input validation
    this.name = name;
  }
  
  public String getName()
  {
    return this.name;
  }

  // Protected
  protected final String name;
}

