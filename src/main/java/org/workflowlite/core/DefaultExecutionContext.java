/********************************************************************
 * File Name:    DefaultExecutionContext.java
 *
 * Date Created: Aug 21, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

/**
 * Default {@link ExecutionContext} implementation allowing to store/retrieve properties.
 * 
 * @author Ajey_Dudhe
 *
 */
public class DefaultExecutionContext extends AbstractExecutionContext
{
  public DefaultExecutionContext(final String workflowId)
  {
    super(workflowId);
  }
}

