# Workflow-lite - Simple workflow engine using Spring framework and Spring Expression Language

**NOTE:** Work in progress

**Workflow-lite** is a simple workflow engine using the Spring framework. As of now, it can be used to define a simple sequential workflow. 
* A workflow consists of Activities to be executed in the given order.
* An activity is a class performing a unit of work.
* An activity can be defined as normal Spring bean with required dependencies injected.
* Apart from this the output of one activity can be injected into the other activity.
* Using the [Spring Expression Language](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html) one can inject original source, output of previous activity, properties from execution context etc. into the activity to be instantiated.

## Adding the library reference
Currently, the library needs to be built manually.
* Download the source.
* Make sure you have [maven](https://maven.apache.org/) installed.
* Build the project using the command: *mvn install*
* Create a new maven project and add following dependency
