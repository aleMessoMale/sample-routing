package com.amazingsoftware.integration.samples.rest.service.currency.domain;

import com.amazingsoftware.integration.samples.rest.service.domain.BaseServiceResponse;

public class CurrencyServiceResponse extends BaseServiceResponse {
	
	private CountryInfoServiceResponse countryInfoList;

	public CountryInfoServiceResponse getCountryInfoList() {
		return countryInfoList;
	}

	public void setCountryInfoList(CountryInfoServiceResponse countryInfoList) {
		this.countryInfoList = countryInfoList;
	}
}
