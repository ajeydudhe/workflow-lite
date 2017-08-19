/********************************************************************
 * File Name:    WorkflowManager.java
 *
 * Date Created: Aug 6, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.workflowlite.core.bean.BeanInstantiator;

/**
 * Main class responsible for executing the workflow with given ID and input.
 * 
 * @author Ajey_Dudhe
 *
 */
public final class WorkflowManager
{
  private WorkflowManager(final BeanInstantiator beanInstantiator)
  {
    this.beanInstantiator = beanInstantiator;
  }
  
  /**
   * Executes the workflow.
   * @param workflowId The id of the given workflow.
   * @param source The input for the workflow.
   * @return The result from the last activity executed in the workflow.
   */
  public Object execute(final String workflowId, final Object source)
  {
    LOGGER.debug("Creating instance of workflow with bean id [{}]", workflowId);
    
    final Workflow workflow = this.beanInstantiator.getWorkflow(workflowId);
    
    LOGGER.info("Executing workflow [{}]", workflow.getName());

    final ExecutionContext context = createExecutionContext(workflowId);
    
    return workflow.execute(context, source);
  }

  // Private
  
  private ExecutionContext createExecutionContext(final String workflowId)
  {
    return new ExecutionContext()
    {     
      @Override
      public String getWorkflowId()
      {
        return workflowId;
      }

      @Override
      public <T> void setValue(final String property, final T value)
      {
        this.properties.put(property, value);
      }
      
      @SuppressWarnings("unchecked")
      @Override
      public <T> T getValue(final String property)
      {
        return (T) this.properties.get(property);
      }
      
      // Private
      private final Map<String, Object> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    };
  }

  // Private
  private BeanInstantiator beanInstantiator;  
  
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowManager.class);
}

