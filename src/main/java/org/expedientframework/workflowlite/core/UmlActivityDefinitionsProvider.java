/********************************************************************
 * File Name:    UmlActivityDefinitionsProvider.java
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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
  
/**
 * This class scans the provided file paths for Uml activity diagram definitions.
 * It reads the files and return them as list of streams.
 * 
 * @author Ajey_Dudhe
 *
 */
public class UmlActivityDefinitionsProvider implements WorkflowDefinitionsProvider
{
  /**
   * Constructor taking in the Uml deinition file paths.
   * 
   * @param umlFilePaths List of file paths.
   */
  public UmlActivityDefinitionsProvider(final List<String> umlFilePaths)
  {
    this.umlFilePaths = umlFilePaths;
  }
  
  @Override
  public List<InputStream> getDefinitions()
  {
    try
    {
      final List<InputStream> definitionStreams = new ArrayList<>();
      final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

      for (String xmlPath : this.umlFilePaths)
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
  private final List<String> umlFilePaths;
  private static final Logger LOGGER = LoggerFactory.getLogger(UmlActivityDefinitionsProvider.class);
}

