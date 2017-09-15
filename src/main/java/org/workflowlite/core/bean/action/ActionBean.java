/********************************************************************
 * File Name:    ActionBean.java
 *
 * Date Created: Aug 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean.action;

import org.workflowlite.core.ExecutionContext;
import org.workflowlite.core.bean.BeanInstantiator;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public abstract class ActionBean
{
  protected ActionBean(final BeanInstantiator beanInstantiator)
  {
    this.beanInstantiator = beanInstantiator;
  }
  
  public abstract Object execute(final ExecutionContext context, final Object source, final Object output);
  
  protected final BeanInstantiator beanInstantiator;
}

