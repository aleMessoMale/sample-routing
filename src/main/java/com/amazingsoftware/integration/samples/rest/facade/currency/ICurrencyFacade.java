package com.amazingsoftware.integration.samples.rest.facade.currency;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.amazingsoftware.integration.samples.rest.facade.currency.domain.CountryInfoResponseFacade;

/**
 * The service-layer interface for the Currency Service.
 * 
 * @author al.casula
 *
 */
public interface ICurrencyFacade {

	/**
	 * This method is exposed as service activator in case a call to the
	 * /services/{version}/currency-countries-info endpoint need to be managed.
	 * 
	 * @param inMessage input Message
	 * @return Message containing info for the country and relative currency 
	 * @throws Exception
	 */
	GenericMessage<List<CountryInfoResponseFacade>> getCurrenciesInfo(Message<?> inMessage) throws Exception;

	
	/**
	 * This method is exposed as service activator in case a not supported version of the country currency service is invoked.
	 * 
	 * @param inMessage Input message
	 * @throws Exception
	 */
	void notSupportedVersionErrorManagement(Message<?> inMessage) throws Exception;
}