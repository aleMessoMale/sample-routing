package com.amazingsoftware.integration.samples.rest.service.currency;

import java.util.List;

import com.amazingsoftware.integration.samples.rest.service.currency.domain.CountryInfoResponse;

/**
 * The facade-layer interface for the Currency Service.
 * 
 * @author al.casula
 *
 */
public interface ICurrencyService {

	/**
	 * Return elements from the cached list (filled at the Spring init phase) paginated according to parameter supplied.
	 * 
	 * 
	 * @param pageNumber number of the page to be returned
	 * @param pageSizeNumber the size of the page to be returned
	 * @return the filtered elements. Return elements from (pageNumber*(pageSizeNumber-1)) to (pageNumber*pageSizeNumber).
	 * If indexes referes to elements not present in the cached list, no elements are returned.
	 * 
	 * @throws Exception
	 */
	List<CountryInfoResponse> getCurrenciesInfo(Integer pageNumber, Integer pageSizeNumber) throws Exception;
	
	/**
	 * 
	 * In case the supplied request contains no need for pagination, this method is called by the facade layer.
	 *  
	 * @return all the info relative to Country and relative currencies. 
	 * @throws Exception
	 */
	List<CountryInfoResponse> getAllCurrenciesInfo() throws Exception;

}