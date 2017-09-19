/********************************************************************
 * File Name:    StudentWorkflowExecutionContext.java
 *
 * Date Created: Sep 19, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.samples;

import org.workflowlite.core.AbstractExecutionContext;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class StudentWorkflowExecutionContext extends AbstractExecutionContext
{
  public StudentWorkflowExecutionContext()
  {
    super("StudentScoreCardWorkflow");
  }
  
  public boolean completedExtracurricularActivities(final String studentName)
  {
    return "aeiou".contains(studentName.substring(0, 1).toLowerCase());
  }
}

