package com.amazingsoftware.integration.samples.config.staging;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 
 * 
 *         This Configuration file points to a specific property file and is
 *         associated to a particular Spring Profile which could be associated
 *         to a particular environment.
 * 
 *         Put in the PropertySource referenced file the correct values for the
 *         relative environment.
 *         
 *         @author al.casula
 */
@Configuration
@PropertySource(value = { "classpath:application-staging.properties" })
@Profile("staging")
public class ApplicationConfigStaging {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}