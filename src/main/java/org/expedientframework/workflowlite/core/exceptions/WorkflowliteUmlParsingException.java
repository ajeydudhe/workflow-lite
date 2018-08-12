/********************************************************************
 * File Name:    WorkflowliteUmlParsingException.java
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
 * UML related exceptions. This should get handled during development only so not need to granular exception types.
 *
 */
public class WorkflowliteUmlParsingException extends WorkflowliteException
{
  WorkflowliteUmlParsingException(final String message)
  {
    super(message);
  }

  private static final long serialVersionUID = 6706840971984418339L;
}

