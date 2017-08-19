/********************************************************************
 * File Name:    CustomTagsTest.java
 *
 * Date Created: Aug 15, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.workflowlite.core.bean.WorkflowDefinitionRepository;
  
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:workflows/simple_workflow.xml")
public class CustomTagsTest
{
  @Test
  public void loadCustomTags()
  {
    //this.workflowManager.loadWorkflowDefinitions("classpath:workflows/custom_workflow.xml"); 
    this.workflowManager.execute("customWorkflow", "abc");
  }
  
  @Inject
  private WorkflowManager workflowManager;
 }

