package test.com.amazingsoftware.integration.sample.utils;

import static test.com.amazingsoftware.integration.sample.consts.TestConsts.URL_SEPARATOR;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import test.com.amazingsoftware.integration.sample.config.ApplicationConfigPropertyTest;
import test.com.amazingsoftware.integration.sample.config.ContextLoaderListenerConfigTest;
import test.com.amazingsoftware.integration.sample.config.SecurityLayerConfigTest;
import test.com.amazingsoftware.integration.sample.config.ServiceLayerConfigTest;
import test.com.amazingsoftware.integration.sample.consts.IntegrationEnv;

/**
 * This bean contains some test utility methods.
 * 
 * @author al.casula
 *
 */
@Component
@ContextConfiguration(classes = { ContextLoaderListenerConfigTest.class}, loader = AnnotationConfigContextLoader.class)
//@ActiveProfiles("test")
public class TestUtils {
	
	@Autowired
	private RestTemplate restTemplate;

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Autowired
	IntegrationEnv integrationEnv;

	public IntegrationEnv getIntegrationEnv() {
		return integrationEnv;
	}

	public void setIntegrationEnv(IntegrationEnv integrationEnv) {
		this.integrationEnv = integrationEnv;
	}

	Logger logger = LoggerFactory.getLogger(TestUtils.class);

	
	/** 
	 * 
	 * @param username the username for the basic authentication.
	 * @param password the password for the basic authentication.
	 * @return the HttpHeaders containing a basic authentication header based upon the supplied parameters.
	 */
	public HttpHeaders getHttpHeadersWithUserCredentials(String username, String password) {

		HttpHeaders headers = new HttpHeaders();

		String combinedUsernamePassword = username + ":" + password;
		byte[] base64Token = Base64.getEncoder().encode(combinedUsernamePassword.getBytes());
		String base64EncodedToken = new String(base64Token);

		headers.add("Authorization", "Basic " + base64EncodedToken);

		logger.info("Basic " + base64EncodedToken);

		return headers;
	}

	/**
	 * During integration tests a servlet container is started exposing the country currency service. 
	 * This method returns the full url to point to the service exposed by the integration servlet container server for the web channel.
	 * 
	 * @return the Integration URL to call during integration tests.
	 * 
	 */
	public String getWebIntegrationEnvFullUrl() {
		StringBuilder fullUrlSb = new StringBuilder();


		fullUrlSb.append("http://").append(integrationEnv.getBaseHost()).append(":").append(integrationEnv.getPort()).append(URL_SEPARATOR)
				.append(integrationEnv.getContextPath()).append(URL_SEPARATOR).append(integrationEnv.getServicesPrefix())
				.append(URL_SEPARATOR).append(integrationEnv.getWebChannel()).append(URL_SEPARATOR).append(integrationEnv.getCurrencyVersion()).append(URL_SEPARATOR).append(integrationEnv.getCurrencyBaseAppContextPath());
		
		String webFullUrl = fullUrlSb.toString();
		
		logger.debug("Returning web full url: {}", webFullUrl);
		
		return webFullUrl;
	}
	
	/**
	 * During integration tests a servlet container is started exposing the country currency service. 
	 * This method returns the full url to point to the service exposed by the integration servlet container server for the mobile channel.
	 * 
	 * @return the Integration URL to call during integration tests.
	 * 
	 */
	public String getMobileIntegrationEnvFullUrl() {
		StringBuilder fullUrlSb = new StringBuilder();


		fullUrlSb.append("http://").append(integrationEnv.getBaseHost()).append(":").append(integrationEnv.getPort()).append(URL_SEPARATOR)
				.append(integrationEnv.getContextPath()).append(URL_SEPARATOR).append(integrationEnv.getServicesPrefix())
				.append(URL_SEPARATOR).append(integrationEnv.getMobileChannel()).append(URL_SEPARATOR).append(integrationEnv.getCurrencyVersion()).append(URL_SEPARATOR).append(integrationEnv.getCurrencyBaseAppContextPath());
		
		String mobileFullUrl = fullUrlSb.toString();
		
		logger.debug("Returning mobile full url: {}", mobileFullUrl);
		
		return mobileFullUrl;
	}
	
	
	
	public <T> ResponseEntity<T> executeRestCall(HttpHeaders headers, String fullUrl, HttpMethod httpMethod, Class<T> responseType){
		HttpEntity<Object> request = new HttpEntity<Object>(headers);

		logger.info("Calling with URI: {} and Request {} ", fullUrl, request);

		ResponseEntity<T> exchange = restTemplate.exchange(fullUrl, httpMethod, request, responseType);
		
		return exchange;
	}
	

}
