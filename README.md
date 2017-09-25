# Workflow-lite - Simple workflow engine using Spring framework and Spring Expression Language

**NOTE:** Work in progress. Need to update the readme as per the new implementation which uses UML activity diagram to define workflow !!!

**Workflow-lite** is a simple workflow engine using the Spring framework. As of now, it can be used to define a simple sequential workflow.

* A workflow consists of Actions to be executed in the given order.
* An action is a class performing a unit of work.
* An action can be defined as normal Spring bean with required dependencies injected.
* Apart from this the output of one action can be injected into the other action.
* Supports conditional flow.
* Supports asynchronous execution.
* Using the [Spring Expression Language](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html) one can inject original source, output of previous action, properties from execution context etc. into the action to be instantiated.
* The workflow can be defined using UML2 activity diagram.

## How is it different?
There are few blogs on how to use Spring to have a simple sequential workflow. But they mostly deal with sequential action execution without support for conditional branching. Also, in most cases, the interface for performing action takes some context which is used to pass the inputs from one action to another which makes the actions interdependent.

**Workflow-lite** on the other hand allows to define the actions as normal Java classes defining their dependencies to be injected using constructor or properties. Even the output of one action can be passed to another using dependency injection and not using the context object.  

## Prerequisites
* [Maven](https://maven.apache.org/) for building the project.
* [Papyrus](https://eclipse.org/papyrus/) eclipse plug-in for defining the workflow using UML activity diagram. 

## Adding the library reference
Currently, the library needs to be built manually.

* Download the source.
* Make sure you have [maven](https://maven.apache.org/) installed.
* Build the project using the command: *mvn install*
* Create a new maven project and add following dependency
  	```xml
  	<dependency>
  		<groupId>org.workflowlite</groupId>
  		<artifactId>workflow-lite-core</artifactId>
  		<version>0.0.1-SNAPSHOT</version>
  	</dependency>
  	```

## Use case - Student score card preparation
We will define a workflow to calculate the score for a given student. The workflow will take a student object as input and have following actions:

* CalculateTotalScoreAction - Takes map of subject to marks as input and returns total of all the marks.
* AddBonusMarksAction - Takes the total score for a student and adds 10 bonus marks.
* PublishStudentScoreAction - Takes student name and total score as input and returns a simple string describing the score e.g. Student 'John Doe' scored 130 marks.

### Defining the workflow using UML activity diagram
Using the Papyrus plugin create the activity diagram as follow:

![Workflow](images/student_score_card_workflow.png)

* Add the _Opaque_ action node to represent the workflow actions.
* Add the _Decision_ node to represent the condition. Since Papyrus does not show the name of condition use the comment to call out the condition.

### Implementing the actions
All the workflow actions needs to implement the [Action](src/main/java/org/workflowlite/core/Action.java) interface. Also, instead of directly implementing the interface consider extending the [AbstractAction](src/main/java/org/workflowlite/core/AbstractAction.java) or [AbstractAsyncAction](src/main/java/org/workflowlite/core/AbstractAsyncAction.java) as follows:

**TBD:** Add code snippet !!!

As seen above, the [PublishStudentScoreAction](src/test/java/org/workflowlite/core/samples/PublishStudentScoreAction.java) simply takes the student name and score as constructor parameters and then in *execute()* returns a simple formatted string. Note that we are not using [ExecutionContext](src/main/java/org/workflowlite/core/ExecutionContext.java) object to pass parameters to actions but using constructor injection. Similarly, implement other actions.

### Linking the UML activity diagram with implementation
So far we have created UML activity diagram describing the workflow we need to execute and implemented the actions. But how do we link them? This is also a very simple steps. We will use String dependency injection here and define the workflow actions as beans.

* Go to the activity diagram and select an **Opaque** node.
* In the properties view click the *UML* tab.
* Select *Add* option on *Language*.
* Select *JAVA* as language and click Ok.
* On the right hand side paste the Spring bean definition for the class. For example, following bean is for [CalculateTotalScoreAction](src/test/java/org/workflowlite/core/samples/CalculateTotalScoreAction.java) action.
	```xml
	<bean class="org.workflowlite.core.samples.CalculateTotalScoreAction">
		<constructor-arg value="%{student.scores}" />
	</bean>
	```

In above bean definition we have used Spring Expression Language to pass the input. Our expression definition starts with **%{** and ends with **}**. For this example, we are passing the value of property *stores* on the student object. The *student* object is our input to the workflow. In our [StudentWorkflowExecutionContext](src/test/java/org/workflowlite/core/samples/StudentWorkflowExecutionContext.java) we have mentioned that the input to the workflow should be refered as *student* in the expressions. Also, there are *context* and *output* variables available. **context** refers to the [ExecutionContext](src/main/java/org/workflowlite/core/ExecutionContext.java) instance while **output** refers to the output from previous action.

**Defining the condition**

To define the conditional flow we will again use the Spring expression.

* Go to the activity diagram and select an **Decision** node.
* Here, we will put the Spring expression as name of the node.
* For this example, we use the expression as *%{context.completedExtracurricularActivities(student.getName())}*
* Above expression states that invoke the *completedExtracurricularActivities* method on the *context* instance passing in the student name.
* The Spring expression result is then used to determine which flow to execute.
* For this to happen, we need to select the outgoing links and name them as per the expected output from expression.
* In our example, the outgoing links from decision node has name as *true* and *false* since our expression returns these values. Note that we will always do toString() on the expression result to match the outgoing link names. 

## Asynchronous execution
In most of the cases an action will perform some asynchronous operation or will wait on some other asynchronous operation to complete. Hence, the overall workflow execution itself needs to be asynchronous. Handling this is very easy. The action needs to return a [CompletableFuture<T>](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html) and that's all.

## Work in progress
* Optimize the expression evaluation by caching the expressions.
* Error handling.
* Persistence support for workflows.
 	