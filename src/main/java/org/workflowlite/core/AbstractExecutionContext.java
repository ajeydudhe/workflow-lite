/********************************************************************
 * File Name:    AbstractExecutionContext.java
 *
 * Date Created: Sep 19, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.Map;
import java.util.TreeMap;

/**
 * Default {@link ExecutionContext} implementation allowing to store/retrieve properties.
 * 
 * @author Ajey_Dudhe
 *
 */
public abstract class AbstractExecutionContext implements ExecutionContext
{
  public AbstractExecutionContext(final String workflowId)
  {
    this.workflowId = workflowId;
  }
  
  @Override
  public String getWorkflowId()
  {
    return this.workflowId;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue(final String property)
  {
    return (T) this.properties.get(property);
  }

  @Override
  public <T> void setValue(String property, T value)
  {
    this.properties.put(property, value);
  }
  
  // Private
  private final String workflowId;
  private final Map<String, Object> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
}

