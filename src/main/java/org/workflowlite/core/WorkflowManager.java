/********************************************************************
 * File Name:    WorkflowManager.java
 *
 * Date Created: Aug 6, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.workflowlite.core.bean.BeanInstantiator;
import org.workflowlite.core.bean.WorkflowDefinitionRepository;
import org.workflowlite.core.utils.LockSentry;
import org.workflowlite.core.utils.ReaderWriterLock;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class WorkflowManager
{
  public WorkflowManager(final BeanInstantiator beanInstantiator, final WorkflowDefinitionRepository repository)
  {
    this.beanInstantiator = beanInstantiator;
    this.repository = repository;
  }
  
  public Object execute(final String workflowId, final Object source)
  {
    try(LockSentry lock = this.lock.readLock())
    {
      LOGGER.info("Executing workflow with bean id [{}]", workflowId);
      
      final Workflow workflow = this.beanInstantiator.getWorkflow(workflowId);
      
      final ExecutionContext context = createExecutionContext(workflowId);
      
      return workflow.execute(context, source);
    }
  }

  public void loadWorkflowDefinitions(final String workflowDefinitionXmlPath)
  {
    try(LockSentry lock = this.lock.writeLock())
    {
      this.repository.load(workflowDefinitionXmlPath);
    }
  }

  // Private
  
  private ExecutionContext createExecutionContext(final String workflowId)
  {
    return new ExecutionContext()
    {     
      @Override
      public String getWorkflowId()
      {
        return workflowId;
      }

      @Override
      public <T> void setValue(final String property, final T value)
      {
        this.properties.put(property, value);
      }
      
      @SuppressWarnings("unchecked")
      @Override
      public <T> T getValue(final String property)
      {
        return (T) this.properties.get(property);
      }
      
      // Private
      private final Map<String, Object> properties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    };
  }

  // Private
  private BeanInstantiator beanInstantiator;  
  private WorkflowDefinitionRepository repository;
  private final ReaderWriterLock lock = new ReaderWriterLock();
  
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowManager.class);
}

