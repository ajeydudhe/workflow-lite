/********************************************************************
 * File Name:    BasicTest.java
 *
 * Date Created: Aug 5, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import static org.assertj.core.api.Assertions.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
  
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:workflows/simple_workflow.xml")
public class BasicTest
{
  @Test
  public void test()
  {
    LOGGER.error("#### Starting test...");

    this.workflowManager.execute("workflow_01", null);
    //this.workflowManager.execute("workflow_02", null);
  }
  
  // Private
  @Inject
  private WorkflowManager workflowManager;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(BasicTest.class);
}

