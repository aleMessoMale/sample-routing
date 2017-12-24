package test.com.amazingsoftware.integration.sample.utils;

import static test.com.amazingsoftware.integration.sample.consts.TestConsts.URL_SEPARATOR;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import test.com.amazingsoftware.integration.sample.consts.IntegrationEnv;

/**
 * This bean contains some test utility methods.
 * 
 * @author al.casula
 *
 */
@Component
public class TestUtils {

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
	 * This method returns the full url to point to the service exposed by the integration servlet container server.
	 * 
	 * @return the Integration URL to call during integration tests.
	 * 
	 */
	public String getIntegrationEnvFullUrl() {
		StringBuilder fullUrlSb = new StringBuilder();


		fullUrlSb.append("http://").append(integrationEnv.getBaseHost()).append(":").append(integrationEnv.getPort()).append(URL_SEPARATOR)
				.append(integrationEnv.getContextPath()).append(URL_SEPARATOR).append(integrationEnv.getServicesPrefix())
				.append(URL_SEPARATOR).append(integrationEnv.getCurrencyVersion()).append(URL_SEPARATOR).append(integrationEnv.getCurrencyBaseAppContextPath());
		
		return fullUrlSb.toString();
	}
	

}
