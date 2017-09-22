# Workflow-lite - Simple workflow engine using Spring framework and Spring Expression Language

**NOTE:** Work in progress. Need to update the readme as per the new implementation which uses UML activity diagram to define workflow !!!

**Workflow-lite** is a simple workflow engine using the Spring framework. As of now, it can be used to define a simple sequential workflow. 
* A workflow consists of Actions to be executed in the given order.
* An action is a class performing a unit of work.
* An action can be defined as normal Spring bean with required dependencies injected.
* Apart from this the output of one action can be injected into the other action.
* Supports conditional flow.
* Using the [Spring Expression Language](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html) one can inject original source, output of previous action, properties from execution context etc. into the action to be instantiated.
* The workflow can be defined using UML2 activity diagram.

## How is it different?
There are few workflow implementations which use Spring framework. But they mostly deal with sequential action execution without support for conditional branching. Also, the interface for performing action takes some context which is used to pass the inputs from one action to another.
Workflow-lite on the other hand allows to define the actions as normal Java classes defining their dependencies to be injected. Even the output of one action can be passed to another using dependency injection and not using the context object.  

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

## Work in progress
* Optimize the expression evaluation by caching the expressions.
* Error handling.
* Persistence support for workflows.
 	