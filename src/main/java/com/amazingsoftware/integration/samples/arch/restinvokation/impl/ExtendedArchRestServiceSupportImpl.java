package com.amazingsoftware.integration.samples.arch.restinvokation.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.amazingsoftware.integration.samples.arch.restinvokation.ExtendedArchRestServiceSupport;

@Component
public class ExtendedArchRestServiceSupportImpl implements ExtendedArchRestServiceSupport {

	private static Logger logger = LoggerFactory.getLogger(ExtendedArchRestServiceSupportImpl.class);

	private RestTemplate restTemplate;

	private static final char JSON_OPENING_OBJECT_CHAR = '{';
	private static final char JSON_OPENING_ARRAY_CHAR = '[';

	public ExtendedArchRestServiceSupportImpl() {
		this.restTemplate = new RestTemplate();
	}



	public <T> ResponseEntity<T> performCall(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables) throws Exception {
		logger.debug("Executing Rest call to URI: {} ", url);

		StopWatch stopWatchSoapInvocation = new StopWatch();
		stopWatchSoapInvocation.start();

		ResponseEntity<T> responseEntity = null;
		try {

			responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);

			stopAndLog(true, url, stopWatchSoapInvocation, null);

			if (responseEntity == null) {
				throw new RestClientException("Failed calling URL " + url);
			}

		} catch (Exception e) {

			stopAndLog(false, url, stopWatchSoapInvocation, e);
			throw e;

		} 

		return responseEntity;
	}


	/**
	 * @param ok status of the url call
	 * @param url the url of the rest invokation
	 * @param stopWatchSoapInvocation the stopWatch associated to the current rest invokation
	 * @param e the eventual exception caught.
	 */
	private void stopAndLog(boolean ok, String url, StopWatch stopWatchSoapInvocation, Exception e) {
		if (stopWatchSoapInvocation.isRunning()) {
			stopWatchSoapInvocation.stop();
		}
		final long totalTimeMillis = stopWatchSoapInvocation.getTotalTimeMillis();
		if (ok) {
			 logger.info("Rest Calling to URL {} was executed OK in {} ms", url,
			 totalTimeMillis);
		} else {
			Throwable cause = e != null ? e.getCause() : null;
			String message = e != null ? e.getMessage() : null;
			 logger.error("Error during REST calling. URL {}. Execution in {}ms. Cause: {}, Message: {}, e:{}", url,
			 totalTimeMillis, cause, message, e);

			logger.error("Error calling Rest Web Services for URL: " + url);
		}

	}

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

}
