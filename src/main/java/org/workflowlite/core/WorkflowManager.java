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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.workflowlite.core.bean.BeanInstantiator;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class WorkflowManager
{
  public WorkflowManager(final BeanInstantiator beanInstantiator)
  {
    this.beanInstantiator = beanInstantiator;
  }
  
  public void execute(final String workflowBeanId, final Object source) // TODO: Ajey - do not use bean id in name since the id should be abstracted
  {
    LOGGER.info("Executing workflow with bean id [{}]", workflowBeanId);
    final Workflow workflow = this.beanInstantiator.getWorkflow(workflowBeanId);
    workflow.execute(null, source);
  }

  // Private
  private BeanInstantiator beanInstantiator; // TODO: Ajey - Inject using setter
  
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowManager.class);
}

