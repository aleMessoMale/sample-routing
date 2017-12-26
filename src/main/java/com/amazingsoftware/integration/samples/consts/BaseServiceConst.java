package com.amazingsoftware.integration.samples.consts;

/**
 * 
 *         This class contains all the constants for the Service.
 *         Use a different static class for constants which belong to different
 *         functional matters.
 *         
 *         @author al.casula
 */
public class BaseServiceConst {

	public static final String WEB_APP_SERVICE_NAME = "rest-countries-integration";
	
	public static final String REST_COUNTRY_FIELD_SELECTOR = "fields";
	
	public static final String MESSAGE_HTTP_STATUS_SEPARATOR = "###";

	public final class HeaderMessageKeys {
		public static final String MESSAGE_HEADER_VERSION = "version";
		public static final String MESSAGE_HEADER_CHANNEL = "channel";
	}
	
	public final class Channels {
		public static final String WEB_CHANNEL = "web";
		public static final String MOBILE_CHANNEL = "mobile";
	}

	
	/**
	 *
	 * Contains query string values which can be passed to the currency country service put in place.
	 * 
	 * @author al.casula
	 */
	public final class QueryStringParameters {
		public static final String PAGE_SIZE_PARAM = "pageSize";
		public static final String PAGE_NUM_PARAM = "pageNumber";
	}


	/**
	 * Constants for MDC in logs.
	 * 
	 * @author al.casula
	 *
	 */
	public final class Mdc {

		public static final String APP_NAME = "application";
		public static final String SERVICE_NAME = "serviceName";
		public static final String CALLED_URL = "calledUrl";
		public static final String VERSION = "version";
		public static final String OPERATION = "operation";
		public static final String CHANNEL = "channel";
	}

	/**
	 * Constants for different environments.
	 * 
	 * @author al.casula
	 *
	 */
	public final class Environments {
		public static final String PRODUCTION = "production";
		public static final String STAGING = "staging";
		public static final String DEV = "dev";
	}
	
	
	
	/**
	 * Common error messages should be put here.
	 * 
	 * @author al.casula
	 *
	 */
	public final class ErrorMessages {
		public static final String PAGE_SIZE_OR_MESSAGE_NOT_PROVIDED = "Page size or Page number has not been provided.";
		public static final String PAGE_SIZE_OR_MESSAGE_NOT_VALID = "Page size or Page number not Valid.";
		public static final String VERSION_NOT_SUPPORTED = "Version not supported: ";
		public static final String PARAMETER_NOT_SUPPORTED = "Parameter supplied not supported";
		public static final String GENERIC_ERROR = "Generic Error";
		public static final String ILLEGAL_STATE = "Illegal Application State";
		public static final String CHANNEL_NOT_SUPPORTED = "Channel not supported: ";
	}
	
	

}
