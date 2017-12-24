package test.com.amazingsoftware.integration.sample.consts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This bean contains all the values set for the Integration Environment in the relative property file.
 * 
 * 
 * @author al.casula
 *
 */
@Component
public class IntegrationEnv {

	public void setBaseHost(String baseHost) {
		this.baseHost = baseHost;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setServicesPrefix(String servicesPrefix) {
		this.servicesPrefix = servicesPrefix;
	}

	public void setCurrencyBaseAppContextPath(String currencyBaseAppContextPath) {
		this.currencyBaseAppContextPath = currencyBaseAppContextPath;
	}

	public void setCurrencyVersion(String currencyVersion) {
		this.currencyVersion = currencyVersion;
	}

	@Value("${integration.env.basehost}")
	private String baseHost;
	
	public String getBaseHost() {
		return baseHost;
	}

	public String getPort() {
		return port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getServicesPrefix() {
		return servicesPrefix;
	}

	public String getCurrencyBaseAppContextPath() {
		return currencyBaseAppContextPath;
	}
	
	public String getCurrencyVersion() {
		return currencyVersion;
	}

	@Value("${integration.env.port}")
	private String port;
	
	@Value("${integration.env.context-path}")
	private String contextPath;
	
	@Value("${integration.env.service-prefix}")
	private String servicesPrefix;
	
	@Value("${integration.env.base-app-context-path}")
	private String currencyBaseAppContextPath;
	
	@Value("${integration.env.currency.version}")
	private String currencyVersion;
	
}
