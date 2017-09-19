/********************************************************************
 * File Name:    BeanInstantiator.java
 *
 * Date Created: Aug 6, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.workflowlite.core.utils.ThreadLocalSentry;
  
/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public final class BeanInstantiator implements ApplicationContextAware, BeanFactoryPostProcessor, BeanExpressionResolver
{
  private BeanInstantiator()
  {    
  }
  
  public Workflow getWorkflow(final String workflowBeanId)
  {
    return this.applicationContext.getBean(workflowBeanId, Workflow.class);
  }
  
  @SuppressWarnings("unchecked")
  public <TContext extends ExecutionContext> Action<TContext, Object> getAction(final String activityBeanId, final ExecutionContext context, final Object source, final Object output)
  {
    // Adding the root object having source property so that in the expression we can use the source property.
    try(ThreadLocalSentry<Object> threadLocalSource = EXPRESSION_EVALUATION_ROOT_OBJECT.set(createRootObject(context, source, output)))
    {      
      return this.applicationContext.getBean(activityBeanId, Action.class);
    }
  }
  
  public Object evaluateExpression(final String expression, final ExecutionContext context, final Object source, final Object output)
  {
    return this.doEvaluate(expression, createRootObject(context, source, output));
  }
  
  @Override
  public Object evaluate(final String expression, final BeanExpressionContext evalContext) throws BeansException
  {
    if(!expression.contains(EXPRESSION_PREFIX))
    {
      return this.existingExpressionResolver.evaluate(expression, evalContext);
    }

    return this.doEvaluate(expression, EXPRESSION_EVALUATION_ROOT_OBJECT.get());
  }

  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException
  {
    this.existingExpressionResolver = beanFactory.getBeanExpressionResolver();
    beanFactory.setBeanExpressionResolver(this);
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
  {
    this.applicationContext = applicationContext;
  }
  
  private static Object createRootObject(final ExecutionContext context, final Object source, final Object output)
  {
    return new Object() {
      
      @SuppressWarnings("unused")
      public ExecutionContext getContext() {
        return context;
      }

      @SuppressWarnings("unused")
      public Object getSource() {
        return source; // TODO: Ajey - Should be able to configure the variable name here instead of using source. For example, student, event etc. Use PropertyAccessor on evaluation context.
      }
      
      @SuppressWarnings("unused")
      public Object getOutput() {
        return output;
      }
    };
  }
  
  private Object doEvaluate(final String expression, final Object source)
  {
    // TODO: Ajey - Cache the expression object
    final Expression expressionObject = this.expressionParser.parseExpression(expression, PARSER_CONTEXT);
    
    return expressionObject.getValue(source);
  }

  // Private
  private ApplicationContext applicationContext;
  private BeanExpressionResolver existingExpressionResolver;
  private final SpelExpressionParser expressionParser = new SpelExpressionParser();
  
  private static final ThreadLocalSentry<Object> EXPRESSION_EVALUATION_ROOT_OBJECT = new ThreadLocalSentry<>(null);
  
  private static final ParserContext PARSER_CONTEXT = new ParserContext()
  {    
    @Override
    public boolean isTemplate()
    {
      return true;
    }
    
    @Override
    public String getExpressionPrefix()
    {
      return EXPRESSION_PREFIX;
    }

    @Override
    public String getExpressionSuffix()
    {
      return EXPRESSION_SUFFIX;
    }
  };
  
  private static final String EXPRESSION_PREFIX = "%{";
  private static final String EXPRESSION_SUFFIX = "}";
}

