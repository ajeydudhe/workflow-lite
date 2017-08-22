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
  	```xml
  	<dependency>
  		<groupId>org.workflowlite</groupId>
  		<artifactId>workflow-lite-core</artifactId>
  		<version>0.0.1-SNAPSHOT</version>
  	</dependency>
  	```

## Creating the workflow
For this example, we will create a simple workflow with following string operations:
* **ReverseStringActivity** takes a string as input and returns reverse string.
* **AlternateCaseActivity** takes a string as input and returns a string with every alternate character having opposite case e.g. input = abcd; output = aBcD

The workflow will take a string input and if the string starts with vowels then we simply reverse the string else we return the string with alternate case.

### ReverseStringActivity
The ReverseStringActivity class will be as follows:

```java
package my.poc.workflow;

import org.workflowlite.core.AbstractActivity;
import org.workflowlite.core.ExecutionContext;
  
public class ReverseStringActivity extends AbstractActivity
{
  public ReverseStringActivity(final String value)
  {
    super(ReverseStringActivity.class.getSimpleName());
    
    this.value = value;
  }

  public Object execute(final ExecutionContext context)
  {
    **return new StringBuilder(this.value).reverse().toString();**
  }

  // Private
  private String value;
}
```
   	
The *ReverseStringActivity* class extends the *AbstractActivity* class with in turn implements the *Activity* interface. The class takes the value to be operated upon as input. It implements the *execute()* method returning the reverse of the input string.

Similarly, we will have the *AlternateCaseActivity* implemented.   	