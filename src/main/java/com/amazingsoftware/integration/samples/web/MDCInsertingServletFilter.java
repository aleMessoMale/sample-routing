package com.amazingsoftware.integration.samples.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.amazingsoftware.integration.samples.consts.BaseServiceConst;

/**
 * This filter, is responsible for filling the MDC Context for a better logging writing of the app events.
 * 
 * @author al.casula
 *
 */
public class MDCInsertingServletFilter extends ch.qos.logback.classic.helpers.MDCInsertingServletFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(MDCInsertingServletFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		populateMDCContext(httpServletRequest);

		try {
			super.doFilter(request, response, chain);
		} finally {
			cleanupMDC();
		}
	}

	private void cleanupMDC() {
		MDC.remove(BaseServiceConst.Mdc.CALLED_URL);
		MDC.remove(BaseServiceConst.Mdc.SERVICE_NAME);
		MDC.remove(BaseServiceConst.Mdc.OPERATION);
		MDC.remove(BaseServiceConst.Mdc.VERSION);
		MDC.remove(BaseServiceConst.Mdc.CHANNEL);
	}

	
	/**
	 * Should be implemented in a stronger way. Just as a PoC to init MDC Context Correctly.
	 * 
	 * @param httpServletRequest
	 */
	protected void populateMDCContext(HttpServletRequest httpServletRequest) {

		if (httpServletRequest.getRequestURL().toString().split("//").length > 0
				&& httpServletRequest.getRequestURL().toString().split("//")[1].split("/").length > 5) {
			String version = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[4];
			
			
			String channel = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[3];
			String operation = httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[5];

			MDC.put(BaseServiceConst.Mdc.OPERATION, operation);
			MDC.put(BaseServiceConst.Mdc.VERSION, version);
			MDC.put(BaseServiceConst.Mdc.SERVICE_NAME,
					httpServletRequest.getRequestURL().toString().split("//")[1].split("/")[1]);
			MDC.put(BaseServiceConst.Mdc.CHANNEL,
					channel);
		}

	}

	@Override
	public void destroy() {
		super.destroy();
	}

}