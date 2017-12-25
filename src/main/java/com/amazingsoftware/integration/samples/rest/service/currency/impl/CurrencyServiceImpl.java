package com.amazingsoftware.integration.samples.rest.service.currency.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazingsoftware.integration.samples.arch.restinvokation.impl.ExtendedArchRestServiceSupportImpl;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.IRestCountriesUrlBuilder;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.enums.FieldsToRecoverInCountryRest;
import com.amazingsoftware.integration.samples.rest.service.BaseService;
import com.amazingsoftware.integration.samples.rest.service.currency.ICurrencyService;
import com.amazingsoftware.integration.samples.rest.service.currency.domain.CountryInfoServiceResponse;
import com.amazingsoftware.integration.samples.rest.service.currency.helper.CurrencyServiceHelper;
import com.amazingsoftware.integration.samples.rest.service.currency.validator.CurrencyServiceValidator;
import com.amazingsoftware.integration.samples.utils.HttpUtils;

@Service("currencyService")
public class CurrencyServiceImpl extends BaseService implements ICurrencyService {

	Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

	@Autowired
	ExtendedArchRestServiceSupportImpl restClient;
	
	@Autowired
	CurrencyServiceValidator currencyServiceValidator;

	@Autowired
	HttpUtils httpUtils;

	@Autowired
	IRestCountriesUrlBuilder restCountriesUrlBuilder;
	
	@Autowired
	CurrencyServiceHelper currencyServiceHelper;

	private List<CountryInfoServiceResponse> countryCurrenciesInfoResponse;

	/**
	 * Caching values in init method so that there's no need to explicitly
	 * call the rest service at every invokation of the services methods. We expect these data to change rarely.
	 */
	@PostConstruct
	public void init() {
		countryCurrenciesInfoResponse = fillCountriesCurrenciesInfo();
	}

	protected List<CountryInfoServiceResponse> getCountryCurrenciesInfoResponse() {
		if (countryCurrenciesInfoResponse == null) {
			countryCurrenciesInfoResponse = fillCountriesCurrenciesInfo();
		}

		return countryCurrenciesInfoResponse;
	}
	
	@Override
	public List<CountryInfoServiceResponse> getAllCurrenciesInfo() throws Exception {
		
		Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
				
		return getCountryCurrenciesInfoResponse();
	}

	@Override
	public List<CountryInfoServiceResponse> getCurrenciesInfo(Integer pageNumber, Integer pageSizeNumber)
			throws Exception {

		/* Parameter validation first */
		currencyServiceValidator.validatePageNumberAndPageSize(pageNumber, pageSizeNumber);

		logger.info("getCurrenciesInfo called for page: {} and size: {}", pageNumber, pageSizeNumber);
		Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
		
		/* If parameters are ok, call the helper method and return the filtered elements as requested */
		return currencyServiceHelper.getPaginatedValuesForCurrency(getCountryCurrenciesInfoResponse(), (pageNumber - 1) * pageSizeNumber, pageSizeNumber * pageNumber);
		

	}

	/**
	 * This method is used to fill a list from the source rest service so that it can be used as cache is subsequent calls.
	 * @return the List obtained from the call to source rest service.
	 */
	private List<CountryInfoServiceResponse> fillCountriesCurrenciesInfo() {

		HttpHeaders restHeaders = new HttpHeaders();
		restHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<?> httpEntity = new HttpEntity<>(restHeaders);

		/* Build the url with currency and name fields */
		String url = restCountriesUrlBuilder.buildFullUrl(FieldsToRecoverInCountryRest.CURRENCY,
				FieldsToRecoverInCountryRest.NAME);

		ParameterizedTypeReference<List<CountryInfoServiceResponse>> listOfCountryResponse = new ParameterizedTypeReference<List<CountryInfoServiceResponse>>() {
		};

		ResponseEntity<List<CountryInfoServiceResponse>> exchange;

		try {
			/* perform rest call with a centralized method so that success, error and execution time are treated uniformally */
			exchange = restClient.performCall(url, HttpMethod.GET, httpEntity, listOfCountryResponse);
		} catch (Exception e) {
			logger.warn("Problems filling up Currencies Info. I will try again at first invokation");
			return null;
		}

		List<CountryInfoServiceResponse> countryListResponse = exchange != null ? exchange.getBody()
				: new ArrayList<CountryInfoServiceResponse>();

		logger.info("Currencies Info has been filled up correctly");

		return countryListResponse;
	}



}
