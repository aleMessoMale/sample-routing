package com.amazingsoftware.integration.samples.rest.service.currency.domain;

import java.util.List;

import com.amazingsoftware.integration.samples.rest.service.domain.BaseServiceResponse;
import com.amazingsoftware.integration.samples.utils.JsonSerializerWrapperUtil;

/**
 * This object contains the information of service-layer relative to country and relative currency.
 * 
 * This object is returned to the facade-layer invoker.
 * 
 * @author al.casula
 *
 */
public class CountryInfoServiceResponse  {
	
	@Override
	public String toString() {
		return JsonSerializerWrapperUtil.toJson(this);
	}

	String name;
	
	String capital;
	
	String region;
	
	String subregion;
	
	String population;
	
	private List<Currency> currencies;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}

}
