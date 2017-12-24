package com.amazingsoftware.integration.samples.arch.restinvokation;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface ExtendedArchRestServiceSupport {
	/**
	 * Returns ResponseEntity 
	 *
	 * @param url
	 *            rest endpoint
	 * @param method
	 *            Http Methods.
	 * @param requestEntity
	 *            headers and body
	 * @param responseType
	 *            Expected object type on positive 200 return
	 * @param uriVariables
	 * @param <T>
	 *            any class
	 * @return the response of the call to the Rest URI.
	 * @throws Exception
	 */
	  <T> ResponseEntity<T> performCall(String url, HttpMethod method, HttpEntity<?> requestEntity,  ParameterizedTypeReference<T> responseType, Object... uriVariables) throws Exception;
}
