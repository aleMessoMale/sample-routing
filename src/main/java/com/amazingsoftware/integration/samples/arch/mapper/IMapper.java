package com.amazingsoftware.integration.samples.arch.mapper;

import java.util.List;

/**
 * This class is responsible for mapping Facade to Service DTOs and list of DTO through Streams and viceversa. 
 * 
 * This is necessary to keep different layers as loosely coupled as much as possible.
 * 
 * @author al.casula
 *
 */
public interface IMapper {

	<F, S> S fromFacadeToService(F facadeDomainObject, Class<S> serviceDomainObjectClass)
			throws InstantiationException, IllegalAccessException;

	<F, S> F fromServiceToFacade(S serviceDomainObject, Class<F> facadeDomainObjectClass)
			throws InstantiationException, IllegalAccessException;

	<F, S> List<S> fromFacadeListToServiceList(List<F> facadeDomainObjectList, Class<S> serviceDomainObjectClass);

	<F, S> List<F> fromServiceListToFacadeList(List<S> serviceDomainObjectList, Class<F> facadeDomainObjectClass);

}