/********************************************************************
 * File Name:    WorkflowDefinitionsProvider.java
 *
 * Date Created: Aug 18, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.io.InputStream;
import java.util.List;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public interface WorkflowDefinitionsProvider
{
  public List<InputStream> getDefinitionXmls();
}

