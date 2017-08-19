/********************************************************************
 * File Name:    WorkflowXmlsProvider.java
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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class WorkflowXmlsProvider implements WorkflowDefinitionsProvider
{
  public WorkflowXmlsProvider(final List<String> workflowXmlFilePaths)
  {
    this.workflowXmlFilePaths = workflowXmlFilePaths;
  }
  
  @Override
  public List<InputStream> getDefinitionXmls()
  {
    try
    {
      final List<InputStream> definitionStreams = new ArrayList<>();
      final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

      for (String xmlPath : this.workflowXmlFilePaths)
      {
        for (Resource resource : resolver.getResources(xmlPath))
        {
          definitionStreams.add(resource.getInputStream());
        }
      }  
      
      return definitionStreams;
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while resolving workflow definition xml paths.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
  
  // Private
  private final List<String> workflowXmlFilePaths;
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowXmlsProvider.class);
}

