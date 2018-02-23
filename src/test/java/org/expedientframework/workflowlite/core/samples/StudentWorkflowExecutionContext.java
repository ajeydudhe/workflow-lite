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

package org.expedientframework.workflowlite.core.samples;

import org.expedientframework.workflowlite.core.AbstractExecutionContext;
  
public class StudentWorkflowExecutionContext extends AbstractExecutionContext
{
  public StudentWorkflowExecutionContext()
  {
    super("StudentScoreCardWorkflow", "student");
  }
  
  public boolean completedExtracurricularActivities(final String studentName)
  {
    return "aeiou".contains(studentName.substring(0, 1).toLowerCase());
  }
}

