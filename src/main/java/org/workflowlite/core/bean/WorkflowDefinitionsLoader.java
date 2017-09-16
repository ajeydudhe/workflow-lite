/********************************************************************
 * File Name:    WorkflowDefinitionsLoader.java
 *
 * Date Created: Aug 15, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.core.util.IOUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.statemachine.uml.support.UmlUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.workflowlite.core.UmlActivityDefinitionsProvider;
import org.workflowlite.core.Workflow;
import org.workflowlite.core.WorkflowDefinitionsProvider;
import org.workflowlite.core.utils.xml.XmlElementBuilder;
import org.workflowlite.core.utils.xml.XmlUtils;
import org.xml.sax.InputSource;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class WorkflowDefinitionsLoader implements BeanDefinitionRegistryPostProcessor
{
  private WorkflowDefinitionsLoader()
  {    
  }
  
  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException
  {   
    this.beanFactory = beanFactory;
    
    final Map<String, ? extends UmlActivityDefinitionsProvider> definitionsProviders = beanFactory.getBeansOfType(UmlActivityDefinitionsProvider.class);
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
      final File umlFile = File.createTempFile("workflow-lite-", ".uml"); // TODO: Ajey - Delete the file
      
      LOGGER.info("Created temp file for uml definition: {}", umlFile.getPath());
      
      Files.copy(definitionStream, Paths.get(umlFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
      
      // Load the bean definitions
      final String beanDefinitionsXml = transform(umlFile.getPath());

      final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanDefinitionRegistry);
      reader.setValidationMode(XmlValidationModeDetector.VALIDATION_XSD);
      
      final InputSource source = new InputSource(new StringReader(beanDefinitionsXml));
      
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
 
  public String transform(final String umlDefinitionFile)
  {
    try
    {
      final Document beanXml = loadEmptyBeanXml();
      final Model model = UmlUtils.getModel(umlDefinitionFile);
      
      for (Activity umlActivity : EcoreUtil.<Activity>getObjectsByType(model.getPackagedElements(), UMLPackage.Literals.ACTIVITY))
      {
        LOGGER.info("Activity: {}", umlActivity);
        
        final BeanInstantiator beanInstantiator = this.beanFactory.getBean(BeanInstantiator.class);
        final Workflow workflow = (Workflow) this.beanFactory.getBean("workflow-lite-workflow-bean", umlActivity, beanInstantiator);
        this.beanFactory.registerSingleton(umlActivity.getName(), workflow);
        
        LOGGER.info("Registered workflow bean with name [{}]", umlActivity.getName());
        
        for (ActivityNode node : umlActivity.getNodes())
        {
          LOGGER.info("Node: {}", node);
          
          final boolean isActionNode = (node instanceof OpaqueAction);
          if(!isActionNode)
             continue;
          
          addBeanDefinition((OpaqueAction)node, beanXml);
        }
      }
      
      return XmlUtils.getXml(beanXml).replace("xmlns=\"\"", "");
    }
    catch (Exception e)
    {
      throw e;
    }
  }

  private void addBeanDefinition(final OpaqueAction action, final Document beans)
  {
    if(action.getBodies().isEmpty())
    {
      throw new IllegalArgumentException("The OpaqueAction node must have bean xml specified for the Spring class."); // TODO: Ajey - Throw custom exception !!!
    }
   
    try
    {
      LOGGER.info("Bean definition: {}{}", System.lineSeparator(), action.getBodies().get(0));
      
      final DocumentLoader docLoader = new DefaultDocumentLoader();
      final Document document = docLoader.loadDocument(new InputSource(new StringReader(action.getBodies().get(0))), null, null, XmlValidationModeDetector.VALIDATION_NONE, true);
      
      final Node beanNode = document.getFirstChild();
      
      XmlElementBuilder.element((Element) beanNode)
                       .attribute("scope", "prototype")
                       .attribute("id", org.workflowlite.core.uml.UmlUtils.createBeanId(action))
                       .build();
      
      beans.getFirstChild().appendChild(beans.importNode(beanNode, true));
    }
    catch(Exception e)
    {
      LOGGER.error("An error occurred while creating bean definition xml.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
  
  private static Document loadEmptyBeanXml()
  {
    try
    {
      final DocumentLoader docLoader = new DefaultDocumentLoader();
      
      return docLoader.loadDocument(new InputSource(WorkflowDefinitionsLoader.class.getResourceAsStream("/spring-beans/empty_bean.xml")), 
                                                    null, 
                                                    null, 
                                                    XmlValidationModeDetector.VALIDATION_NONE, 
                                                    true);
    }
    catch (Exception e)
    {
      LOGGER.error("An error occurred while loading empty bean xml.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }

  // Private
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private ConfigurableListableBeanFactory beanFactory;
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDefinitionsLoader.class);
}

