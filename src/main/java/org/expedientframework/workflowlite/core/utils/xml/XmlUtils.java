/********************************************************************
 * File Name:    XmlUtils.java
 *
 * Date Created: Sep 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.utils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
  
/**
 * DO NOT USE. FOR INTERNAL PURPOSE ONLY !
 * 
 * @author Ajey_Dudhe
 *
 */
public class XmlUtils
{
  public static String getXml(final Document document)
  {
    final DOMImplementationLS doc = (DOMImplementationLS) document.getImplementation();
    
    final LSSerializer writer = doc.createLSSerializer();
    writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
        
    return writer.writeToString(document);
  }
}

