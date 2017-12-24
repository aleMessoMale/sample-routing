package com.amazingsoftware.integration.samples.arch.errors.builder.impl;

import org.springframework.stereotype.Component;

import com.amazingsoftware.integration.samples.arch.errors.builder.AbstractErrorBuilder;
import com.amazingsoftware.integration.samples.arch.errors.dto.ErrorDto;

@Component
public class MessageErrorBuilderImpl extends AbstractErrorBuilder {

	public ErrorDto buildErrorFromMessage(String message){
		ErrorDto errDto = new ErrorDto();
		
		errDto.setMessage(message);
		return errDto;
	}

}
