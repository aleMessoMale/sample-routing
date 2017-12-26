# Introduction

This sample demonstrates how you can send an HTTP request to a **[Spring Integration][]** HTTP service. This sample also uses **[Spring Security][]** for HTTP Basic authentication. With the HTTP Path facility, the client program can send requests with URL Variables.

Feed by a Rest Web Service: http://restcountries.eu/rest/v2/, the rest web service created is able to return a list of countries with their relative currency values.  
  
The solution provided is multi-version and multi-channel web application. Channel and relative service version is part of the exposed path.  

Here an example of the URL to invoke: *http://localhost:8080/rest-integration-sample/services/web/v1/currency-countries-info*   
Supported Http Verb is GET.   

Here an extract of a correct response:
```json
{
    "version": "v1",
    "channel": "web",
    "countryInfoList": [
        {
            "name": "Bouvet Island",
            "currencies": [
                {
                    "code": "NOK",
                    "name": "Norwegian krone",
                    "symbol": "kr"
                }
            ]
        },
        {
            "name": "Brazil",
            "currencies": [
                {
                    "code": "BRL",
                    "name": "Brazilian real",
                    "symbol": "R$"
                }
            ]
        },
        {
            "name": "British Indian Ocean Territory",
            "currencies": [
                {
                    "code": "USD",
                    "name": "United States dollar",
                    "symbol": "$"
                }
            ]
        }, 
        and so on...
     ]
}
```


It's also possible to receive data in a **paginated way**, passing URL Variables   

URLparams are optional. If no pagination is requested, all elements are returned.

