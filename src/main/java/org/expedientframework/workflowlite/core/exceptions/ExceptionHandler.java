/********************************************************************
 * File Name:    ExceptionHandler.java
 *
 * Date Created: Aug 12, 2018
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2018 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.exceptions;

import java.io.IOException;

/**
 * Helper class for exception handling
 *
 */
public final class ExceptionHandler
{
  public static WorkflowliteException throwWorkflowliteException(final Throwable cause)
  {
    throw cause instanceof WorkflowliteException ? (WorkflowliteException)cause : new WorkflowliteException(cause);
  }
  
  public static WorkflowliteException throwException(final IOException cause)
  {
    throw new WorkflowliteIOException(cause);
  }
  
  public static WorkflowliteException throwUmlParsingException(final String message)
  {
    throw new WorkflowliteUmlParsingException(message);
  }
}

