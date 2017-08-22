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
    return new StringBuilder(this.value).reverse().toString();
  }

  // Private
  private String value;
}
```
   	
The *ReverseStringActivity* class extends the *AbstractActivity* class with in turn implements the *Activity* interface. The class takes the value to be operated upon as input. It implements the *execute()* method returning the reverse of the input string.

Similarly, we will have the *AlternateCaseActivity* implemented.

### The workflow xml template
Create a bean xml in the project as *src/main/resources/workflows/simple_workflow.xml* folder with following settings:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:wf="http://www.workflowlite.org/schema/core"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.workflowlite.org/schema/core http://www.workflowlite.org/schema/core/workflow.xsd">

</beans>
```

Notice that we have added custom namespace tag **_xmlns:wf="http://www.workflowlite.org/schema/core"_** and also the corresponding schema xsd location in specified in xsi:schemaLocation.

### The workflow definition
Following xml snippet defines the workflow to execute the **_ReverseStringActivity_** followed by **_AlternateCaseActivity_**. 

```xml
	<wf:workflow id="simpleWorkflow">
		<wf:activities>
			<wf:activity class="my.poc.workflow.ReverseStringActivity">
				<constructor-arg value="%{source}" />
			</wf:activity>
			<wf:activity class="my.poc.workflow.AlternateCaseActivity">
				<constructor-arg value="%{source}" />
			</wf:activity>
		</wf:activities>
	</wf:workflow>
```

Notice, that in **_ReverseStringActivity_** constructor we are injecting the value using Spring Expression which is makred using custom prefix of **_%{_**. The variable **_source_** in the expression refers to the original input provided while executing the workflow. Similarly, the constructor for **_AlternateCaseActivity_** has it's value inject using custom expression. However, instead of **_source_** we ave used **_output_** variable which refers to the output from previous activity which in this case is **_ReverseStringActivity_**. 	