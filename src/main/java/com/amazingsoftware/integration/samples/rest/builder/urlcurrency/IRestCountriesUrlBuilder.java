package com.amazingsoftware.integration.samples.rest.builder.urlcurrency;

import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.enums.FieldsToRecoverInCountryRest;


/** * 
 * 
 *
 * This class is responsible of creating an URL for call the http://restcountries.eu/rest/v2/all rest EndPoint.
 * 
 * Given the list of fields to recover of the enum {@link FieldsToRecoverInCountryRest}, return the correct url to invoke.
 * 
 * @author al.casula
 */
public interface IRestCountriesUrlBuilder {

	/**
	 * @param fieldsToRecover the fields to recover according to {@link FieldsToRecoverInCountryRest} enum.
	 * @return the whole URL to call the restcountries URL endpoint.
	 */
	String buildFullUrl(FieldsToRecoverInCountryRest... fieldsToRecover);

}