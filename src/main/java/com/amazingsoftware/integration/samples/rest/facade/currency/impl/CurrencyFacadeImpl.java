package com.amazingsoftware.integration.samples.rest.facade.currency.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.amazingsoftware.integration.samples.arch.errors.builder.impl.MessageErrorBuilderImpl;
import com.amazingsoftware.integration.samples.consts.BaseServiceConst;
import com.amazingsoftware.integration.samples.consts.BaseServiceConst.HeaderMessageKeys;
import com.amazingsoftware.integration.samples.consts.BaseServiceConst.QueryStringParameters;
import com.amazingsoftware.integration.samples.rest.facade.BaseFacade;
import com.amazingsoftware.integration.samples.rest.facade.currency.ICurrencyFacade;
import com.amazingsoftware.integration.samples.rest.facade.currency.domain.CountryInfoFacadeResponse;
import com.amazingsoftware.integration.samples.rest.facade.currency.domain.CurrencyFacadeResponse;
import com.amazingsoftware.integration.samples.rest.facade.currency.mapper.CurrencyMapper;
import com.amazingsoftware.integration.samples.rest.service.currency.ICurrencyService;
import com.amazingsoftware.integration.samples.utils.HttpUtils;
import com.amazingsoftware.integration.samples.utils.NumberUtil;

@Service("currencyFacade")
public class CurrencyFacadeImpl extends BaseFacade implements ICurrencyFacade {

	Logger logger = LoggerFactory.getLogger(CurrencyFacadeImpl.class);

	@Autowired
	MessageErrorBuilderImpl errorBuilder;

	@Autowired
	ICurrencyService currencyService;

	@Autowired
	HttpUtils httpUtils;

	@Autowired
	CurrencyMapper currencyMapper;
	
	@Override
	@Secured("ROLE_INTEGRATION_REST_USER")
	public GenericMessage<CurrencyFacadeResponse> getCurrenciesInfoForMobile(Message<?> inMessage) throws Exception {
		

		
		return this.getCurrenciesInfoForWeb(inMessage);
		
		
//		return this.getCurrenciesInfoForWeb(inMessage);
	}
	

	@Override
	@Secured("ROLE_INTEGRATION_REST_USER")
	public GenericMessage<CurrencyFacadeResponse> getCurrenciesInfoForWeb(Message<?> inMessage) throws Exception {

		CurrencyFacadeResponse currencyFacadeResponse = this.createResponseFacade(CurrencyFacadeResponse.class, inMessage);
		
		
		Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
		
		/* Get the servlet request */
		HttpServletRequest req = httpUtils.getServletRequest();
		
		/* Get the version from the Message Header */
		String version = httpUtils.getHeaderFromIntegrationMessage(inMessage, HeaderMessageKeys.MESSAGE_HEADER_VERSION);

		logger.info("Called to getCurrenciesInfoFacade with version {}", version);
		try {
		/* Get the pageNumber and pageSizeNumber queryString parameter from the Request */
			Integer pageSizeNumber = NumberUtil
					.checkIntegerNumber(req.getParameter(QueryStringParameters.PAGE_SIZE_PARAM), null);
			Integer pageNumber = NumberUtil.checkIntegerNumber(req.getParameter(QueryStringParameters.PAGE_NUM_PARAM),
					null);
			
			
			if ((pageSizeNumber == null && pageNumber != null) || (pageSizeNumber != null && pageNumber == null)) {
				throw new IllegalArgumentException(BaseServiceConst.ErrorMessages.PARAMETER_NOT_SUPPORTED
						+ BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR + HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			/* If no pagination has been requested, call the getAllCurrenciesInfo service method */
			if (pageSizeNumber == null && pageNumber == null) {
				
				List<CountryInfoFacadeResponse> countryInfoResponseList = currencyMapper.fromServiceListToFacadeList(
						currencyService.getAllCurrenciesInfo(), CountryInfoFacadeResponse.class);
				
				currencyFacadeResponse.setCountryInfoList(countryInfoResponseList);
				
				return new GenericMessage<CurrencyFacadeResponse>(currencyFacadeResponse, responseHeaderMap);
			}
			
			/* In case pagination is requested, call the paginated service layer method */
			if (pageSizeNumber != null && pageNumber != null) {
				
				List<CountryInfoFacadeResponse> countryInfoResponseList = currencyMapper.fromServiceListToFacadeList(
						currencyService.getCurrenciesInfo(pageNumber, pageSizeNumber), CountryInfoFacadeResponse.class);
				
				
				currencyFacadeResponse.setCountryInfoList(countryInfoResponseList);
				
				return new GenericMessage<CurrencyFacadeResponse>(currencyFacadeResponse,
						responseHeaderMap);
			}



			/*
			 * This code should never be reached - IllegalStateException to
			 * track it
			 */
			throw new IllegalStateException(BaseServiceConst.ErrorMessages.ILLEGAL_STATE
					+ BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR + HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (IllegalArgumentException | ParseException i) {
			logger.error("Catched exception from Service Layer, remapping in a Parameter not supporterd Error", i);
			throw new IllegalArgumentException(BaseServiceConst.ErrorMessages.PARAMETER_NOT_SUPPORTED
					+ BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR + HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Catched exception from Service Layer, remapping in a generic error", e);

			throw new Exception(BaseServiceConst.ErrorMessages.GENERIC_ERROR
					+ BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR + HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	@Secured("ROLE_INTEGRATION_REST_USER")
	public void notSupportedChannelErrorManagement(Message<?> inMessage) throws Exception {

		String version = httpUtils.getHeaderFromIntegrationMessage(inMessage, HeaderMessageKeys.MESSAGE_HEADER_VERSION);

		logger.warn("Called wrong {} version", BaseServiceConst.WEB_APP_SERVICE_NAME);
		logger.warn("Called with version {} which is not supported", version);

		throw new IllegalArgumentException(BaseServiceConst.ErrorMessages.CHANNEL_NOT_SUPPORTED + version
				+ BaseServiceConst.MESSAGE_HTTP_STATUS_SEPARATOR + HttpStatus.NOT_FOUND);

	}
}
