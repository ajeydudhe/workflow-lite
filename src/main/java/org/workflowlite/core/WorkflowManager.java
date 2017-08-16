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

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.workflowlite.core.bean.activity.ActivityBean;
import org.workflowlite.core.bean.activity.ConditionalActivityBean;
import org.workflowlite.core.bean.BeanInstantiator;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class WorkflowManager
{
  @SuppressWarnings("unchecked")
  public void execute(final String workflowBeanId, final Object source) // TODO: Ajey - do not use bean id in name since the id should be abstracted
  {
    LOGGER.info("Executing workflow with bean id [{}]", workflowBeanId);
    final Workflow workflow = this.beanInstantiator.getWorkflow(workflowBeanId);
    
    Object previousActivityOutput = source;  
    final LinkedList<ActivityBean> activities = new LinkedList<>(workflow.getActivities());
    for (ActivityBean activityBean = activities.poll(); activityBean != null; activityBean = activities.poll())
    {
      LOGGER.info("Invoking activity [{}]", activityBean);

      final Object result = activityBean.execute(null, previousActivityOutput);
      if(activityBean instanceof ConditionalActivityBean) // TODO: Ajey - Avoid type casting. Can we use visitor here ???
      {
        activities.addAll(0, (List<ActivityBean>)result);
        continue;
      }
      
      LOGGER.info("Result: {}", result);
      
      previousActivityOutput = result;
    }
  }

  // Private
  @Inject
  private BeanInstantiator beanInstantiator; // TODO: Ajey - Inject using setter
  
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowManager.class);
}

