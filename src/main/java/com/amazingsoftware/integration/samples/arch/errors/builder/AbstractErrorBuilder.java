package com.amazingsoftware.integration.samples.arch.errors.builder;

import com.amazingsoftware.integration.samples.arch.errors.dto.ErrorDto;
import com.amazingsoftware.integration.samples.utils.JsonSerializerWrapperUtil;



/**
 * 
 * This class is responsible of creating an ErrorDto from a error message. 
 * 
 * Error Dto contains a structure which should be shared with consumers (clients).
 *
 * @author al.casula
 */
public abstract class AbstractErrorBuilder {
	
	
	/**
	 * @param message the error message 
	 * @return an ErrorDto with a structure pre-shared with client.
	 */
	public abstract ErrorDto buildErrorFromMessage(String message);
	
	public String toJson(){
		return JsonSerializerWrapperUtil.toJson(this);
	}

}
