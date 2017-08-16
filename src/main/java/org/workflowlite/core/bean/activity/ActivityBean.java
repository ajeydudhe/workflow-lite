/********************************************************************
 * File Name:    ActivityBean.java
 *
 * Date Created: Aug 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean.activity;

import javax.inject.Inject;

import org.workflowlite.core.ExecutionContext;
import org.workflowlite.core.bean.BeanInstantiator;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public abstract class ActivityBean
{
  protected ActivityBean(final BeanInstantiator beanInstantiator)
  {
    this.beanInstantiator = beanInstantiator;
  }
  
  public abstract Object execute(final ExecutionContext context, final Object source);
  
  protected final BeanInstantiator beanInstantiator;
}

