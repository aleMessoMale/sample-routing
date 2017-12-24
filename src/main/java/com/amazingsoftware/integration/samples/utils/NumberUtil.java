package com.amazingsoftware.integration.samples.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains all the number utility methods.
 * 
 * @author al.casula
 *
 */
public class NumberUtil {

	private static Logger logger = LoggerFactory.getLogger(NumberUtil.class);

	/**
	 * @param number the number representation to convert into an Integer
	 * @param defaultToReturn the default value to return in case of error.
	 * @return the Integer to return. Can be the default value or the converted value from the provided number.
	 */
	public static Integer checkIntegerNumber(Object number, Integer defaultToReturn)  {
		Integer numberConversion = defaultToReturn;
		if (number != null) {

			try {
				numberConversion = new Integer(number.toString());
			} catch (Exception ex) {
				logger.warn("checkIntegerNumber throws an error, returning default value {}. Error: {}",
						numberConversion, ex);
			} catch (Throwable ex) {
				logger.warn("checkIntegerNumber throws an error, returning default value. Error: {}", ex);
			}

		}
		return numberConversion;
	}

}
