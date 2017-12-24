package com.amazingsoftware.integration.samples.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * Http utility methods.
 * 
 * @author al.casula
 *
 */
@Component
public class HttpUtils {
	
	public HttpServletRequest getServletRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        return attr.getRequest();
    }
	
	/**
	 * @param parameters the map from which construct the query string section.
	 * @return the query string section, together with ?, given the map with key and value. Returns ?&key1=value1&key2=value2...
	 */
	public String createRequestParameterQueryString(@Nullable Map<String,String> parameters){
		
		if(parameters == null || parameters.size() == 0){
			return "";
		}
		
		
		StringBuilder queryString = new StringBuilder("?");
		
		//TODO: Can be used Streams instead?
		for (Map.Entry<String, String> parameter : parameters.entrySet())
		{
		    queryString.append(parameter.getKey()).append("=").append(parameter.getValue()).append("&");
		}
		
		
		return queryString.substring(0, queryString.length()-1).toString();
		
	}

	public String getHeaderFromIntegrationMessage(@Nullable Message<?> inMessage, String headerConst){

		if (inMessage == null) {
			return "";
		}

		MessageHeaders headers = inMessage.getHeaders();

		return (String)headers.get(headerConst);
	}

}