See **[Pagination Section](https://github.com/aleMessoMale/sample-routing/#pagination "Pagination Section")** for more info.

The only **version** supported is the v1, and, as you probably already noticed, is part of the exposed path. See **[Integration Section](https://github.com/aleMessoMale/sample-routing/#integration "Integration Section")** to better understand how versioning has been managed.  

The only **channels** supported are web and mobile. See **[Integration Section](https://github.com/aleMessoMale/sample-routing/#integration "Integration Section")** to better understand how multi-channel features have been managed.

As already said, the web service is secured with **Spring Security**. Here the **credentials** to provide, with Basic Authentication, in order to consume the exposed rest web service. These are present in the [user.properties] file:  
username: INTEGRATION_REST_USER  
password: 1Password  

For more info see the **[Security Section](https://github.com/aleMessoMale/sample-routing/#security "Security Section")**.  

Unit tests and Integration tests have been created. See the **[Test Section](https://github.com/aleMessoMale/sample-routing/#test "Test Section")** for more info.

The provided project itself is a web project and it can be build using [Maven][] and the resulting war under `target/rest-integration-sample.war` can be deployed to Servlet Containers such as [Jetty][] or [Apache Tomcat][].

**Environment property segregation Section** has been obtained through the use of Spring profiles. See the **[Environment segregation Section](https://github.com/aleMessoMale/sample-routing/#environment-segregation "Environment segregation Section")** for more details.

This sample has been crated with:   
- **Spring Tool Suite**    
- Version: 3.8.4.RELEASE   
- Build Id: 201703310825  
- Platform: Eclipse Neon.3 (4.6.3)  

# To run and Test this sample

   - execute command `mvn clean verify`  from the root of the project.
   - deploy the war *rest-integration-sample.war* in the target folder, under a Servlet Container.
   - add *spring.profiles.active="dev"* to the Servlet Container as a System Variable, at the time of writing only the "dev" profile works correctly.  

A **Basic Authentication** with [Spring Security][] has been put in place. Provide then these credentials in order to consume this Rest Service:  
- username: INTEGRATION\_REST\_USER  
- password: 1Password  

For testing purpose, you can use a Rest Client as [PostMan][] and call the subsequent URL with the GET Http Verb: *http://localhost:8080/rest-integration-sample/services/web/v1/currency-countries-info?pageSize=10&pageNumber=5*  

This is an example of paginated response and returns the fifth page and the page will have a size of ten elements, calling the version v1 of the web service for the web channel.  

Pagination (and the relative URL Variables pageSize and pageNumber) is optional. If no pagination is provided, all values are returned.    


# Main features and more details

## Multi-layer project

This project is composed by these layers:  
- *Security*: A security layer has been put in place through [Spring Security]. For more info see the Spring Security configuration file [here](./src/main/webapp/WEB-INF/config/security-config.xml) or the relative [Security Section](https://github.com/aleMessoMale/sample-routing/#security "Security Section")
- *Web*: A servlet is called in order to fill the MDC Context of [LogBack] and allow a better logging experience. For more info See [here](./src/main/java/com/amazingsoftware/integration/samples/web/MDCInsertingServletFilter.java) the Servlet which fills the MDC Context, the whole [logback.xml](./src/main/resources/logback.xml) file or the specific **[Logs Section](https://github.com/aleMessoMale/sample-routing/#logs "Logs Section")**.  
- *Integration*: An Integration layer through [Spring Integration] has been put in place in order to orchestrate correctly messages received from the exposed Rest Web Service, managing different versions and channels. For more info see the Spring Configuration file for the Integration features [here](./src/main/resources/META-INF/spring/integration/application-spring-integration.xml) or the relative [Integration Section](https://github.com/aleMessoMale/sample-routing/#integration "Integration Section")  
- *Facade*: A facade layer has been put in place in order to make easier the interaction with the below service layer.  
- *Service*: A Service layer has been put in place. This layer is responsible for most of the business logic creating decoupling with the above Facade Layer.

An arch package has been written to manage most common operations such as logging, errors, mapping between layers and rest invokation.  

An extensible [Mapper](./src/main/java/com/amazingsoftware/integration/samples/arch/mapper/impl/Mapper.java) class (optimazed with Streams) has been written in order to make easier the mapping operations between domain objects of different layers so that is possible to keep different layers loosely coupled as much as possible.  

Architecture guarantees that every Response contains channel and version that has been used. 

A rest package has been created also and contains subpackages for facade and service layers and further packages are expected one for each service (now only currency exists). 

A config package has been written for configuration files, with subpackages for different environments. See the **[Environment segregation Section](https://github.com/aleMessoMale/sample-routing/#environment-segregation "Environment segregation Section")** for more details.  

An utils package has been created for contains util classes for most common operations such as collection filtering, http management, Json Serialization, Log filtering and number management.  

A web package contains so far the Servlet for the MDC Context filling, see **[Logs Section](https://github.com/aleMessoMale/sample-routing/#logs "Logs Section")** for more details.  


## Security

Rest service exposed is Basic Authentication secured through [Spring Integration].  
Here the credentials to give:  
- username: INTEGRATION\_REST\_USER  
- password: 1Password

Username and password are written in the [user.properties] file. 

A test case has been written in order to print the encoded password so that is possible to change the actual password with a different one.  
For the relative test case see [here](./src/test/java/test/com/amazingsoftware/integration/sample/rest/CreateEncodedPasswordTest.java)
 

In this example, we are using in memory authentication. The password encoder is BCryptPasswordEncoder that is considered so far the best password encoder to use.  

```xml
<security:authentication-manager alias="authenticationManager">
		<security:authentication-provider>
			<security:password-encoder ref="passwordEncoder" />
			<security:user-service properties="classpath:users.properties" />
		</security:authentication-provider>
	</security:authentication-manager>


	<!-- BCryptPasswordEncoder is considered so far the best password encoder 
		to use -->
	<bean id="passwordEncoder"
		class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder">
		<constructor-arg value="11" />
	</bean>
```

This extract has been taken from the whole spring security configuration [file](./src/main/webapp/WEB-INF/config/security-config.xml) 

The access to the service activator method is secured and the caller must be authenticated with a role of ROLE_INTEGRATION_REST_USER as shown below:

```java
@Override
	@Secured("ROLE_INTEGRATION_REST_USER")
	public GenericMessage<CurrencyFacadeResponse> getCurrenciesInfoForWeb(Message<?> inMessage) throws Exception {

		CurrencyFacadeResponse currencyFacadeResponse = this.createResponseFacade(CurrencyFacadeResponse.class, inMessage);
		
		
		Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
		
		/* Get the servlet request */
		HttpServletRequest req = httpUtils.getServletRequest();
		
		/* Get the version from the Message Header */
		String version = httpUtils.getHeaderFromIntegrationMessage(inMessage, HeaderMessageKeys.MESSAGE_HEADER_VERSION);
		...
		...
}
```

## Logs

The logging framework chosen is [LogBack][].

A Servlet has been put in place in order to fill the MDC Context at every invokation.
[See here](./src/main/java/com/amazingsoftware/integration/samples/web/MDCInsertingServletFilter.java) the relative code.

Below the main method involved: 

```java
/**
	 * Should be implemented in a stronger way. Just as a PoC to init MDC Context Correctly.
	 * 
	 * @param httpServletRequest
	 */
	protected void populateMDCContext(HttpServletRequest httpServletRequest) {

		if (httpServletRequest.getRequestURL().toString().split("//").length > 0
				&& httpServletRequest.getRequestURL().toString().split("//")[1].split("/").length > 5) {
			String version = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[4];
			
			
			String channel = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[3];
			String service = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[5];

			MDC.put(BaseServiceConst.Mdc.SERVICE_NAME, service);
			MDC.put(BaseServiceConst.Mdc.VERSION, version);
			MDC.put(BaseServiceConst.Mdc.APP_NAME,
					httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[1]);
			MDC.put(BaseServiceConst.Mdc.CHANNEL,
					channel);
		}

	}
```

Follows the relevant information put in the **MDC Context** (also automatically obtained by LogBack itself):  
- application: the name of the webapp, in this case *rest-integration-sample*  
- service: the name of the called rest service. 
- channel: the channel associated to the executing call.  
- version: the version associated to the executing call. The only supported version is v1.  
- remoteHost: the IP Address of the caller.  
- method: the HTTP Method used.  

Moreover **Asynch Logging** has been chosen in order to do not decrease performance during logging operation along with log **File Rotation for size and date** to limit disk space dedicated to log files.
What follow is the configuration of a default appender:
```xml
	<appender name="DEFAULT_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>rest-integration-sample.log</file>
		<rollingPolicy
			class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
			<!-- rollover daily -->
			<fileNamePattern>rest-integration-sample.%d{yyyy-MM-dd}.%i.log
			</fileNamePattern>
			<!-- each file should be at most 50MB, keep 30 days worth of history, 
				but at most 500MB -->
			<maxFileSize>50MB</maxFileSize>
			<maxHistory>30</maxHistory>
			<totalSizeCap>500MB</totalSizeCap>
		</rollingPolicy>
		<encoder>
			<pattern>
				%d|%5p|[%c{1}:%L]|%X{application}|%X{serviceName}|%X{channel}|%X{version}|%X{req.remoteHost}|%X{req.method}|%msg%n
			</pattern>
		</encoder>
	</appender>
```
extracted from the main [logback.xml](./src/main/resources/logback.xml) file.

Here a single line of logs:  
```xml
2017-12-26 16:09:40,981| INFO|[c.a.i.s.r.s.c.i.CurrencyServiceImpl:86]|rest-integration-sample|currency-countries-info|mobile|v1|127.0.0.1|GET|getCurrenciesInfo called for page: 4 and size: 10
```

Another feature added for logging is the automatic **logging with Aspects**.
Click [here](./src/main/java/com/amazingsoftware/integration/samples/arch/aspect/log/LogAspect.java) to see main class that, through aspects write logs automatically tracking execution of particular  methods in certain packages writing while entering, exiting, writing passed arguments and returned elements along with execution time.  
It's also possible to filter particular sensible values (such as password, cv2, card number and so on) before printing it.  

Here the main method involved:  

```java
	/**
	 * mainPointCut is an Around Aspect. When the conditions in the PointCuts
	 * defined before are met, particular logs are written before and after
	 * method execution.
	 * 
	 * Sensible data can be easily removed through a filter called in arguments or result and the use of {@link LogFilteringUtils}
	 * 
	 * @param joinPoint
	 *            the JoinPoint of the Around Aspect
	 * @return
	 * @throws Throwable
	 *             eventual exception that can happen during execution
	 */
	@Around("mainPointcut()")
	public Object aroundMainPointcut(ProceedingJoinPoint joinPoint) throws Throwable {

		return executeAround(joinPoint, new BeforeExecution() {
			@Override
			public void run(String className, String operation, Object[] args) {
				logger.info("Entering: {}.{}({})", className == null ? "className is null" : className,
						operation == null ? "operation is null" : operation,
						args == null ? "args are null" : LogFilteringUtils.maskArgs(args));
			}
		}, new AfterExecution() {
			@Override
			public void run(String className, String operation, Object[] args, Object result, long totalTimeMillis) {
				logger.info("Exiting: {}.{}, Return: {}, Executed in: {} ms",
						className == null ? "className is null" : className,
						operation == null ? "operation is null" : operation,
						result == null ? "result is null" : LogFilteringUtils.maskResult(result), totalTimeMillis);
			}
		});
	}
	
	
	@FunctionalInterface
	private interface BeforeExecution {
		void run(String className, String operation, Object[] args);
	}

	@FunctionalInterface
	private interface AfterExecution {
		void run(String className, String operation, Object[] args, Object result, long totalTimeMillis);
	}
	
```	
 
Follows a log line written automatically with Aspects:  

```xml
2017-12-26 12:20:33,759| INFO|[c.a.i.s.a.a.l.LogAspect:95]|rest-integration-sample|currency-countries-info|web|v1|0:0:0:0:0:0:0:1|GET|Exiting: com.amazingsoftware.integration.samples.rest.service.currency.impl.CurrencyServiceImpl.getCurrenciesInfo, Return: [{"name":"Bouvet Island","currencies":[{"code":"NOK","name":"Norwegian krone","symbol":"kr"}]}, {"name":"Brazil","currencies":[{"code":"BRL","name":"Brazilian real","symbol":"R$"}]}, {"name":"British Indian Ocean Territory","currencies":[{"code":"USD","name":"United States dollar","symbol":"$"}]}, {"name":"United States Minor Outlying Islands","currencies":[{"code":"USD","name":"United States Dollar","symbol":"$"}]}, {"name":"Virgin Islands (British)","currencies":[{"name":"[D]","symbol":"$"},{"code":"USD","name":"United States dollar","symbol":"$"}]}, {"name":"Virgin Islands (U.S.)","currencies":[{"code":"USD","name":"United States dollar","symbol":"$"}]}, {"name":"Brunei Darussalam","currencies":[{"code":"BND","name":"Brunei dollar","symbol":"$"},{"code":"SGD","name":"Singapore dollar","symbol":"$"}]}, {"name":"Bulgaria","currencies":[{"code":"BGN","name":"Bulgarian lev","symbol":"??"}]}, {"name":"Burkina Faso","currencies":[{"code":"XOF","name":"West African CFA franc","symbol":"Fr"}]}, {"name":"Burundi","currencies":[{"code":"BIF","name":"Burundian franc","symbol":"Fr"}]}], Executed in: 8 ms
```

## Integration

This sample uses a **[Spring Integration][]** HTTP service exposing a REST web service supporting GET Method.
Through Integration has been managed also **Service Versioning**, **Multi-Channel Management** and **Error Management**.


Here the inbound-gateway configuration, part of the whole configuration file linked [here](./src/main/resources/META-INF/spring/integration/application-spring-integration.xml).

```xml
	<!-- expose rest services -->
	<!-- errors go in the relative channel -->
	<int-http:inbound-gateway id="inboundCurrencyCountriesGateway"
		supported-methods="GET" request-channel="currencyCountriesRequest"
		reply-channel="currencyCountriesResponse" mapped-response-headers="HTTP_RESPONSE_HEADERS"
		error-channel="errorChannel"
		path="/services/{channel}/${application.version}/currency-countries-info" reply-timeout="50000">

		<!-- channel provided in the path, is automatically copied in the message header. -->
		<int-http:header name="channel" expression="#pathVariables.channel" />

	</int-http:inbound-gateway>
```

Version exposed is taken from a property file and is segregated for different environment through the use of Spring Profiles. See the **[Environment segregation Section](https://github.com/aleMessoMale/sample-routing/#environment-segregation "Environment segregation Section")** for more details.  

Channel provided in the path is automatically copied in the message header.
According to that value, through a header-value-router whose configuration is shown below, Message is routed to a different channel and managed from a different service activator. 

```xml
<!-- according to header message value, message goes in the right channel -->
	<int:header-value-router input-channel="currencyCountriesRequest"
		header-name="channel" default-output-channel="unSupportedChannel" resolution-required="false">
		<int:mapping value="mobile" channel="mobileChannel" />
		<int:mapping value="web" channel="webChannel" />
		<int:mapping value="otherChannels" channel="unSupportedChannel" />
	</int:header-value-router>
```

At the time of writing, only web and mobile channels are supported. If provided channel is not supported, Message is redirected to a particular channel and managed from a service activator for a specific management.  

Main entry point for the supported channels and version for the country-currency service is the currencyFacade. See **[Multi-layer Project Section](https://github.com/aleMessoMale/sample-routing/#multi-layer-project "Multi-layer Section")** for more detail regarding the project structure.  
Here another extract of the whole configuration file showing the main service activators:

```xml
	<!-- service activator manage at facade level the message routed, web channel-->
	<int:service-activator id="currencyServiceActivatorWeb"
		input-channel="webChannel" output-channel="currencyCountriesResponse"
		ref="currencyFacade" method="getCurrenciesInfoForWeb" requires-reply="true"
		send-timeout="60000" />
	
	<!-- service activator manage at facade level the message routed, mobile channel-->
	<int:service-activator id="currencyServiceActivatorMobile"
		input-channel="mobileChannel" output-channel="currencyCountriesResponse"
		ref="currencyFacade" method="getCurrenciesInfoForMobile" requires-reply="true"
		send-timeout="60000" />
```


As shown in the first extract, any error raised is automatically redirected to the errorChannel and managed from a specific service activator as shown below: 

```xml
<int:service-activator input-channel="errorChannel" ref="genericCurrencyServiceErrorManager" method="handleError"/>
```

[Here](./src/main/java/com/amazingsoftware/integration/samples/arch/errors/GenericCurrencyServiceErrorManager.java) is linked the whole ErrorManager and below the method encharged of the error management and mapping:


```java
 @ServiceActivator
    public Message<ErrorResponse> handleError(Message<MessageHandlingException> message) {
    	
    	Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
    	ErrorResponse errorResp = new ErrorResponse();
    	
    	
    	if(message!=null && message.getPayload()!=null && message.getPayload().getCause()!=null){
    		String errMessage = message.getPayload().getCause().toString();
    		String statusCode = getHttpStatusCodeFromMessage(errMessage);
    		responseHeaderMap.put(org.springframework.integration.http.HttpHeaders.STATUS_CODE, statusCode);
    		
    		errorResp.setError(errorBuilder.buildErrorFromMessage(getErrorMessageFromMessage(errMessage)));
    	} else {
    		responseHeaderMap.put(org.springframework.integration.http.HttpHeaders.STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    		errorResp.setError(errorBuilder.buildErrorFromMessage(BaseServiceConst.ErrorMessages.GENERIC_ERROR));
    	}
    	

		Message<ErrorResponse> mappedError = new GenericMessage<ErrorResponse>(errorResp, responseHeaderMap);
		

        
        return mappedError;
    }
```

As it is possible to see an [ErrorBuilder](./src/main/java/com/amazingsoftware/integration/samples/arch/errors/builder/AbstractErrorBuilder.java) has been created in order to make returned error Json structure always equal and easily understandable for the client.


Managed errors at the time of writing are:   
- Call to channel different from web and mobile return a "Channel not Supported" error.  
- Uncorrect combinations of query string parameters (only one of them, not a number provided as a value, negative numbers and so on) return a "Parameter not supported"  error.  
- Credentials not correct for the Spring Security Authentication, returns a 401 Error.   

For further details about functional requirements, see the **[Test Section](https://github.com/aleMessoMale/sample-routing/#test "Test Section")**, in a particular way the integration one. 

## Environment segregation

Environment segregation has been taken into account and has been managed through Spring profiles. 

Different Spring profiles imply different property file loading and different Spring Context inizialization.  
It's necessary in fact insert the System Variable **spring.profiles.active="dev"** to the Servlet Container in which the webapp will be deployed that is, at the time of writing, the only working spring profile.  

Here the relevant code associated with this feature.  

```java
/**
 * 
 * 
 *         This Configuration file points to a specific property file and is
 *         associated to a particular Spring Profile which could be associated
 *         to a particular environment.
 * 
 *         Put in the PropertySource referenced file the correct values for the
 *         relative environment.
 *         
 *         @author al.casula
 */
@Configuration
@PropertySource(value = { "classpath:application-dev.properties" })
@Profile("dev")
public class ApplicationConfigDev {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
```


## Pagination

At the PostConstruct phase of the Spring Service Bean injection, the whole list of countries and relative currencies are loaded from the source rest at the URL: http://restcountries.eu/rest/v2/ and stored in a list as a **cache** as shown below:  

```java
/**
	 * Caching values in init method so that there's no need to explicitly
	 * call the rest service at every invokation of the services methods. We expect these data to change rarely.
	 */
	@PostConstruct
	public void init() {
		countryCurrenciesInfoResponse = fillCountriesCurrenciesInfo();
	}
```


Subsequent invokation of the service return:  
- the whole list if pagination is not set.  
- a part of the list, filtered through Streams, if pagination is set.     
Follows an extract of the [main class](./src/main/java/com/amazingsoftware/integration/samples/utils/FilterUtils.java) utility involved for filtering:  

```java
/**
	 * This method filters a given list for the provided indexes.
	 * 
	 * @param listToFilter
	 *            the list to filter
	 * @param initialIndex
	 *            inclusive
	 * @param lastIndex
	 *            excluded
	 * @return the filtered list
	 */
	public <T> List<T> filterArrayListForIndex(List<T> listToFilter, int initialIndex, int lastIndex) {
		/* last index is equal to size since last index is excluded */
		int lastIndexInListToFilter = listToFilter.size();

		if (initialIndex > lastIndex) {
			throw new IllegalArgumentException("Initial index cannot be greater than last index");
		}

		if (initialIndex < 0 || lastIndex < 0) {
			throw new IllegalArgumentException("Index cannot be negative.");
		}

		/*
		 * Querying indexes greater than the size of the list to filter, don't
		 * throw error, just return an empty array
		 */
		if (initialIndex > lastIndexInListToFilter) {
			return new ArrayList<T>();
		}

		if (initialIndex < lastIndexInListToFilter && lastIndex > lastIndexInListToFilter) {
			logger.info("Moving last index from {} to {}", lastIndex, lastIndexInListToFilter);
			lastIndex = lastIndexInListToFilter;
		}

		List<T> filteredList = IntStream.range(initialIndex, lastIndex).mapToObj(listToFilter::get)
				.collect(Collectors.toList());

		return filteredList;
	}
```

It's possible then to receive data in a **paginated way**, passing two URL Variables (Query String):   
- *pageNumber*: the number of the page.  
- *pageSize*: the size of a single page.       

Here an example of URL to invoke: http://localhost:8080/rest-integration-sample/services/v1/currency-countries-info?pageSize=10&pageNumber=5  

In this case I'm querying the rest service to get the fifth page, with a size of ten elements per page, asking then for elements which start from the 40th and go till the 50th country. 
URLparams are optional. If no pagination is requested, all elements are returned.  

Uncorrect combination of query string parameters (only one of them, not a number provided as a value, negative numbers and so on) return a "Parameter not supported" error.  


## Test

**Unit test** and **Integration tests** are present.   

Both kind of tests use Spring's dependency injection and are associated to the test profile as shown in this extract below:

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ContextLoaderListenerConfigTest.class, ApplicationConfigPropertyTest.class,
		SecurityLayerConfigTest.class, ServiceLayerConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
```

Unit test are relative to the service-layer (see **[Multi-layer Project Section](https://github.com/aleMessoMale/sample-routing/#multi-layer-project "Multi-layer Section")** for multi-layer explaination).   

Click [here](./src/test/java/test/com/amazingsoftware/integration/sample/rest/ServiceLayerTest.java) to see it in detail.

**Integration tests** have been obtained through the integration of **[Maven Failsafe Plugin][]** and **[Jetty Maven Plugin][]**  to the [Maven][] default lifecycle phases involved in the integration tests matters.

Here the relevant section of the pom.xml relative to the Integration tests:
```xml
<!-- Integration Testing -->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-failsafe-plugin</artifactId>
				<version>2.20.1</version>
				<configuration>
					<includes>
						<!-- naming convention of Integration Test Java file -->
						<exclude>%regex[.*(IntTest).*]</exclude>
					</includes>
				</configuration>
				<executions>
					<execution>
						<id>integration-test</id>
						<goals>
							<goal>integration-test</goal>
						</goals>
					</execution>
					<execution>
						<id>verify</id>
						<goals>
							<goal>verify</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<!-- Integration Testing -->
			<plugin>
				<groupId>org.eclipse.jetty</groupId>
				<artifactId>jetty-maven-plugin</artifactId>
				<!-- <version>9.2.3</version> -->
				<version>9.4.2.v20170220</version>
				<configuration>
					<webApp>
						<contextPath>/rest-integration-sample</contextPath>
					</webApp>
					<httpConnector>
						<port>8090</port>
					</httpConnector>
					<scanIntervalSeconds>10</scanIntervalSeconds>
					<stopPort>8005</stopPort>
					<stopKey>STOP</stopKey>
				</configuration>
				<executions>
					<execution>
						<id>start-jetty</id>
						<phase>pre-integration-test</phase>
						<goals>
							<!-- stop any previous instance to free up the port -->
							<goal>stop</goal>
							<goal>start</goal>
						</goals>
						<configuration>
							<scanIntervalSeconds>0</scanIntervalSeconds>
							<daemon>true</daemon>
						</configuration>
					</execution>
					<execution>
						<id>stop-jetty</id>
						<phase>post-integration-test</phase>
						<goals>
							<goal>stop</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
```
extracted from the whole **[pom.xml](./pom.xml)**.

During integration tests, a Jetty Server is started with the just built war artifact and an Integration test, [CurrencyInfoIntTest.java][], has been created to cover most relevant requirements, stated in the JavaDoc's test, as shown below in this extract:

```java
     /**
	 * tests that calling the rest service exposed by the integration test
	 * environment, with pagination, response has an Http Status of OK, size is
	 * equal to page size and elements content are correct.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithPaginationReturnsPageSizeElementsWithCorrectContent()
			throws Exception {

		String fullUrl = testUtils.getWebIntegrationEnvFullUrl();

		final String pageNum = "2";
		final String pageSize = "10";

		HashMap<String, String> queryStringValues = new HashMap<String, String>();
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_NUM_PARAM, pageNum);
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_SIZE_PARAM, pageSize);
		String queryString = httpUtils.createRequestParameterQueryString(queryStringValues);
		logger.info("Created Query String:" + queryString);

		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword);
		fullUrl += queryString;
		
		ResponseEntity<CurrencyFacadeResponse> exchange = testUtils.executeRestCall(headers,fullUrl,HttpMethod.GET,CurrencyFacadeResponse.class);
		

		assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
		assertEquals(BaseServiceConst.Channels.WEB_CHANNEL, exchange.getBody().getChannel());
		assertEquals(testUtils.getIntegrationEnv().getCurrencyVersion(), exchange.getBody().getVersion());
		assertEquals(NumberUtil.checkIntegerNumber(pageSize, null).intValue(), exchange.getBody().getCountryInfoList().size());
		assertEquals("Argentina", exchange.getBody().getCountryInfoList().get(0).getName());

	}
```

The test uses Spring's [RestTemplate][] to assemble and send HTTP requests. The *Server*, on the other hand, is using Spring Integration's HTTP Endpoint configuration.  

A **[Test Util class](./src/test/java/test/com/amazingsoftware/integration/sample/utils/TestUtils.java)** has been written for most common operations.

[Apache Tomcat]: http://tomcat.apache.org/
[Jetty]: http://www.eclipse.org/jetty/
[JUnit]: http://junit.org/
[RestTemplate]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
[Spring Security]: http://www.springsource.org/spring-security
[Spring Tool Suite]: http://www.springsource.org/sts
[Maven]: http://maven.apache.org/
[Maven Failsafe Plugin]: http://maven.apache.org/surefire/maven-failsafe-plugin/
[Jetty Maven Plugin]: https://www.eclipse.org/jetty/documentation/9.4.x/jetty-maven-plugin.html
[PostMan]:https://www.getpostman.com/
[LogBack]:https://logback.qos.ch/
[Spring Integration]: https://projects.spring.io/spring-integration/
[CurrencyInfoIntTest.java]: ./src/test/java/test/com/amazingsoftware/integration/sample/rest/integration/currency/CurrencyInfoIntTest.java
[user.properties]: ./src/main/resources/users.properties