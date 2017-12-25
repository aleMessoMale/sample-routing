package com.amazingsoftware.integration.samples.rest.facade.currency.domain;

import java.util.ArrayList;
import java.util.List;

import com.amazingsoftware.integration.samples.rest.facade.domain.BaseFacadeResponse;

public class CurrencyFacadeResponse extends BaseFacadeResponse {
	
	private List<CountryInfoFacadeResponse> countryInfoList;

	public List<CountryInfoFacadeResponse> getCountryInfoList() {
		return countryInfoList;
	}

	public void setCountryInfoList(List<CountryInfoFacadeResponse> countryInfoList) {
		this.countryInfoList = countryInfoList;
	}

}
