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

package org.expedientframework.workflowlite.core;

import java.io.InputStream;
import java.util.List;
  
/**
 * Interface for fetching the workflow definitions.
 * 
 * @author Ajey_Dudhe
 *
 */
public interface WorkflowDefinitionsProvider
{
  /**
   * Should return the workflow definitions from the respective source.
   * For example, from Uml definition files.
   * 
   * @return The list of workflow definitions as streams.
   */
  public List<InputStream> getDefinitions();
}

