package com.amazingsoftware.integration.samples.rest.facade.currency.domain;

import java.util.List;

import com.amazingsoftware.integration.samples.rest.facade.domain.BaseFacadeResponse;
import com.amazingsoftware.integration.samples.utils.JsonSerializerWrapperUtil;
/**
 * This object contains the information of facade-layer relative to country and relative currency.
 * 
 * This object is returned to the client.
 * 
 * @author al.casula
 *
 */
public class CountryInfoFacadeResponse {
	
	@Override
	public String toString() {
		return JsonSerializerWrapperUtil.toJson(this);
	}

	String name;
	
	String capital;
	
	String region;
	
	String subregion;
	
	String population;
	
	private List<CurrencyFacade> currencyFacades;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<CurrencyFacade> getCurrencies() {
		return currencyFacades;
	}

	public void setCurrencies(List<CurrencyFacade> currencyFacades) {
		this.currencyFacades = currencyFacades;
	}

}
