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

package org.workflowlite.core.bean;

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
import org.workflowlite.core.Activity;
import org.workflowlite.core.ExecutionContext;
import org.workflowlite.core.Workflow;
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
    // TODO: Ajey - We can remove this flag once we start creating beans from custom xml tags
    try(ThreadLocalSentry<Boolean> threadLocal = ENABLE_CUSTOM_EXPRESSION_EVALUATION.set(Boolean.FALSE))
    {
      return this.applicationContext.getBean(workflowBeanId, Workflow.class);
    }
  }
  
  public Activity getActivity(final String activityBeanId, final ExecutionContext context, final Object source, final Object output)
  {
    // Adding the root object having source property so that in the expression we can use the source property.
    try(ThreadLocalSentry<Object> threadLocalSource = EXPRESSION_EVAULATION_ROOT_OBJECT.set(createRootObject(context, source, output));
        ThreadLocalSentry<Boolean> threadLocalExpressionFlag = ENABLE_CUSTOM_EXPRESSION_EVALUATION.set(Boolean.TRUE))
    {      
      return this.applicationContext.getBean(activityBeanId, Activity.class);
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

    if(ENABLE_CUSTOM_EXPRESSION_EVALUATION.get() == Boolean.FALSE)
    {
      return expression; // Return the expression as is since we will evaluate it later
    }
    
    return this.doEvaluate(expression, EXPRESSION_EVAULATION_ROOT_OBJECT.get());
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
      
      public ExecutionContext getContext() {
        return context;
      }

      public Object getSource() {
        return source;
      }
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
  
  private static final ThreadLocalSentry<Object> EXPRESSION_EVAULATION_ROOT_OBJECT = new ThreadLocalSentry<>(null);
  private static final ThreadLocalSentry<Boolean> ENABLE_CUSTOM_EXPRESSION_EVALUATION = new ThreadLocalSentry<>(Boolean.FALSE);
  
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
  
  private static final String EXPRESSION_PREFIX = "${";
  private static final String EXPRESSION_SUFFIX = "}";
}

