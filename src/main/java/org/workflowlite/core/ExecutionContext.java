/********************************************************************
 * File Name:    ExecutionContext.java
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
 * Simple interface allowing to pass workflow specific data.
 * <br/><b>Note:</b> This should rarely be used for passing any data between 
 * activities. Use Spring Depenedncy Injection with Spring Expression Language
 * to inject such data.
 * 
 * @author Ajey_Dudhe
 *
 */
public interface ExecutionContext
{
  public String getWorkflowId();
  public String getSourcePropertyName();
  public <T> T getValue(final String property);
  public <T> void setValue(final String property, final T value);
}

