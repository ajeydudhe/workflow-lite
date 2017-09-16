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
   * Executes the given workflow specified in {@link ExecutionContext#getWorkflowId()}
   * @param executionContext The {@link ExecutionContext} for the workflow.
   * @param source The input for the workflow.
   * @return The result from the last activity executed.
   */
  @SuppressWarnings("unchecked")
  public <TSource, TResult> TResult execute(final ExecutionContext executionContext, final TSource source)
  {
    LOGGER.debug("Creating instance of workflow with bean id [{}]", executionContext.getWorkflowId());
    
    final Workflow workflow = this.beanInstantiator.getWorkflow(executionContext.getWorkflowId());
    
    LOGGER.info("Executing workflow [{}]", workflow.getName());

    return (TResult) workflow.execute(executionContext, source);
  }

  // Private
  private BeanInstantiator beanInstantiator;  
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowManager.class);
}

