/********************************************************************
 * File Name:    XmlElementBuilder.java
 *
 * Date Created: Aug 19, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.utils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
  
/**
 * Simple wrapper to create element with attributes.
 * 
 * @author Ajey_Dudhe
 *
 */
public class XmlElementBuilder
{
  private XmlElementBuilder(final String elementName, final Document document)
  {
    this.element = document.createElement(elementName);
  }
  
  public static XmlElementBuilder element(final String elementName, final Document document)
  {
    return new XmlElementBuilder(elementName, document);
  }
  
  public XmlElementBuilder attribute(final String name, final String value)
  {
    this.element.setAttribute(name, value);
    return this;
  }
  
  public XmlElementBuilder parent(final Element parent)
  {
    parent.appendChild(this.element);
    
    return this;
  }

  public XmlElementBuilder child(final Element child)
  {
    this.element.appendChild(child);
    
    return this;
  }
  
  public Element build()
  {
    return this.element;
  }
  
  // Private
  private final Element element;
}

