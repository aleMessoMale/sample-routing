package test.com.amazingsoftware.integration.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazingsoftware.integration.samples.arch.restinvokation.impl.ExtendedArchRestServiceSupportImpl;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.IRestCountriesUrlBuilder;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.impl.RestCountriesUrlBuilderImpl;
import com.amazingsoftware.integration.samples.rest.service.currency.ICurrencyService;
import com.amazingsoftware.integration.samples.rest.service.currency.helper.CurrencyServiceHelper;
import com.amazingsoftware.integration.samples.rest.service.currency.impl.CurrencyServiceImpl;
import com.amazingsoftware.integration.samples.rest.service.currency.validator.CurrencyServiceValidator;
import com.amazingsoftware.integration.samples.utils.FilterUtils;
import com.amazingsoftware.integration.samples.utils.HttpUtils;

import test.com.amazingsoftware.integration.sample.consts.IntegrationEnv;
import test.com.amazingsoftware.integration.sample.utils.TestUtils;

/**
 * Configuration file for the test Service-layer context.
 * 
 * @author al.casula
 *
 */
@Configuration
@Profile("test")
public class ServiceLayerConfigTest {
	
	@Bean 
	ICurrencyService currencyService(){
		return new CurrencyServiceImpl();
	}
	
	@Bean
	ExtendedArchRestServiceSupportImpl restClient(){
		return new ExtendedArchRestServiceSupportImpl();
	}
	
	@Bean
	HttpUtils httpUtils(){
		return new HttpUtils();
	}
	
	@Bean
	IRestCountriesUrlBuilder restCountriesUrlBuilder(){
		return new RestCountriesUrlBuilderImpl();
	}
	
	@Bean 
	TestUtils testUtils(){
		TestUtils testUtils = new TestUtils();
		return testUtils;
	}
	
	@Bean 
	IntegrationEnv integrationEnv(){
		return new IntegrationEnv();
	}
	
	@Bean 
	CurrencyServiceValidator currencyServiceValidator(){
		return new CurrencyServiceValidator();
	}
	
	@Bean
	FilterUtils filterUtils(){
		return new FilterUtils();
	}
	
	@Bean 
	CurrencyServiceHelper currencyServiceHelper(){
		return new CurrencyServiceHelper();
	}

}
