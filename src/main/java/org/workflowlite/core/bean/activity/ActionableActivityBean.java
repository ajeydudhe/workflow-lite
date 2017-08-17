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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  public Object execute(final ExecutionContext context, final Object source, final Object output)
  {
    LOGGER.info("Executing activity [{}]", this.activityBeanId);
    
    final Activity activity = this.beanInstantiator.getActivity(this.activityBeanId, context, source, output);
    
    final Object result = activity.execute(context);
    
    LOGGER.info("Activity [{}] result: [{}]", this.activityBeanId, result); // TODO: Ajey - Log at debug level !!!
    
    return result;
  }
  
  // Private
  private final String activityBeanId;

  private static final Logger LOGGER = LoggerFactory.getLogger(ActionableActivityBean.class);
}

