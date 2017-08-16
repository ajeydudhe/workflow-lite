/********************************************************************
 * File Name:    ActionableActivityBean.java
 *
 * Date Created: Aug 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean.activity;

import org.workflowlite.core.Activity;
import org.workflowlite.core.ExecutionContext;
import org.workflowlite.core.bean.BeanInstantiator;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class ActionableActivityBean extends ActivityBean
{
  public ActionableActivityBean(final String activityBeanId, final BeanInstantiator beanInstantiator)
  {
    super(beanInstantiator);
    
    this.activityBeanId = activityBeanId;
  }
  
  @Override
  public Object execute(final ExecutionContext context, final Object source)
  {
    final Activity activity = this.beanInstantiator.getActivity(this.activityBeanId, source);
    
    return activity.execute(context);
  }
  
  // Private
  private final String activityBeanId;
}

