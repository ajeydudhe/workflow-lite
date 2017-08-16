/********************************************************************
 * File Name:    WorkflowBeanDefinitionParser.java
 *
 * Date Created: Aug 15, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.bean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class WorkflowBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser
{
  @Override
  protected String getBeanClassName(final Element element)
  {
    return element.getAttribute("org.workflowlite.core.Workflow");
  }
  
  @Override
  protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder)
  {
    super.doParse(element, parserContext, builder);
  }
}

