package com.amazingsoftware.integration.samples.rest.builder.urlcurrency.enums;

import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.IRestCountriesUrlBuilder;
import com.amazingsoftware.integration.samples.utils.LogFilteringUtils;

/**
 * 
 * 
 * This enum contains all the fields that can be recovered from the http://restcountries.eu/rest/v2/all rest EndPoint.
 *
 * used by {@link IRestCountriesUrlBuilder}
 * 
 * @author al.casula
 */
public enum FieldsToRecoverInCountryRest {
	NAME("name"), CURRENCY("currencies"), CAPITAL("capital"), REGION("region"),SUB_REGION("subregion"),POPULATION("population");
	
	private String value;

	FieldsToRecoverInCountryRest(String value) {
	        this.setValue(value);
	 }

	public String getValue() {
		return value;
	}

	private void setValue(String value) {
		this.value = value;
	}
}
