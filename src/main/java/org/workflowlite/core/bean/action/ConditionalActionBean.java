/********************************************************************
 * File Name:    ConditionalActionBean.java
 *
 * Date Created: Aug 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.workflowlite.core.ExecutionContext;
import org.workflowlite.core.bean.BeanInstantiator;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class ConditionalActionBean extends ActionBean
{
  private ConditionalActionBean(final Map<String, Object> switchStatementAsMap, final BeanInstantiator beanInstantiator)
  {
    super(beanInstantiator);
    
    this.switchStatementAsMap = switchStatementAsMap;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object execute(final ExecutionContext context, final Object source, final Object output)
  {
    final String conditionExpression = (String) this.switchStatementAsMap.get("condition");
    
    Assert.hasText(conditionExpression, "@conditionExpression cannot be null or empty.");
    
    final Object conclusion = this.beanInstantiator.evaluateExpression(conditionExpression, context, source, output);

    LOGGER.info("Condition [{}] evaluated to [{}]", conditionExpression, conclusion);
    
    // TODO: Ajey - Handle null conclusion
    List<ActionBean> activities = (List<ActionBean>) this.switchStatementAsMap.get(conclusion.toString());
    
    if(activities == null)
    {
      activities = (List<ActionBean>) this.switchStatementAsMap.get("default");
    }
    
    if(activities == null)
    {
      throw new RuntimeException("Could not find activities to be executed on given condition."); // TODO: Ajey - Throw custom exception !!!
    }
    
    return activities;
  }
  
  // Private
  private final Map<String, Object> switchStatementAsMap;
  private final static Logger LOGGER = LoggerFactory.getLogger(ConditionalActionBean.class);
}

