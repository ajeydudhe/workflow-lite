/********************************************************************
 * File Name:    Workflow.java
 *
 * Date Created: Aug 5, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.workflowlite.core.bean.activity.ActivityBean;
import org.workflowlite.core.bean.activity.ConditionalActivityBean;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class Workflow
{
  public Workflow(final String name, final List<ActivityBean> activities)
  {
    // TODO: Ajey - input validations ???
    this.name = name;
    
    this.activities = Collections.unmodifiableList(activities);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  @SuppressWarnings("unchecked")
  public Object execute(final ExecutionContext context, final Object source)
  {
    Object previousActivityOutput = source;  
    final LinkedList<ActivityBean> activities = new LinkedList<>(this.activities);
    for (ActivityBean activityBean = activities.poll(); activityBean != null; activityBean = activities.poll())
    {
      final Object result = activityBean.execute(null, previousActivityOutput);
      if(activityBean instanceof ConditionalActivityBean) // TODO: Ajey - Avoid type casting. Can we use visitor here ???
      {
        activities.addAll(0, (List<ActivityBean>)result);
        continue;
      }
      
      previousActivityOutput = result;
    }
    
    return previousActivityOutput;
  }

  // Private
  private final String name;
  private List<ActivityBean> activities;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Workflow.class);
}

