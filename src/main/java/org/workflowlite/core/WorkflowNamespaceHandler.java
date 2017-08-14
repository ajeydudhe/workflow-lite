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

package org.workflowlite.core;

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
    registerBeanDefinitionParser("workflow", new BeanDefinitionParser()
    {      
      @Override
      public BeanDefinition parse(Element element, ParserContext parserContext)
      {
        return null;
      }
    });
  }
 }

