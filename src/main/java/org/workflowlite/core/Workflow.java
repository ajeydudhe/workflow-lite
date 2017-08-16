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
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.workflowlite.core.bean.activity.ActivityBean;
  
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
  
  public List<ActivityBean> getActivities()
  {
    return this.activities;
  }

  // Private
  private final String name;
  private List<ActivityBean> activities;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Workflow.class);
}

