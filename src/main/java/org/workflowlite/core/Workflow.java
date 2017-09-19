/********************************************************************
 * File Name:    Workflow.java
 *
 * Date Created: Aug 5, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.concurrent.CompletableFuture;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.FinalNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.workflowlite.core.utils.UmlUtils;
  
/**
 * Workflow class responsible for executing the activities in given sequence.
 * It also takes care of conditional statement execution.
 * <br/>
 * <b>Note: </b>There is one instance of this class per workflow definition and hence the actual execution is wrapped up in {@link WorkflowExecutor}.
 * @author Ajey_Dudhe
 *
 */
public final class Workflow
{
  private Workflow(final Activity umlActivity, final BeanInstantiator beanInstantiator)
  {
    this.umlActivity = umlActivity;
    this.beanInstantiator = beanInstantiator;
  }
  
  public String getName()
  {
    return this.umlActivity.getName();
  }
  
  public Object execute(final ExecutionContext context, final Object source)
  {
    return new WorkflowExecutor().execute(context, source);
  }

  private class WorkflowExecutor
  {
    public Object execute(final ExecutionContext context, final Object source)
    {
      final InitialNode initialNode = (InitialNode) EcoreUtil.getObjectByType(umlActivity.getNodes(), UMLPackage.Literals.INITIAL_NODE);
      
      // TODO: Ajey - Handle infinite loops. Consumer should take care of such situations but good to guards against it here. But might be diffcult and will need higher value of iterations or configurable value to determine if we have infinite loop. 
      return executeAll(context, source, getNextNode(initialNode));
    }

    private Object executeAll(final ExecutionContext context, final Object source, ActivityNode nextNode)
    {
      while(! (nextNode instanceof FinalNode) )
      {
        nextNode = execute(context, source, nextNode);
        
        // Should we define an interface instead for async activity?
        // If CompletableFuture is expected output of one activity then it will be an issue. But this should be rare and not a good practice.
        if(this.output instanceof CompletableFuture)
        {      
          return handleAsyncResult(context, source, nextNode);
        }
      }
      
      LOGGER.info("Done executing all the activities in the workflow. Had async activities [{}].", this.future != null);
      
      // If there was async activity in between then we have returned this.future. So complete it with the last known output.
      if(this.future != null)
      {
        this.future.complete(this.output);
      }
      
      return this.output;
    }

    private ActivityNode execute(final ExecutionContext context, final Object source, final ActivityNode activityNode)
    {
      if(activityNode instanceof OpaqueAction)
      {
        return execute(context, source, (OpaqueAction)activityNode);
      }
      
      if(activityNode instanceof FinalNode)
      {
        return activityNode;
      }

      if(activityNode instanceof DecisionNode)
      {
        return execute(context, source, (DecisionNode)activityNode);
      }

      if(activityNode instanceof MergeNode)
      {
        return execute(context, source, getNextNode(activityNode));
      }

      throw new IllegalArgumentException(String.format("Uml '%s' is not supported.", activityNode.eClass().getName()));
    }

    private ActivityNode execute(final ExecutionContext context, final Object source, final OpaqueAction umlAction)
    {
      final Action<ExecutionContext, Object> actionImplementation = beanInstantiator.getAction(UmlUtils.createBeanId(umlAction), 
                                                                                               context, 
                                                                                               source, 
                                                                                               this.output);
      this.output = actionImplementation.execute(context);
      
      return getNextNode(umlAction);
    }

    private ActivityNode execute(final ExecutionContext context, final Object source, final DecisionNode umlDecision)
    {
      final Object conclusion = beanInstantiator.evaluateExpression(umlDecision.getName(), context, source, this.output);

      LOGGER.info("Condition [{}] evaluated to [{}]", umlDecision.getName(), conclusion);
      
      for (ActivityEdge whenCondition : umlDecision.getOutgoings())
      {
        if( ! StringUtils.hasText(whenCondition.getName()))
        {
          throw new RuntimeException("Outgoing edge from DecisionNode should have values defined."); // TODO: Ajey - Throw custom exception !!!
        }
        
        if(whenCondition.getName().equalsIgnoreCase(conclusion.toString()))
        {
          return whenCondition.getTarget();
        }
      }
      
      throw new RuntimeException("Could not find action to be executed on given condition."); // TODO: Ajey - Throw custom exception !!!
    }
    
    @SuppressWarnings("unchecked")
    private Object handleAsyncResult(final ExecutionContext context, final Object source, final ActivityNode nextNode)
    {
      if(this.future == null)
      {
        this.future = new CompletableFuture<Object>();
      }
      
      final CompletableFuture<Object> futureResult = (CompletableFuture<Object>) this.output;
      futureResult.whenComplete((asyncResult, exception) -> {
        
        if(exception != null)
        {
          LOGGER.error("Async activity threw exception", exception);
          this.future.completeExceptionally(exception);
          return;
        }
        
        LOGGER.info("Async activity returned: {}", asyncResult);
        
        this.output = asyncResult;
        
        executeAll(context, source, nextNode);
      });
      
      return this.future; // If this was the first async activity then this.future is returned back else the returned value is ignored because we are calling execute() from completion of async activity above.
    }

    private ActivityNode getNextNode(final ActivityNode umlNode)
    {
      if(umlNode.getOutgoings().isEmpty())
      {
        throw new IllegalArgumentException(String.format("Uml node '%s' (%s) does not point to next node.", umlNode.getName(), umlNode.eClass().getName())); // TODO: Ajey - Throw custom exception !!!
      }
      
      return umlNode.getOutgoings().get(0).getTarget();
    }

    // Private
    private CompletableFuture<Object> future = null;
    private Object output = null;
  }
  
  // Private
  private final Activity umlActivity;
  private final BeanInstantiator beanInstantiator;
  private static final Logger LOGGER = LoggerFactory.getLogger(Workflow.class);
}

