/********************************************************************
 * File Name:    WorkflowXmlToBeanXmlTransformer.java
 *
 * Date Created: Aug 17, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.workflowlite.core.Workflow;
import org.workflowlite.core.bean.activity.ActionableActivityBean;
import org.workflowlite.core.bean.activity.ConditionalActivityBean;
import org.workflowlite.core.utils.xml.XmlElementBuilder;
import org.xml.sax.InputSource;
  
/**
 * Internal class to parse the workflow definition xml and convert to bean definitions.
 * 
 * @author Ajey_Dudhe
 *
 */
class WorkflowXmlToBeanXmlTransformer
{
  public WorkflowXmlToBeanXmlTransformer(final InputStream inputStream)
  {
    this.inputStream = inputStream;
  }
  
  public String getTransformedXml()
  {
    try(InputStream stream = this.inputStream)
    {
      final DocumentLoader docLoader = new DefaultDocumentLoader();
      final Document document = docLoader.loadDocument(new InputSource(inputStream), null, null, XmlValidationModeDetector.VALIDATION_XSD, true);
      
      LOGGER.debug("B4 transforming workflow defintion xml: {}{}", System.lineSeparator(), getXml(document));
      
      final NodeList workflows = document.getElementsByTagNameNS(NAMESPACE_CORE, "workflow");
      
      LOGGER.info("Total Workflow nodes = {}", workflows.getLength());
      
      for (int nIndex = workflows.getLength() - 1; nIndex >= 0; nIndex--) // Iterating from back since we will be removing the elements after processing
      {
        final Element workflowNode = (Element) workflows.item(nIndex);
        
        createWorkflowBeanDefinition(document, workflowNode);
        
        document.renameNode(workflowNode, null, "bean");
      }
      
      // Hack !!!
      final String workflowDefinitionXml = getXml(document).replace("xmlns=\"\"", "");
      
      LOGGER.debug("After transforming workflow definition xml: {}{}", System.lineSeparator(), workflowDefinitionXml);
      
      return workflowDefinitionXml;
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while processing workflow definition.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
  
  private void createWorkflowBeanDefinition(final Document document, final Element workflowNode)
  {
    // Set the class and scope
    workflowNode.setAttribute("class", Workflow.class.getName());
    workflowNode.setAttribute("scope", "prototype");
    
    XmlElementBuilder.element("constructor-arg", document)
                     .attribute("name", "name")
                     .attribute("value", workflowNode.getAttribute("id"))
                     .parent(workflowNode);

    XmlElementBuilder.element("constructor-arg", document)
                     .attribute("name", "activities")
                     .child(getActivities(document, workflowNode))
                     .parent(workflowNode);
  }

  private Element getActivities(final Document document, final Element parentNode)
  {
    // We should have only one <activities> element in <workflow> which is driven by the xsd
    final Element activitiesElement = (Element) parentNode.getElementsByTagNameNS(NAMESPACE_CORE, "activities").item(0);
    
    final NodeList activities = activitiesElement.getChildNodes();
    for (int nIndex = activities.getLength() - 1; nIndex >= 0; --nIndex)
    {
      final Node activityNode = activities.item(nIndex);
      if(activityNode.getNodeType() != Node.ELEMENT_NODE || !(activityNode instanceof Element))
         continue;
      
      final Element activityElement = (Element) activityNode;

      if(activityElement.getLocalName().equalsIgnoreCase("activity"))
      {
        addActionableActivityBean(document, (Element) activityElement);      
        continue;
      }
      
      if(activityElement.getLocalName().equalsIgnoreCase("switch"))
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
   
    XmlElementBuilder.element("constructor-arg", document)
                     .attribute("name", "activityBeanId")
                     .attribute("value", activityElement.getAttribute("id"))
                     .parent(actionableActivityBean);
    
    XmlElementBuilder.element("constructor-arg", document)
                     .attribute("name", "beanInstantiator")
                     .attribute("ref", "beanInstantiator")
                     .parent(actionableActivityBean);
    
    activityElement.getParentNode().insertBefore(actionableActivityBean, activityElement); // Maintain the order since we are enumerating the activities from last to first.

    activityElement.setAttribute("scope", "prototype"); // Scope should always be prototype because we have expression to be evaluated agains the context/source/output
    
    document.renameNode(activityElement, null, "bean"); // Rename the activity element to bean
        
    document.getFirstChild().appendChild(activityElement);
  }
  
  private void addConditionalActivityBean(final Document document, final Element switchElement)
  {
    final Element conditionalActivityBean = createActivityBeanElement(document, switchElement, ConditionalActivityBean.class);

    final Element mapElement = XmlElementBuilder.element("map", document)
                                                .attribute("key-type", String.class.getName())
                                                .attribute("value-type", Object.class.getName())
                                                .build();
    
    XmlElementBuilder.element("entry", document)
                     .attribute("key", "condition")
                     .attribute("value", switchElement.getAttribute("on"))
                     .parent(mapElement);
    
    final NodeList whenNodes = switchElement.getElementsByTagNameNS(NAMESPACE_CORE, "when");
    
    LOGGER.info("No. of 'when' statements = {}", whenNodes.getLength());
    
    for (int nIndex = 0; nIndex < whenNodes.getLength(); ++nIndex)
    {
      final Element whenElement = (Element) whenNodes.item(nIndex);

      XmlElementBuilder.element("entry", document)
                       .attribute("key", whenElement.getAttribute("value"))
                       .child(getActivities(document, whenElement))
                       .parent(mapElement);
    }

    final NodeList defaultNodes = switchElement.getElementsByTagNameNS(NAMESPACE_CORE, "default");
    
    LOGGER.info("No. of default statements = {}", defaultNodes.getLength());
    if(defaultNodes.getLength() > 0)
    {
      final Element defaultElement = (Element) defaultNodes.item(0); // We should have single default tag as per the xsd

      XmlElementBuilder.element("entry", document)
                       .attribute("key", "default")
                       .child(getActivities(document, defaultElement))
                       .parent(mapElement);
    }
    
    XmlElementBuilder.element("constructor-arg", document)
                     .attribute("name", "switchStatementAsMap")
                     .child(mapElement)
                     .parent(conditionalActivityBean);
    
    switchElement.getParentNode().replaceChild(conditionalActivityBean, switchElement);
  }

  private Element createActivityBeanElement(final Document document, final Element activityElement, final Class<?> activityClass)
  {
    if(!activityElement.hasAttribute("id"))
    {
      throw new IllegalArgumentException("Workflow activity should have unique 'id' attribute defined."); // TODO: Ajey - Revisit !!!
    }
    
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
  private final InputStream inputStream;
  private static final String NAMESPACE_CORE = "http://www.workflowlite.org/schema/core"; 
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowXmlToBeanXmlTransformer.class);
}