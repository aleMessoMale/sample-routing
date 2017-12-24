package com.amazingsoftware.integration.samples.rest.service.currency.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazingsoftware.integration.samples.consts.BaseServiceConst;

/**
 * @author al.casula
 * 
 *         This class should contain validation methods for received parameters
 *         relative to Service-Layer beans of the Currency Country
 *         Service.
 *
 */
@Component
public class CurrencyServiceValidator {

	Logger logger = LoggerFactory.getLogger(CurrencyServiceValidator.class);

	public void validatePageNumberAndPageSize(Integer pageNumber, Integer pageSizeNumber) {
		if (pageNumber == null || pageSizeNumber == null) {
			logger.error(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_PROVIDED);
			throw new IllegalArgumentException(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_PROVIDED);
		}

		if (pageNumber.compareTo(new Integer("0")) < 0 || pageSizeNumber.compareTo(new Integer("0")) <= 0) {
			logger.error(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_VALID);
			throw new IllegalArgumentException(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_VALID);
		}
	}
}
