/********************************************************************
 * File Name:    WorkflowExpressionEvaluationContext.java
 *
 * Date Created: Sep 20, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class WorkflowExpressionEvaluationContext extends StandardEvaluationContext
{
  public WorkflowExpressionEvaluationContext(final ExecutionContext context, final Object source, final Object output)
  {
    this.addPropertyAccessor(new WorkflowPropertyAccessor(context, source, output));
  }
  
  private static class WorkflowPropertyAccessor implements PropertyAccessor
  {
    public WorkflowPropertyAccessor(final ExecutionContext context, final Object source, final Object output)
    {
      this.properties.put("context", context);
      this.properties.put("output", output);
      
      if(StringUtils.hasText(context.getSourcePropertyName()))
      {
        this.properties.put(context.getSourcePropertyName(), source);
      }
    }
    
    @Override
    public Class<?>[] getSpecificTargetClasses()
    {
      return null;
    }

    @Override
    public boolean canRead(final EvaluationContext context, final Object target, final String name) throws AccessException
    {
      return this.properties.containsKey(name);
    }

    @Override
    public TypedValue read(final EvaluationContext context, final Object target, final String name) throws AccessException
    {
      return new TypedValue(this.properties.get(name));
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException
    {
      return false;
    }

    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException
    {
    }
    
    // Private
    private final Map<String, Object> properties = new HashMap<>(); // TODO: Ajey - Should we use tree map to have variable names case-insensitive???
  }
}
