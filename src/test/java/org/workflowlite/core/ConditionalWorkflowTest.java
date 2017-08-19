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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
  
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:wf_definitions.xml")
public class ConditionalWorkflowTest
{
  @Test
  public void singleCondition_conditionGetsEvaluatedToTrue()
  {
    final String result = (String) this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueCaseHandling", "abc_123_xyz");
    assertThat(result).as("Result").isEqualTo("ABC_TRUE_XYZ");
  }

  @Test
  public void singleCondition_conditionGetsEvaluatedToFalse_ThrowsException()
  {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("Could not find activities to be executed on given condition.");
    
    this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueCaseHandling", "abc_xyz");
  }

  @Test
  public void singleCondition_conditionGetsEvaluatedToFalse()
  {
    // True
    String result = (String) this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueAndFalseCaseHandling", "abc_123_xyz");
    assertThat(result).as("Result").isEqualTo("ABC_TRUE_XYZ");

    // False
    result = (String) this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueAndFalseCaseHandling", "abc_xyz");
    assertThat(result).as("Result").isEqualTo("ABC_XYZ_FALSE");
  }

  @Test
  public void singleCondition_conditionGetsEvaluatedToDefault()
  {
    // True
    String result = (String) this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueFalseAndDefaultCaseHandling", "abc");
    assertThat(result).as("Result").isEqualTo("ABC_TRUE");    

    // False
    result = (String) this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueFalseAndDefaultCaseHandling", "xyz");
    assertThat(result).as("Result").isEqualTo("XYZ_FALSE");    

    // False
    result = (String) this.workflowManager.execute("conditionalWorkflowSingleConditionWithTrueFalseAndDefaultCaseHandling", "pqr");
    assertThat(result).as("Result").isEqualTo("PQR_DEFAULT");    
  }
  
  // Private
  @Inject
  private WorkflowManager workflowManager;
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
}

