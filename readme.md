# Introduction

This sample demonstrates how you can send an HTTP request to a **[Spring Integration][]** HTTP service. This sample also uses **[Spring Security][]** for HTTP Basic authentication. With the HTTP Path facility, the client program can send requests with URL Variables.

Feed by a Rest Web Service: http://restcountries.eu/rest/v2/, the rest web service created is able to return a list of countries with their relative currency values.
 
Here the URL to invoke: *http://localhost:8080/rest-integration-sample/services/v1/currency-countries-info*   
Supported Http Verb is GET.   


Here an example of correct response:
```json
[
    {
        "name": "Nicaragua",
        "currencies": [
            {
                "code": "NIO",
                "name": "Nicaraguan córdoba",
                "symbol": "C$"
            }
        ]
    },{
        "name": "Niger",
        "currencies": [
            {
                "code": "XOF",
                "name": "West African CFA franc",
                "symbol": "Fr"
            }
        ]
    } and so on...
 ] 
```


It's also possible to receive data in a **paginated way**, passing URL Variables   

URLparams are optional. If no pagination is requested, all elements are returned.

See **[Pagination](https://github.com/aleMessoMale/sample-routing/#pagination "Pagination Section")** for more info.

The only **version** supported is the v1, and, as you probably already noticed, is part of the exposed path. See **[Integration](https://github.com/aleMessoMale/sample-routing/#integration "Integration Section")** section to better understand how versioning has been managed.

As already said, the web service is secured with **Spring Integration**. Here the **credentials** to provide, with Basic Authentication, in order to consume the exposed rest web service. These are present in the user.properties file:  
username: INTEGRATION_REST_USER  
password: 1Password  

For more info see the **[Security](https://github.com/aleMessoMale/sample-routing/#security "Security Section")** section.  

Unit tests and Integration tests have been created. See the **[Test](https://github.com/aleMessoMale/sample-routing/#test "Test Section")** section for more info.

The provided project itself is a web project and it can be build using [Maven][] and the resulting war under `target/rest-integration-sample.war` can be deployed to Servlet Containers such as [Jetty][] or [Apache Tomcat][].

**Environment property segregation** has been obtained through the use of Spring profiles. See the **[Environment segregation](https://github.com/aleMessoMale/sample-routing/#environment-segregation "Environment segregation Section")** section for more details.

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

For testing purpose, you can use a Rest Client as [PostMan][] and call the subsequent URL with the GET Http Verb: *http://localhost:8080/rest-integration-sample/services/v1/currency-countries-info?pageSize=10&pageNumber=5*  

This is an example of paginated response and returns the fifth page and the page will have a size of ten elements. 

Pagination (and the relative URL Variables pageSize and pageNumber) is optional. If no pagination is provided, all values are returned.    


# Main features and more details

## Multi-layer project

This project is composed by these layers:  
- *Security*: A security layer has been put in place through [Spring Security]. For more info see the Spring Security configuration file [here](./src/main/webapp/WEB-INF/config/security-config.xml) or the relative section [Security](https://github.com/aleMessoMale/sample-routing/#security "Security Section")
- *Web*: A servlet is called in order to fill the MDC Context of [LogBack] and allow a better logging experience. For more info See [here](./src/main/java/com/amazingsoftware/integration/samples/web/MDCInsertingServletFilter.java) the Servlet which fills the MDC Context, the whole [logback.xml](./src/main/resources/logback.xml) file or the specific **[Logs](https://github.com/aleMessoMale/sample-routing/#logs "Logs Section")** section.  
- *Integration*: An Integration layer through [Spring Integration] has been put in place in order to orchestrate correctly messages received from the exposed Rest Web Service. For more info see the Spring Configuration file for the Integration features [here](./src/main/resources/META-INF/spring/integration/application-spring-integration.xml) or the relative section [Integration](https://github.com/aleMessoMale/sample-routing/#integration "Integration Section")  
- *Facade*: A facade layer has been put in place in order to make easier the interaction with the below service layer.  
- *Service*: A Service layer has been put in place. This layer is responsible for most of the business logic creating decoupling with the above Facade Layer.

An extensible [Mapper](./src/main/java/com/amazingsoftware/integration/samples/arch/mapper/impl/Mapper.java) class (optimazed with Streams) has been written in order to make easier the mapping operations between domain objects of different layers so that is possible to keep different layers loosely coupled as much as possible.

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
public GenericMessage<List<CountryInfoResponseFacade>> getCurrenciesInfo(Message<?> inMessage) throws Exception {

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
				&& httpServletRequest.getRequestURL().toString().split("//")[1].split("/").length > 4) {
			String operation = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[4];
			String version = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[3];

			MDC.put(BaseServiceConst.Mdc.OPERATION, operation);
			MDC.put(BaseServiceConst.Mdc.VERSION, version);
			MDC.put(BaseServiceConst.Mdc.SERVICE_NAME,
					httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[1]);
		}

	}
```

Follows the relevant information put in the **MDC Context** (also automatically obtained by LogBack itself):  
- serviceName: the name of the webapp, in this case *rest-integration-sample*  
- operation: the name of the called rest operation.  
- version: the version actually supported is v1.  
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
				%d|%5p|[%c{1}:%L]|%X{serviceName}|%X{operation}|%X{version}|%X{req.remoteHost}|%X{req.method}|%msg%n
			</pattern>
		</encoder>
	</appender>
```
extracted from the main [logback.xml](./src/main/resources/logback.xml) file.

Here a single line of logs:  
```xml
2017-12-22 15:59:16,245| INFO|[c.a.i.s.r.s.c.i.CurrencyServiceImpl:86]|rest-integration-sample|currency-countries-info|v1|127.0.0.1|GET|getCurrenciesInfo called for page: 2 and size: 10
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
2017-12-22 16:01:39,744| INFO|[c.a.i.s.a.a.l.LogAspect:95]|rest-integration-sample|currency-countries-info|v1|127.0.0.1|GET|Exiting: com.amazingsoftware.integration.samples.rest.service.currency.impl.CurrencyServiceImpl.getCurrenciesInfo, Return: [{"name":"Cambodia","currencies":[{"code":"KHR","name":"Cambodian riel","symbol":"?"},{"code":"USD","name":"United States dollar","symbol":"$"}]}, {"name":"Cameroon","currencies":[{"code":"XAF","name":"Central African CFA franc","symbol":"Fr"}]}, {"name":"Canada","currencies":[{"code":"CAD","name":"Canadian dollar","symbol":"$"}]}, {"name":"Cabo Verde","currencies":[{"code":"CVE","name":"Cape Verdean escudo","symbol":"Esc"}]}, {"name":"Cayman Islands","currencies":[{"code":"KYD","name":"Cayman Islands dollar","symbol":"$"}]}, {"name":"Central African Republic","currencies":[{"code":"XAF","name":"Central African CFA franc","symbol":"Fr"}]}, {"name":"Chad","currencies":[{"code":"XAF","name":"Central African CFA franc","symbol":"Fr"}]}, {"name":"Chile","currencies":[{"code":"CLP","name":"Chilean peso","symbol":"$"}]}, {"name":"China","currencies":[{"code":"CNY","name":"Chinese yuan","symbol":"¥"}]}, {"name":"Christmas Island","currencies":[{"code":"AUD","name":"Australian dollar","symbol":"$"}]}], Executed in: 11 ms
```

## Integration

This sample uses a **[Spring Integration][]** HTTP service exposing a REST web service supporting GET Method.
Through Integration has been managed also **Service Versioning** and **Error Management**.


Here the inbound-gateway configuration, part of the whole configuration file linked [here](./src/main/resources/META-INF/spring/integration/application-spring-integration.xml).

```xml
<int-http:inbound-gateway id="inboundCurrencyCountriesGateway"
		supported-methods="GET" request-channel="currencyCountriesRequest"
		reply-channel="currencyCountriesResponse" mapped-response-headers="HTTP_RESPONSE_HEADERS"
		error-channel="errorChannel"
		path="/services/{version}/currency-countries-info" reply-timeout="50000">

		<int-http:header name="version" expression="#pathVariables.version" />

	</int-http:inbound-gateway>
```

Version provided in the path is automatically copied in the message header.
According to that value, through a header-value-router whose configuration is shown below, Message is routed to a different channel and managed from a different service activator. 

```xml
	<int:header-value-router input-channel="currencyCountriesRequest"
		header-name="version" default-output-channel="unSupportedVersionChannel" resolution-required="false">
		<int:mapping value="v1" channel="v1Channel" />
		<int:mapping value="otherVersions" channel="unSupportedVersionChannel" />
	</int:header-value-router>
```

At the time of writing, only v1 is supported. If version is not supported, Message is redirected to a particular channel and managed from a service activator for a specific management. Main entry point for the supported version for the country-currency service is the currencyFacade. See **[Multi-layer](https://github.com/aleMessoMale/sample-routing/#multi-layer-project "Multi-layer Section") project** section for more detail regarding the project structure.  
Here another extract of the whole configuration file showing the main service activator.  

```xml
	<int:service-activator id="currencyServiceActivator"
		input-channel="v1Channel" output-channel="currencyCountriesResponse"
		ref="currencyFacade" method="getCurrenciesInfo" requires-reply="true"
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
- Call to other versions return a "Version not Supported" error.  
- Uncorrect combinations of query string parameters (only one of them, not a number provided as a value, negative numbers and so on) return a "Parameter not supported"  error.  
- Credentials not correct for the Spring Security Authentication, returns a 401 Error.   

For further details about functional requirements, see the section **[Test](https://github.com/aleMessoMale/sample-routing/#test "Test Section")**, in a particular way the integration one. 

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
Follows an extract of the main [Util](./src/main/java/com/amazingsoftware/integration/samples/utils/FilterUtils.java) filtering class involved for filtering:  

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

Unit test are relative to the service-layer (see **[Multi-layer](https://github.com/aleMessoMale/sample-routing/#multi-layer-project "Multi-layer Section") project** Section for multi-layer explaination) and Click [here](./src/test/java/test/com/amazingsoftware/integration/sample/rest/ServiceLayerTest.java) to see it in detail.

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

During integration tests, a Jetty Server is started with the just built war artifact and an Integration test: [CurrencyInfoIntTest.java][], has been created to cover most relevant requirements, stated in the JavaDoc's test, as shown below in this extract:

```java
/**
	 * tests that calling the rest service exposed by the integration test
	 * environment, with pagination, response has an Http Status of OK, size is
	 * equal to page size and element are correct.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithPaginationReturnsPageSizeElementsWithCorrectContent()
			throws Exception {

		String fullUrl = testUtils.getIntegrationEnvFullUrl();

		final String pageNum = "2";
		final String pageSize = "10";

		HashMap<String, String> queryStringValues = new HashMap<String, String>();
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_NUM_PARAM, pageNum);
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_SIZE_PARAM, pageSize);
		String queryString = httpUtils.createRequestParameterQueryString(queryStringValues);
		logger.info("Created Query String:" + queryString);

		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		ParameterizedTypeReference<List<CountryInfoResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoResponse>>() {
		};

		ResponseEntity<List<CountryInfoResponse>> exchange;

		fullUrl += queryString;

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);

		exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		List<CountryInfoResponse> countryListResponse = exchange.getBody();

		assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
		assertEquals(NumberUtil.checkIntegerNumber(pageSize, null).intValue(), exchange.getBody().size());
		assertEquals("Argentina", exchange.getBody().get(0).getName());

	}
```

The test uses Spring's [RestTemplate][] to assemble and send HTTP requests. The *Server*, on the other hand, is using Spring Integration's HTTP Endpoint configuration.

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