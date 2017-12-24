package com.amazingsoftware.integration.samples.arch.errors;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import com.amazingsoftware.integration.samples.arch.errors.builder.impl.MessageErrorBuilderImpl;
import com.amazingsoftware.integration.samples.consts.BaseServiceConst;
import com.amazingsoftware.integration.samples.rest.service.currency.ICurrencyService;
import com.amazingsoftware.integration.samples.utils.HttpUtils;


/**
 * This class is responsible of manage all the exceptions which are routed to the errorChannel defined in {@link application-spring-integration.xml}
 * 
 * At the time of writing, only Currency Country Rest Service errors are routed here, in the handleError method. 
 *
 * @author al.casula
 */
@Service("genericCurrencyServiceErrorManager")
public class GenericCurrencyServiceErrorManager {
	
	@Autowired
	MessageErrorBuilderImpl errorBuilder;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	HttpUtils httpUtils;
    
	Logger logger = LoggerFactory.getLogger(GenericCurrencyServiceErrorManager.class);
	
    /**
     *  * This methos is responsible of manage all the exceptions which are routed to the errorChannel.
     *  
     * @see application-spring-integration.xml
     * @param message the Message which contains the Exception
     * @return the Message which contains the Message containing the exception mapped in the Error Dto of the current Application.
     */
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
    
    private String getHttpStatusCodeFromMessage(String message){
    	String[] comps = message.split(BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR);
    	
    	if(comps != null && comps.length>1){
    		return comps[1];
    	}
    	
    	return HttpStatus.INTERNAL_SERVER_ERROR.toString();
    }
    
    
    private String getErrorMessageFromMessage(String message){
    	String[] comps = message.split(BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR);
    	
    	String exceptionMessageComps[] = comps[0].split(":");
    	
    	if(exceptionMessageComps != null && exceptionMessageComps.length > 1){
    		return exceptionMessageComps[1];
    	}
    	
    	return message;
    	
    }
}