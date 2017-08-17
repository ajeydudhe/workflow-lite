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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.workflowlite.core.Workflow;
import org.workflowlite.core.bean.activity.ActionableActivityBean;
import org.workflowlite.core.bean.activity.ConditionalActivityBean;
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

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
  {
    this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext;
  }

  private void processResource(final Resource resource)
  {
    try(InputStream inputStream = resource.getInputStream())
    {
      final DocumentLoader docLoader = new DefaultDocumentLoader();
      final Document document = docLoader.loadDocument(new InputSource(inputStream), null, null, XmlValidationModeDetector.VALIDATION_NONE, false); // TODO: Ajey - Setting namespaceaware to false as it is adding the xmlns attribute to some node
      
      LOGGER.info("B4 modifying: {}{}", System.lineSeparator(), getXml(document));
      
      //final NodeList workflows = document.getElementsByTagNameNS(NAMESPACE_CORE, "workflow");
      final NodeList workflows = document.getElementsByTagName("wf:workflow"); // TODO: Ajey - We should use above api to be independent of the namespace alias wf
      
      LOGGER.info("Total Workflow nodes = [{}]", workflows.getLength());
      for (int nIndex = workflows.getLength() - 1; nIndex >= 0; nIndex--)
      {
        final Element workflowNode = (Element) workflows.item(nIndex);
        
        LOGGER.error("### workflowNode = {}", workflowNode);
        
        createWorkflowBeanDefinition(document, workflowNode);
        
        //workflowNode.getParentNode().replaceChild(workflowBeanElement, workflowNode);
        final Node renamedNode = document.renameNode(workflowNode, null, "bean");
        LOGGER.error("### renamedNode = {}", renamedNode);
      }
      
      LOGGER.error("### After modifying: {}{}", System.lineSeparator(), getXml(document));

      final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanDefinitionRegistry);
      reader.setValidationMode(XmlValidationModeDetector.VALIDATION_XSD);
      final InputSource source = new InputSource(new StringReader(getXml(document)));
      reader.loadBeanDefinitions(source);
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while processing workflow definition.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
  
  private void createWorkflowBeanDefinition(final Document document, final Element workflowNode)
  {
    workflowNode.setAttribute("class", Workflow.class.getName());
    workflowNode.setAttribute("scope", "prototype");
    
    final Element constructorArgName = createConstructorArgElement(document);
    constructorArgName.setAttribute("name", "name");
    constructorArgName.setAttribute("value", workflowNode.getAttribute("id"));
    
    workflowNode.appendChild(constructorArgName);

    final Element constructorArgActivities = createConstructorArgElement(document);
    constructorArgActivities.setAttribute("name", "activities");

    final Element activities = getActivities(document, workflowNode);    
    constructorArgActivities.appendChild(activities);

    workflowNode.appendChild(constructorArgActivities);
  }

  private Element createConstructorArgElement(final Document document)
  {
    final Element constructorArgName = document.createElement("constructor-arg");
    return constructorArgName;
  }

  private Element getActivities(final Document document, final Element parentNode)
  {
    // We should have only one <activities> element in <workflow> which is driven by the xsd
    //final Element activitiesElement = (Element) parentNode.getElementsByTagNameNS(NAMESPACE_CORE, "activities").item(0);
    final Element activitiesElement = (Element) parentNode.getElementsByTagName("wf:activities").item(0); // TODO: Ajey - Use above api to be independent of namespace alias wf
    
    final NodeList activities = activitiesElement.getChildNodes();
    for (int nIndex = activities.getLength() - 1; nIndex >= 0; --nIndex)
    {
      final Node activityNode = activities.item(nIndex);
      if(activityNode.getNodeType() != Node.ELEMENT_NODE || !(activityNode instanceof Element))
         continue;
      
      final Element activityElement = (Element) activityNode;
      LOGGER.error("### Element: {}", activityElement);
      if(activityElement.getTagName().equalsIgnoreCase("wf:activity")) // TODO: Ajey - Use name without namespace
      {
        addActionableActivityBean(document, (Element) activityElement);      
        continue;
      }
      
      if(activityElement.getTagName().equalsIgnoreCase("wf:switch")) // TODO: Ajey - Use name without namespace
      {
        addConditionalActivityBean(document, (Element) activityElement);      
        continue;
      }
    }        
    document.renameNode(activitiesElement, null, "list");
    return activitiesElement;
  }

  private void addActionableActivityBean(final Document document, final Element activityElement)
  {
    final Element actionableActivityBean = createActivityBeanElement(document, activityElement, ActionableActivityBean.class);
   
    final Element constructorArgElement = createConstructorArgElement(document);
    constructorArgElement.setAttribute("name", "activityBeanId");
    constructorArgElement.setAttribute("value", activityElement.getAttribute("id"));
    
    actionableActivityBean.appendChild(constructorArgElement);
    
    //activityElement.getParentNode().appendChild(actionableActivityBean);
    activityElement.getParentNode().insertBefore(actionableActivityBean, activityElement);

    document.renameNode(activityElement, null, "bean");
        
    document.getFirstChild().appendChild(activityElement);
  }
  
  private void addConditionalActivityBean(final Document document, final Element switchElement)
  {
    final Element conditionalActivityBean = createActivityBeanElement(document, switchElement, ConditionalActivityBean.class);

    final Element mapElement = document.createElement("map");
    mapElement.setAttribute("key-type", String.class.getName());
    mapElement.setAttribute("value-type", Object.class.getName());
    
    final Element conditionEntry = document.createElement("entry");
    conditionEntry.setAttribute("key", "condition");
    conditionEntry.setAttribute("value", switchElement.getAttribute("on"));
    
    mapElement.appendChild(conditionEntry);

    final NodeList whenNodes = switchElement.getElementsByTagName("wf:when"); // TODO: Ajey - Remove namespace prefix
    
    LOGGER.info("No. of when statements = {}", whenNodes.getLength());
    
    for (int nIndex = 0; nIndex < whenNodes.getLength(); ++nIndex)
    {
      final Element whenElement = (Element) whenNodes.item(nIndex);

      final Element whenEntry = document.createElement("entry");
      whenEntry.setAttribute("key", whenElement.getAttribute("value"));
      
      whenEntry.appendChild(getActivities(document, whenElement));
      
      mapElement.appendChild(whenEntry);
    }

    final NodeList defaultNodes = switchElement.getElementsByTagName("wf:default"); // TODO: Ajey - Remove namespace prefix
    
    LOGGER.info("No. of default statements = {}", defaultNodes.getLength());
    if(defaultNodes.getLength() > 0)
    {
      final Element defaultElement = (Element) defaultNodes.item(0); // We should have single default tag as per the xsd
  
      final Element defaultEntry = document.createElement("entry");
      defaultEntry.setAttribute("key", "default");
      
      defaultEntry.appendChild(getActivities(document, defaultElement));
      
      mapElement.appendChild(defaultEntry);
    }
    
    final Element constructorElement = createConstructorArgElement(document);
    constructorElement.setAttribute("name", "switchStatementAsMap");
    constructorElement.appendChild(mapElement);
    
    conditionalActivityBean.appendChild(constructorElement);
    
    switchElement.getParentNode().replaceChild(conditionalActivityBean, switchElement);
  }

  private Element createActivityBeanElement(final Document document, final Element activityElement, final Class<?> activityClass)
  {
    // TODO: Ajey - ID should be mandatory
    final String beanId = activityElement.getAttribute("id") + "_" + activityClass.getSimpleName();
    return createBeanElement(document, beanId, activityClass);
  }  
  
  private Element createBeanElement(final Document document, final String beanId, final Class<?> beanClass)
  {
    final Element beanElement = document.createElement("bean");
   
    beanElement.setAttribute("id", beanId);
    beanElement.setAttribute("class", beanClass.getName());
    beanElement.setAttribute("scope", "prototype");
    
    return beanElement;
  }
  
  private String getXml(final Document document)
  {
    final DOMImplementationLS doc = (DOMImplementationLS) document.getImplementation();
    
    final LSSerializer writer = doc.createLSSerializer();
    writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
        
    return writer.writeToString(document);
  }
  
  // Private
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private static final String NAMESPACE_CORE = "http://www.workflowlite.org/schema/core"; 
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDefinitionRepository.class);
}

