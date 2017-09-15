/********************************************************************
 * File Name:    WorkflowDefinitionLoader.java
 *
 * Date Created: Aug 15, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.workflowlite.core.WorkflowDefinitionsProvider;
import org.xml.sax.InputSource;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class WorkflowDefinitionLoader implements BeanDefinitionRegistryPostProcessor
{
  private WorkflowDefinitionLoader()
  {    
  }
  
  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException
  {   
    final Map<String, ? extends WorkflowDefinitionsProvider> definitionsProviders = beanFactory.getBeansOfType(WorkflowDefinitionsProvider.class);
    if(definitionsProviders == null)
       return;
    
    definitionsProviders.values().forEach(provider -> provider.getDefinitions().forEach(stream -> loadDefinitions(stream)));
  }

  @Override
  public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException
  {
    this.beanDefinitionRegistry = registry;
  }
  
  private void loadDefinitions(final InputStream definitionStream)
  {
    try
    {
      final String workflowDefinitionXml = new WorkflowXmlToBeanXmlTransformer(definitionStream).getTransformedXml();

      final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanDefinitionRegistry);
      reader.setValidationMode(XmlValidationModeDetector.VALIDATION_XSD);
      
      final InputSource source = new InputSource(new StringReader(workflowDefinitionXml));
      
      reader.loadBeanDefinitions(source);
    }
    catch(RuntimeException e)
    {
      throw e; // Already logged. TODO: Replace with custom exception !!! 
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while processing workflow definition.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
 
  // Private
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDefinitionLoader.class);
}

