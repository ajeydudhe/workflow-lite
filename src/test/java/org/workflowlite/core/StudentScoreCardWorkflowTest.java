package org.workflowlite.core;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.workflowlite.core.samples.Student;
import org.workflowlite.core.samples.StudentWorkflowExecutionContext;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(locations="classpath:wf_definitions.xml")
public class StudentScoreCardWorkflowTest extends AbstractTestNGSpringContextTests 
{
  @Test
  public void resultForNormalStudent_resultWithoutBonusMarks() 
  {
    final Student student = new Student("John Doe");
    student.addScore("History", 60);
    student.addScore("Science", 70);
    
    final String result = this.workflowManager.execute(new StudentWorkflowExecutionContext(), student);
    assertThat(result).as("Result").isEqualTo("Student 'John Doe' scored 130 marks.");
  }

  @Test
  public void resultForNormalStudent_resultWithBonusMarks() 
  {
    final Student student = new Student("Octavia Wilford");
    student.addScore("History", 60);
    student.addScore("Science", 70);
    
    final String result = this.workflowManager.execute(new StudentWorkflowExecutionContext(), student);
    assertThat(result).as("Result").isEqualTo("Student 'Octavia Wilford' scored 140 marks.");
  }
  
  // Private
  @Inject
  private WorkflowManager workflowManager;
}
