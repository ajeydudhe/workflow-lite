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
   	
The **_ReverseStringActivity_** class extends the **_AbstractActivity_** class with in turn implements the **_Activity_** interface. The class takes the value to be operated upon as input. It implements the **_execute()_** method returning the reverse of the input string.

Similarly, we will have the **_AlternateCaseActivity_** implemented.

### Workflow definition xml template
Create a bean xml in the project as **_src/main/resources/workflows/simple_workflow.xml_** folder with following settings:

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

### Workflow definition xml
Following xml snippet defines the workflow to execute the **_ReverseStringActivity_** followed by **_AlternateCaseActivity_**. 

```xml
<wf:workflow id="simpleWorkflow">
	<wf:activities>
		<wf:activity class="my.poc.workflow.ReverseStringActivity">
			<constructor-arg value="%{source}" />
		</wf:activity>
		<wf:activity class="my.poc.workflow.AlternateCaseActivity">
			<constructor-arg value="%{output}" />
		</wf:activity>
	</wf:activities>
</wf:workflow>
```

Notice, that in **_ReverseStringActivity_** constructor we are injecting the value using Spring Expression which is marked using custom prefix of **_%{_**. The variable **_source_** in the expression refers to the original input provided while executing the workflow. Similarly, the constructor for **_AlternateCaseActivity_** has it's value inject using custom expression. However, instead of **_source_** we have used **_output_** variable which refers to the output from previous activity which in this case is **_ReverseStringActivity_**.

### Bean xml
Add following bean xml at **_src/main/resources/workflow_beans.xml_**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<import resource="spring-beans/workflow-lite-core.xml"/>
	
	<bean id="workflowDefinitions" class="org.workflowlite.core.WorkflowXmlsProvider">
		<constructor-arg>
			<list>
				<value>classpath:workflows/*.xml</value>
			</list>
		</constructor-arg>
	</bean>
	
</beans>
```

In above bean xml the **_import_** tag tells to load the predefined bean xml **_spring-beans/workflow-lite-core.xml_** in the workflow-lite library jar.

The bean for **_org.workflowlite.core.WorkflowXmlsProvider_** class tell the library to search for the workflow definitions under the **_workflow/*_** folder on the classpath.

### Executing the workflow
Create a simple JUnit test which loads the bean xml defined above and inject the **_WorkflowManager_** instance.
Following simple test method verifies the workflow:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:workflow_beans.xml")
public class SimpleWorkflowTest
{

  @Test
  public void testSimpleWorkflow()
  {
    String output = this.workflowManager.execute(new DefaultExecutionContext("simpleWorkflow"), "abcdef");
    assertThat(output).as("Workflow output").isEqualTo("fEdCbA");

    output = this.workflowManager.execute(new DefaultExecutionContext("simpleWorkflow"), output);
    assertThat(output).as("Workflow output").isEqualTo("aBcDeF");
  }
  
  // Private
  @Inject
  private WorkflowManager workflowManager;
}
```

In the first call we are passing the input as _abcdef_. The **_ReverseStringActivity_** will convert this to _fedcba_ while the next **_AlternateCaseActivity_** will convert this to alternate case as _fEdCbA_. Similarly, we give the output of previous test as input to the workflow and get the original string _abcdef_ but with alternate case as _aBcDeF_. 
 	