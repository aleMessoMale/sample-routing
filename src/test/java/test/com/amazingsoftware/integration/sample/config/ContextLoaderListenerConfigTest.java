package test.com.amazingsoftware.integration.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * This configuration is used to set the Integration test Environment.
 * 
 * @author al.casula
 *
 */
@Configuration
@ContextConfiguration(value = { "classpath:application-config.xml" })
@Profile("test")
public class ContextLoaderListenerConfigTest {
	
//	@Bean
//	RestTemplate restTemplate() {
//		RestTemplate restTemplate = new RestTemplate();
//		
//		return restTemplate;
//       
//	}
}