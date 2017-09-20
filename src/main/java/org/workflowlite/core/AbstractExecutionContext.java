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

import org.springframework.util.Assert;

/**
 * Default {@link ExecutionContext} implementation allowing to store/retrieve properties.
 * 
 * @author Ajey_Dudhe
 *
 */
public abstract class AbstractExecutionContext implements ExecutionContext
{
  public AbstractExecutionContext(final String workflowId, final String sourcePropertyName)
  {
    Assert.hasText(workflowId, "@workflowId cannot be null or empty.");
    Assert.hasText(sourcePropertyName, "@sourcePropertyName cannot be null or empty.");
    
    this.workflowId = workflowId;
    this.sourcePropertyName = sourcePropertyName;
  }
  
  @Override
  public String getWorkflowId()
  {
    return this.workflowId;
  }

  public String getSourcePropertyName()
  {
    return this.sourcePropertyName;
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
  private final String sourcePropertyName;
  private final Map<String, Object> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
}

