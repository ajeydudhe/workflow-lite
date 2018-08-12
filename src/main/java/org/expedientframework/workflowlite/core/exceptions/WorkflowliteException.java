/********************************************************************
 * File Name:    WorkflowliteException.java
 *
 * Date Created: Aug 12, 2018
 *
 * ------------------------------------------------------------------
 * 
 * Copyright (c) 2018 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.exceptions;
  
/**
 * Base class for all the workflow exceptions.
 *
 */
public class WorkflowliteException extends RuntimeException
{
  WorkflowliteException(final Throwable cause)
  {
    super(cause);
  }

  private static final long serialVersionUID = -6163660161859059816L;
}

