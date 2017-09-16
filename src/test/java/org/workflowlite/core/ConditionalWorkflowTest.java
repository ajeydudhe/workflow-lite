/********************************************************************
 * File Name:    ConditionalWorkflowTest.java
 *
 * Date Created: Aug 20, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import static org.assertj.core.api.Assertions.*;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
  
@ContextConfiguration(locations="classpath:wf_definitions.xml")
public class ConditionalWorkflowTest extends AbstractTestNGSpringContextTests
{
  @Test
  public void singleCondition_conditionGetsEvaluatedToTrue()
  {    
    final String result = (String) this.workflowManager.execute(new DefaultExecutionContext("ConditionalWorkflowSingleConditionWithTrueCaseHandling"), "abc_123_xyz");
    assertThat(result).as("Result").isEqualTo("ABC_TRUE_XYZ");
  }

  @Test(expectedExceptions=RuntimeException.class, expectedExceptionsMessageRegExp="Could not find action to be executed on given condition.")
  public void singleCondition_conditionGetsEvaluatedToFalse_ThrowsException()
  {
    this.workflowManager.execute(new DefaultExecutionContext("ConditionalWorkflowSingleConditionWithTrueCaseHandling"), "abc_xyz");
  }

  @Test
  public void singleCondition_conditionGetsEvaluatedToFalse()
  {
    // True
    String result = this.workflowManager.execute(new DefaultExecutionContext("ConditionalWorkflowSingleConditionWithTrueAndFalseCaseHandling"), "abc_123_xyz");
    assertThat(result).as("Result").isEqualTo("ABC_TRUE_XYZ");

    // False
    result = this.workflowManager.execute(new DefaultExecutionContext("ConditionalWorkflowSingleConditionWithTrueAndFalseCaseHandling"), "abc_xyz");
    assertThat(result).as("Result").isEqualTo("ABC_XYZ_FALSE");
  }

  @Test(invocationCount=20, threadPoolSize=5)
  public void singleCondition_ParallelExecution_conditionGetsEvaluatedToFalse()
  {
    singleCondition_conditionGetsEvaluatedToFalse();
  }
  
  // Private
  @Inject
  private WorkflowManager workflowManager;
}

