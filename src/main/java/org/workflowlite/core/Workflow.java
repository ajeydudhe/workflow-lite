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

import org.workflowlite.core.bean.activity.ActivityBean;
import org.workflowlite.core.bean.activity.ConditionalActivityBean;
  
/**
 * Workflow class responsible for executing the activities in given sequence.
 * It also takes care of conditional statement execution.
 * 
 * @author Ajey_Dudhe
 *
 */
public final class Workflow
{
  private Workflow(final String name, final List<ActivityBean> activities)
  {
    this.name = name;
    
    this.activities = Collections.unmodifiableList(activities);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  @SuppressWarnings("unchecked")
  public <TSource, TResult> TResult execute(final ExecutionContext context, final TSource source)
  {
    Object previousActivityOutput = source;  
    final LinkedList<ActivityBean> activities = new LinkedList<>(this.activities);
    for (ActivityBean activityBean = activities.poll(); activityBean != null; activityBean = activities.poll())
    {
      final Object result = activityBean.execute(context, source, previousActivityOutput);
      if(activityBean instanceof ConditionalActivityBean) // TODO: Ajey - Avoid type casting. Can we use visitor here ???
      {
        activities.addAll(0, (List<ActivityBean>)result);
        continue;
      }
      
      previousActivityOutput = result;
    }
    
    return (TResult) previousActivityOutput;
  }

  // Private
  private final String name;
  private List<ActivityBean> activities;  
  //private static final Logger LOGGER = LoggerFactory.getLogger(Workflow.class);
}

