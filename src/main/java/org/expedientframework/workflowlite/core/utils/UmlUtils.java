/********************************************************************
 * File Name:    UmlUtils.java
 *
 * Date Created: Sep 16, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.utils;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.NamedElement;
  
/**
 * DO NOT USE. FOR INTERNAL PURPOSE ONLY !
 * 
 * @author Ajey_Dudhe
 *
 */
public class UmlUtils
{
  public static String createBeanId(final NamedElement umlNode)
  {
    return umlNode.getQualifiedName() + ((XMLResource)umlNode.eResource()).getID(umlNode);
  }
}

