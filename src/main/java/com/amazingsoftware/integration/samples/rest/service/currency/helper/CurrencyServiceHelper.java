package com.amazingsoftware.integration.samples.rest.service.currency.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazingsoftware.integration.samples.rest.service.currency.domain.CountryInfoResponse;
import com.amazingsoftware.integration.samples.utils.FilterUtils;

/**
 * @author al.casula
 * 
 * This class should contain helper methods for Service-Layer beans, relative to the Currency Country Service.
 *
 */
@Component
public class CurrencyServiceHelper {
	
	@Autowired
	FilterUtils filterUtils;
	
	public List<CountryInfoResponse> getPaginatedValuesForCurrency(List<CountryInfoResponse> listToFilter, int initialIndex, int lastIndex){
		return filterUtils.filterArrayListForIndex(listToFilter, initialIndex, lastIndex);
	}
	

}
