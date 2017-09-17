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
public class AsyncActionTest extends AbstractTestNGSpringContextTests 
{
  @Test
  public void singleActionExecution_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("AsyncSimpleWorkflowWithSingleAction"), "abcd");
    final String result = future.get();
    assertThat(result).as("Async Result").isEqualTo("DCBA");
  }
  
  @Test
  public void twoActionExecution_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("AsyncSimpleWorkflowWithTwoActions"), "abcd");
    
    final String result = future.get();
    
    assertThat(result).as("Async Result").isEqualTo("dCbA");
  }

  @Test
  public void mixedActionExecution_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("AsyncSimpleWorkflowWithBothSyncAndAsyncActions"), "abcd");
    
    final String result = future.get();
    
    assertThat(result).as("Async Result").isEqualTo("aBcDxYz");
  }

  @Test(expectedExceptions=ExecutionException.class, expectedExceptionsMessageRegExp="java.lang.RuntimeException: Dummy exception thrown from AsyncReverseStringAction.")
  public void mixedActionExecution_ThrowsExecutionException() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("AsyncSimpleWorkflowWithBothSyncAndAsyncActionsThrowsExecutionException"), "abcd");
    
    future.get();
  }

  @Test(expectedExceptions=CancellationException.class)
  public void mixedActionExecution_ThrowsCancellationException() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("AsyncSimpleWorkflowWithBothSyncAndAsyncActionsThrowsCancellationException"), "abcd");
    
    future.get();
  }
  
  @Test(invocationCount=20, threadPoolSize=5)
  public void mixedActionExecution_multipleThreads_returnsCompletableFuture() throws InterruptedException, ExecutionException
  {    
    final CompletableFuture<String> future = workflowManager.execute(new DefaultExecutionContext("AsyncSimpleWorkflowWithBothSyncAndAsyncActions"), "abcd");
    
    final String result = future.get();
    
    assertThat(result).as("Async Result").isEqualTo("aBcDxYz");
  }

  // Private
  @Inject
  private WorkflowManager workflowManager;
}
