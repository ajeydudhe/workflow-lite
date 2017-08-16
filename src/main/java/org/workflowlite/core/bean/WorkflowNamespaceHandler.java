/********************************************************************
 * File Name:    WorkflowNamespaceHandler.java
 *
 * Date Created: Aug 15, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class WorkflowNamespaceHandler extends NamespaceHandlerSupport
{
  @Override
  public void init()
  {
    registerBeanDefinitionParser("workflow", new WorkflowBeanDefinitionParser());
  }
  
  // Private
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowNamespaceHandler.class);
 }

