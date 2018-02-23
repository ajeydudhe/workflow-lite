/********************************************************************
 * File Name:    AddBonusMarksAction.java
 *
 * Date Created: Sep 17, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.samples;

import org.expedientframework.workflowlite.core.AbstractAction;
import org.expedientframework.workflowlite.core.ExecutionContext;
  
public class AddBonusMarksAction extends AbstractAction<ExecutionContext, Integer>
{
  public AddBonusMarksAction(final int totalMarks)
  {
    this.totalMarks = totalMarks;
  }
  
  @Override
  public Integer execute(final ExecutionContext context)
  {
    return totalMarks + 10;
  }
  
  // Private
  private final int totalMarks;
}

