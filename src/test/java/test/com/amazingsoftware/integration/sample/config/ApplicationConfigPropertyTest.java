package test.com.amazingsoftware.integration.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author al.casula
 * 
 *         This configuration is used to load correct property values for the
 *         test environment passed as Spring Profile.
 *
 */
@Configuration
@PropertySource(value = { "classpath:application-test.properties" })
@Profile("test")
public class ApplicationConfigPropertyTest {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}