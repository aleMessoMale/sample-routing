
package com.amazingsoftware.integration.samples.rest.facade.currency.domain;

import com.amazingsoftware.integration.samples.utils.JsonSerializerWrapperUtil;

/**
 * This object contains the facade layer representation of the currency info.
 * 
 * @author al.casula
 *
 */
public class CurrencyFacade {

	@Override
	public String toString() {
		return JsonSerializerWrapperUtil.toJson(this);
	}

	private String code;

	private String name;
	
	private String symbol;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
