/********************************************************************
 * File Name:    WorkflowliteIOException.java
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
 * Wrapper for the {@link IOException}
 *
 */
public class WorkflowliteIOException extends WorkflowliteException
{
  WorkflowliteIOException(IOException cause)
  {
    super(cause);
  }

  private static final long serialVersionUID = -9030523312540083165L;
}

