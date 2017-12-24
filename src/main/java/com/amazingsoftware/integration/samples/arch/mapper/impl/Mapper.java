package com.amazingsoftware.integration.samples.arch.mapper.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.amazingsoftware.integration.samples.arch.mapper.IMapper;

public abstract class Mapper implements IMapper {
	
	/* (non-Javadoc)
	 * @see com.amazingsoftware.integration.samples.arch.mapper.impl.IMapper#fromFacadeToService(F, java.lang.Class)
	 */
	@Override
	public <F,S> S fromFacadeToService(F facadeDomainObject, Class<S> serviceDomainObjectClass) throws InstantiationException, IllegalAccessException{
		S serviceDomainObject = serviceDomainObjectClass.newInstance();
		BeanUtils.copyProperties(facadeDomainObject, serviceDomainObject);
		
		return serviceDomainObject;
	}
	
	/* (non-Javadoc)
	 * @see com.amazingsoftware.integration.samples.arch.mapper.impl.IMapper#fromServiceToFacade(S, java.lang.Class)
	 */
	@Override
	public <F,S> F fromServiceToFacade(S serviceDomainObject, Class<F> facadeDomainObjectClass) throws InstantiationException, IllegalAccessException{
		F facadeDomainObject = facadeDomainObjectClass.newInstance();
		BeanUtils.copyProperties(serviceDomainObject, facadeDomainObject);
		
		return facadeDomainObject;
	}
	
	/* (non-Javadoc)
	 * @see com.amazingsoftware.integration.samples.arch.mapper.impl.IMapper#fromFacadeListToServiceList(java.util.List, java.lang.Class)
	 */
	@Override
	public <F,S> List<S> fromFacadeListToServiceList(List<F> facadeDomainObjectList, Class<S> serviceDomainObjectClass) {
		return facadeDomainObjectList.stream().map(f -> {try {
			return fromFacadeToService(f,serviceDomainObjectClass);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;}).filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see com.amazingsoftware.integration.samples.arch.mapper.impl.IMapper#fromServiceListToFacadeList(java.util.List, java.lang.Class)
	 */
	@Override
	public <F,S> List<F> fromServiceListToFacadeList(List<S> serviceDomainObjectList, Class<F> facadeDomainObjectClass) {
		return serviceDomainObjectList.stream().map(s -> {try {
			return fromServiceToFacade(s,facadeDomainObjectClass);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;}).filter(Objects::nonNull).collect(Collectors.toList());
	}
	
}
