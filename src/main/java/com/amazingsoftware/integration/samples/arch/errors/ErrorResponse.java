package com.amazingsoftware.integration.samples.arch.errors;

import com.amazingsoftware.integration.samples.arch.errors.dto.ErrorDto;

public class ErrorResponse {
	public ErrorDto getError() {
		return error;
	}

	public void setError(ErrorDto error) {
		this.error = error;
	}

	private ErrorDto error;
}
