/********************************************************************
 * File Name:    WorkflowDefinitionRepository.java
 *
 * Date Created: Aug 15, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.xml.sax.InputSource;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class WorkflowDefinitionRepository implements ApplicationContextAware
{
  public void load(final String workflowDefinitionXmlPath)
  {
    try
    {
      final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();    
      for (Resource resource : resolver.getResources(workflowDefinitionXmlPath))
      {
        LOGGER.info("Processing resource [{}]", resource);
        
        processResource(resource);
      }
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while loading workflow definitions.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }

  private void processResource(final Resource resource)
  {
    try(InputStream inputStream = resource.getInputStream())
    {
      final DocumentLoader docLoader = new DefaultDocumentLoader();
      final Document document = docLoader.loadDocument(new InputSource(inputStream), null, null, XmlValidationModeDetector.VALIDATION_NONE, false);
      
      LOGGER.info("B4 modifying: {}{}", System.lineSeparator(), getXml(document));
      
      final NodeList workflows = document.getElementsByTagName("wf:workflow");
      
      LOGGER.info("Total Workflow nodes = [{}]", workflows.getLength());
      for (int nIndex = workflows.getLength() - 1; nIndex >= 0; nIndex--)
      {
        final Node workflowNode = workflows.item(nIndex);
        
        LOGGER.error("### workflowNode = {}", workflowNode);
        
        //final Element workflowBeanElement = createWorkflowBeanDefinition(document, workflowNode);
        
        //workflowNode.getParentNode().replaceChild(workflowBeanElement, workflowNode);
        final Node renamedNode = document.renameNode(workflowNode, null, "abc");
        LOGGER.error("### renamedNode = {}", renamedNode);
      }
      
      LOGGER.error("### After modifying: {}{}", System.lineSeparator(), getXml(document));

      /*
      final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanDefinitionRegistry);
      reader.setValidationMode(XmlValidationModeDetector.VALIDATION_XSD);
      final InputSource source = new InputSource(new StringReader(getXml(document)));
      reader.loadBeanDefinitions(source);*/
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while processing workflow definition.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
  
  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
  {
    this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext;
  }

  private String getXml(final Document document)
  {
    final DOMImplementationLS doc = (DOMImplementationLS) document.getImplementation();
    return doc.createLSSerializer().writeToString(document);
  }
  
  // Private
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDefinitionRepository.class);
}

