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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
class Workflow
{
  public Workflow(final String name, final List<?> activities)
  {
    // TODO: Ajey - input validations ???
    this.name = name;
    
    this.activities = this.getActivities(activities);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public List<ActivityBean> getActivities()
  {
    return this.activities;
  }

  @SuppressWarnings("unchecked")
  public List<ActivityBean> getActivities(final Object inputActivities)
  {
    List<Object> activities = new ArrayList<>();
    if(inputActivities instanceof List)
    {
      activities = (List<Object>) inputActivities;
    }
    else
    {
      activities.add(inputActivities);
    }
    
    final List<ActivityBean> activityBeans = new ArrayList<>();
    activities.forEach(activity -> {
      
      final ActivityBean activityBean = (activity instanceof Map ? new ConditionalActivityBean((Map<String, Object>)activity) : new ActionableActivityBean(activity));
      
      activityBeans.add(activityBean);
    });
    
    return Collections.unmodifiableList(activityBeans);
  }
  
  // Private
  private final String name;
  private List<ActivityBean> activities;
  
  @Inject
  private BeanInstantiator beanInstantiator; // TODO: Ajey - Inject using setter
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Workflow.class);

  // Inner class
  public abstract class ActivityBean
  {
    public ActivityBean(final Object activityBean)
    {
      // TODO: Ajey - cannot be null
      
      this.activityBean = activityBean;
    }
    
    public abstract Object execute(final ExecutionContext context, final Object source);    
    
    protected Activity getInstance(final Object source)
    {
      if(this.activityBean instanceof Activity)
      {
        return (Activity) this.activityBean;
      }
      
      if(this.activityBean instanceof String)
      {
        LOGGER.info("Creating instance of activity with bean id [{}] and source [{}]", this.activityBean, source);
        return beanInstantiator.getActivity(this.activityBean.toString(), source);
      }
      
      throw new RuntimeException("Invalid activityBean type " + this.activityBean.getClass()); // TODO: Ajey - Throw custom exception !!!
    }
    
    // protected
    protected final Object activityBean;
  }
  
  public final class ActionableActivityBean extends ActivityBean
  {
    public ActionableActivityBean(final Object activityBean)
    {
      super(activityBean);
    }

    @Override
    public Object execute(final ExecutionContext context, final Object source)
    {
      final Activity activity = this.getInstance(source);
      return activity.execute(context);
    }    
  }

  public final class ConditionalActivityBean extends ActivityBean
  {
    public ConditionalActivityBean(final Map<String, Object> activityBean)
    {
      super(activityBean);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ActivityBean> execute(final ExecutionContext context, final Object source)
    {
      final Map<String, Object> options = (Map<String, Object>) this.activityBean;
      final String condition = (String) options.get("condition");
      
      // TODO: Ajey - Validation: condition cannot be null, the result of condition evaluation should be a key in the map else there should
      // be a default entry
      LOGGER.info("Condition: [{}]", condition);
      
      final Object conclusion = beanInstantiator.evaluateExpression(condition, source);
      
      LOGGER.info("Conclusion: [{}]", conclusion);

      Object activityBeans = options.get(conclusion.toString());
      if(activityBeans == null)
      {
        activityBeans = options.get("default");
      }
      
      if(activityBeans == null)
      {
        throw new RuntimeException("Could not find activities to be executed on given condition."); // TODO: Ajey - Throw custom exception !!!
      }
      
      return getActivities(activityBeans);
    }    
  }
}

