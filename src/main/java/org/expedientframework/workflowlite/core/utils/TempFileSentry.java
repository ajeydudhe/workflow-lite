/********************************************************************
 * File Name:    TempFileSentry.java
 *
 * Date Created: Sep 17, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.expedientframework.workflowlite.core.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
/**
 * A wrapper to created a temp file and then delete once it is out of scope.
 * 
 * @author Ajey_Dudhe
 *
 */
public class TempFileSentry implements Closeable
{
  public TempFileSentry(final String prefix, final String suffix)
  {
    try
    {
      this.tempFile = File.createTempFile(prefix, suffix);
      
      LOGGER.debug("Created temp file: {}", this.tempFile.getPath());
    }
    catch (IOException e)
    {
      LOGGER.error("An error occurred while creating temp file.", e);
      throw new RuntimeException(e); // TODO: Ajey - Throw custom exception !!!
    }
  }
  
  public File get()
  {
    return this.tempFile;
  }
  
  @Override
  public void close()
  {
    if(this.tempFile != null &&  this.tempFile.exists())
    {
      this.tempFile.delete();

      LOGGER.debug("Deleted temp file: {}", this.tempFile.getPath());
      
      this.tempFile = null;
    }
  }
  
  // Private
  private File tempFile;
  private static final Logger LOGGER = LoggerFactory.getLogger(TempFileSentry.class);
}

