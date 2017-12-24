/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.com.amazingsoftware.integration.sample.rest.integration.currency;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.amazingsoftware.integration.samples.consts.BaseServiceConst;
import com.amazingsoftware.integration.samples.rest.service.currency.domain.CountryInfoResponse;
import com.amazingsoftware.integration.samples.utils.HttpUtils;
import com.amazingsoftware.integration.samples.utils.NumberUtil;

import test.com.amazingsoftware.integration.sample.config.ApplicationConfigPropertyTest;
import test.com.amazingsoftware.integration.sample.config.ContextLoaderListenerConfigTest;
import test.com.amazingsoftware.integration.sample.config.SecurityLayerConfigTest;
import test.com.amazingsoftware.integration.sample.config.ServiceLayerConfigTest;
import test.com.amazingsoftware.integration.sample.consts.IntegrationEnv;
import test.com.amazingsoftware.integration.sample.consts.TestConsts;
import test.com.amazingsoftware.integration.sample.utils.TestUtils;

/**
 * 
 * This class contains all the Integration tests. Integration tests have been obtained
 * through the integration of maven-failsafe-plugin and maven jetty plugin
 * to the relevant default lifecycle phases involved.
 * 
 * A jetty server starts exposing the rest service as it will be accessible
 * in the target environment.
 * 
 * Integration server is accessible at
 * http://localhost:8090/rest-integration-sample/services/v1/currency-countries-info?pageSize=<pageSize>&pageNumber=<pageNumber>
 * 
 * QueryString params are optional. If no pagination is requested, all elements
 * are returned.
 * 
 * 
 * 
 * @author al.casula
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ContextLoaderListenerConfigTest.class, ApplicationConfigPropertyTest.class,
		SecurityLayerConfigTest.class, ServiceLayerConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class CurrencyInfoIntTest {

	@Value("${security.username}")
	private String springSecurityUsername;

	@Value("${security.password}")
	private String springSecurityPassword;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private TestUtils testUtils;

	private HttpMessageConverterExtractor<List<CountryInfoResponse>> responseExtractor;
	Logger logger = LoggerFactory.getLogger(CurrencyInfoIntTest.class);

	@Rule
	public ExpectedException exceptionExpected = ExpectedException.none();

	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment, with no pagination, response has an Http Status of OK.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestOK() throws Exception {

		final String fullUrl = testUtils.getIntegrationEnvFullUrl();
		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		ParameterizedTypeReference<List<CountryInfoResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoResponse>>() {
		};

		ResponseEntity<List<CountryInfoResponse>> exchange;

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);

		exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		List<CountryInfoResponse> countryListResponse = exchange.getBody();

		assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));

	}

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

	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment, with no pagination, response has an Http Status of OK and a
	 * size equal to 250, the size of all the elements exposed by the source
	 * rest service.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithNoPaginationReturnsAllElements() throws Exception {

		final String fullUrl = testUtils.getIntegrationEnvFullUrl();
		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		ParameterizedTypeReference<List<CountryInfoResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoResponse>>() {
		};

		ResponseEntity<List<CountryInfoResponse>> exchange;

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);

		exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		List<CountryInfoResponse> countryListResponse = exchange.getBody();

		assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
		assertEquals(TestConsts.REST_COUNTRIES_RESPONSE_SIZE, exchange.getBody().size());

	}

	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment, and asking with pagination to elements which are not
	 * present, returned list is empty and no error is returned.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestReturnsAnEmptyListIfPaginationRequestValuesNotPresent() throws Exception {

		String fullUrl = testUtils.getIntegrationEnvFullUrl();

		String pageNum = "50";
		String pageSize = "10";

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

		assertNotNull(exchange);
		assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
		assertEquals(new Integer("0").intValue(), exchange.getBody().size());
	}

	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment, and asking with pagination to elements present in the
	 * boundaries, correct values are returned in terms of size and content.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestBoundCondition() throws Exception {

		String fullUrl = testUtils.getIntegrationEnvFullUrl();

		/* Rest Source size is 250, requested elements are the last 10 */
		String pageNum = "7";
		String pageSize = "40";

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

		assertNotNull(exchange);
		assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
		assertEquals(new Integer("10").intValue(), exchange.getBody().size());
		assertEquals("Uruguay", exchange.getBody().get(0).getName());
		assertEquals("Zimbabwe", exchange.getBody().get(9).getName());

	}

	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment with an inconsistent query string parameter (only pageNum
	 * parameter is present), internal server error is returned with a specific
	 * error message - parameter supplied not supported.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithInconsistentQueryStringParamReturnsInternalServerError()
			throws Exception {

		String fullUrl = testUtils.getIntegrationEnvFullUrl();

		final String pageNum = "2";

		HashMap<String, String> queryStringValues = new HashMap<String, String>();
		/* Only pageNum is passed */
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_NUM_PARAM, pageNum);
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

		try {
			exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		} catch (org.springframework.web.client.HttpServerErrorException e) {
			assertTrue(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR));
		}

	}

	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment with an inconsistent query string parameter (pageNum and
	 * pageSize are not numbers), internal server error is returned with a
	 * specific error message - parameter supplied not supported.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithNotANumberQueryStringParamReturnInternalServerError()
			throws Exception {

		String fullUrl = testUtils.getIntegrationEnvFullUrl();

		final String pageNum = "33xx";
		final String pageSize = "aa";

		HashMap<String, String> queryStringValues = new HashMap<String, String>();
		/* Only pageNum is passed */
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_NUM_PARAM, pageNum);
		queryStringValues.put(BaseServiceConst.QueryStringParameters.PAGE_SIZE_PARAM, pageSize);
		String queryString = httpUtils.createRequestParameterQueryString(queryStringValues);
		logger.info("Created Query String:" + queryString);

		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		ParameterizedTypeReference<List<CountryInfoResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoResponse>>() {
		};

		ResponseEntity<List<CountryInfoResponse>> exchange = null;

		fullUrl += queryString;

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);
		try {
			exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		} catch (HttpServerErrorException e) {
			assertTrue(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR));
		}

	}
	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment an unAuthorized error 401 is returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithWrongCredendialsReturnsUnAuthCode401Code() throws Exception {

		final String fullUrl = testUtils.getIntegrationEnvFullUrl();
		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword + "Wxx");
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		ParameterizedTypeReference<List<CountryInfoResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoResponse>>() {
		};

		ResponseEntity<List<CountryInfoResponse>> exchange = null;

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);

		try {
			exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		} catch (org.springframework.web.client.HttpClientErrorException e) {
			assertTrue(e.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
		}

	}
	/**
	 * tests that calling the rest service exposed by the integration test
	 * environment with the wrong version, a 404, resource not available, is returned
	 * with a specific error message - version not supported.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallCountryCurrenciesRestWithWrongVersionReturns404() throws Exception {

		IntegrationEnv env = testUtils.getIntegrationEnv();
		env.setCurrencyVersion("v2");

		final String fullUrl = testUtils.getIntegrationEnvFullUrl();

		HttpHeaders headers = testUtils.getHttpHeadersWithUserCredentials(springSecurityUsername,
				springSecurityPassword);
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		ParameterizedTypeReference<List<CountryInfoResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoResponse>>() {
		};

		ResponseEntity<List<CountryInfoResponse>> exchange = null;

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);

		try {
			exchange = restTemplate.exchange(fullUrl, HttpMethod.GET, request, listOfCountryResponse);
		} catch (org.springframework.web.client.HttpClientErrorException e) {
			assertTrue(e.getStatusCode().equals(HttpStatus.NOT_FOUND));
		} finally {
			env.setCurrencyVersion("v1");
		}

	}

}
