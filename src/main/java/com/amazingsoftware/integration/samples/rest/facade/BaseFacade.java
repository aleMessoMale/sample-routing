package com.amazingsoftware.integration.samples.rest.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;

import com.amazingsoftware.integration.samples.consts.BaseServiceConst;
import com.amazingsoftware.integration.samples.rest.facade.domain.BaseFacadeResponse;
import com.amazingsoftware.integration.samples.utils.HttpUtils;

/**
 * 
 *  This class should contain eventual methods common to any Facade-layer Business Bean.
 * 
 * @author al.casula
 *
 */
public class BaseFacade {
	
	@Autowired
	HttpUtils httpUtils;
	
	@Value("${application.version}")
	private String version;

	public <T extends BaseFacadeResponse>  T createResponseFacade(Class<T> facadeResponseClass, String channel, String version) throws InstantiationException, IllegalAccessException{
		
		T facadeResponse = facadeResponseClass.newInstance();
		
		((BaseFacadeResponse)facadeResponse).setChannel(channel);
		((BaseFacadeResponse)facadeResponse).setVersion(version);
		
		return facadeResponse;
	}
	
	public <T extends BaseFacadeResponse>  T createResponseFacade(Class<T> facadeResponseClass, Message inMessage) throws InstantiationException, IllegalAccessException{
		
		String channel = httpUtils.getHeaderFromIntegrationMessage(inMessage, BaseServiceConst.HeaderMessageKeys.MESSAGE_HEADER_CHANNEL);
		
		return (T) this.createResponseFacade(facadeResponseClass, channel, version);
		
	}
	

}
