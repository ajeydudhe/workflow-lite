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

package org.workflowlite.core.samples;

import org.workflowlite.core.AbstractAction;
import org.workflowlite.core.ExecutionContext;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
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

