package com.amazingsoftware.integration.samples.utils;

import java.util.Arrays;
import java.util.Objects;

import com.amazingsoftware.integration.samples.arch.aspect.log.LogAspect;

/**
 * Provide useful methods for filtering logs through regex. 
 * 
 * For example to hide password, cv2, iban and so on...
 * 
 * used by {@link LogAspect}
 * 
 * @author al.casula
 *
 */
public class LogFilteringUtils  {

    public static final int MAX_STRING_LENGTH = 100;

    public static String maskResult(Object result) {

        if (result instanceof String && ((String) result).length() > MAX_STRING_LENGTH) {

            return "hidden, exceeded max string length: " + MAX_STRING_LENGTH;
        }

        String s = result.toString();
        return maskFields(s);
    }

    public static String maskArgs(Object[] args) {

        Object[] objects = Arrays.stream(args).map(o -> {

            if (o instanceof String && ((String) o).length() > MAX_STRING_LENGTH) {
                return "hidden, exceeded max string length: " + MAX_STRING_LENGTH;

            }
            if (o != null) {
                return o.toString();
            }
            return o;
        }).filter(Objects::nonNull).toArray();

        String s = Arrays.toString(objects);
        return maskFields(s);

    }

    public static String maskFields(String s) {

        if (s == null) {
            return "";
        } else {
            String replaced2 = s.replaceAll("(?<=\"password\":)\"[^\"]+\"", "\"********\"");

            return replaced2;
        }
    }
    
}
