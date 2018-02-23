/********************************************************************
 * File Name:    DefaultExecutionContext.java
 *
 * Date Created: Aug 21, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core;

/**
 * Default {@link ExecutionContext} implementation allowing to store/retrieve properties.
 * 
 * @author Ajey_Dudhe
 *
 */
public class DefaultExecutionContext extends AbstractExecutionContext
{
  /**
   * Default constructor taking in the workflow id. It uses 'source' default name for referring the 
   * original input object in the Spring Expression Language.
   * 
   * @param workflowId The workflow id. This should match the ID in the UML diagram.
   */
  public DefaultExecutionContext(final String workflowId)
  {
    this(workflowId, "source");
  }

  /**
   * Constructor taking in the workflow id and the source property name.
   * 
   * @param workflowId The workflow id. This should match the ID in the UML diagram.
   * @param sourcePropertyName The name by which the input object should be referred in the 
   * Spring Expression Languange.
   */
  public DefaultExecutionContext(final String workflowId, final String sourcePropertyName)
  {
    super(workflowId, sourcePropertyName);
  }
}

