/********************************************************************
 * File Name:    ConditionalActivityBean.java
 *
 * Date Created: Aug 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean.activity;

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
public class ConditionalActivityBean extends ActivityBean
{
  public ConditionalActivityBean(final Map<String, Object> switchStatementAsMap, final BeanInstantiator beanInstantiator)
  {
    super(beanInstantiator);
    
    this.switchStatementAsMap = switchStatementAsMap;
  }

  @Override
  public Object execute(final ExecutionContext context, final Object source)
  {
    final String conditionExpression = (String) this.switchStatementAsMap.get("condition");
    
    Assert.hasText(conditionExpression, "@conditionExpression cannot be null or empty.");
    
    final Object conclusion = this.beanInstantiator.evaluateExpression(conditionExpression, source);

    LOGGER.info("Condition [{}] evaluated to [{}]", conditionExpression, conclusion);
    
    // TODO: Ajey - Handle null conclusion
    List<ActivityBean> activities = (List<ActivityBean>) this.switchStatementAsMap.get(conclusion.toString());
    
    if(activities == null)
    {
      activities = (List<ActivityBean>) this.switchStatementAsMap.get("'default");
    }
    
    if(activities == null)
    {
      throw new RuntimeException("Could not find activities to be executed on given condition."); // TODO: Ajey - Throw custom exception !!!
    }
    
    return activities;
  }
  
  // Private
  private final Map<String, Object> switchStatementAsMap;
  private final static Logger LOGGER = LoggerFactory.getLogger(ConditionalActivityBean.class);
}

