package org.workflowlite.core;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations="classpath:wf_definitions.xml")
public class AsyncActivityTest extends AbstractTestNGSpringContextTests 
{
  @Test
  public void singleActivityExecution_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("asyncSimpleWorkflowWithSingleActivity"), "abc");
    final String result = future.get();
    assertThat(result).as("Async Result").isEqualTo("CBA");
  }
  
  @Test
  public void twoActivityExecution_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("asyncSimpleWorkflowWithTwoActivities"), "abcd");
    
    final String result = future.get();
    
    assertThat(result).as("Async Result").isEqualTo("dCbA");
  }

  @Test
  public void mixedActivityExecution_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("asyncSimpleWorkflowWithBothSyncAndAsyncActivities"), "abcd");
    
    final String result = future.get();
    
    assertThat(result).as("Async Result").isEqualTo("aBcDxYz");
  }

  @Test(expectedExceptions=ExecutionException.class, expectedExceptionsMessageRegExp="java.lang.RuntimeException: Dummy exception thrown from AsyncReverseStringActivity.")
  public void mixedActivityExecution_ThrowsExecutionException() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("asyncSimpleWorkflowWithBothSyncAndAsyncActivitiesThrowsExecutionException"), "abcd");
    
    future.get();
  }

  @Test(expectedExceptions=CancellationException.class)
  public void mixedActivityExecution_ThrowsCancellationException() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("asyncSimpleWorkflowWithBothSyncAndAsyncActivitiesThrowsCancellationException"), "abcd");
    
    future.get();
  }
  
  @Test(invocationCount=20, threadPoolSize=5)
  public void mixedActivityExecution_multipleThreads_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("asyncSimpleWorkflowWithBothSyncAndAsyncActivities"), "abcd");
    
    final String result = future.get();
    
    assertThat(result).as("Async Result").isEqualTo("aBcDxYz");
  }

  // Private
  @Inject
  private WorkflowManager workflowManager;
}
