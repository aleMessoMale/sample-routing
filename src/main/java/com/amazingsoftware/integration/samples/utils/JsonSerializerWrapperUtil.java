package com.amazingsoftware.integration.samples.utils;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * Utility method for JSON serialization\deserialization.
 * 
 * @author al.casula
 *
 */
public class JsonSerializerWrapperUtil {

	private static Log logger = LogFactory.getLog(JsonSerializerWrapperUtil.class);

    private static ObjectMapper mapperToJsonInit() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.setFailOnUnknownId(false);
        mapper.setFilterProvider(simpleFilterProvider);
        return mapper;
    }

    private static ObjectMapper mapperToObjectInit() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.setFailOnUnknownId(false);
        mapper.setFilterProvider(simpleFilterProvider);
        return mapper;
    }

    private static ObjectMapper mapperToJson = mapperToJsonInit();
    private static ObjectMapper mapperToObject = mapperToObjectInit();


    private JsonSerializerWrapperUtil() {
    }


    public static String toJson(Object object) {

        String s = null;
        try {
            s = mapperToJson.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Can not convert to json: {}", e);
        }
        return s;
    }

    public static <T> T toObject(String jsonInString, Class<T> aClass) {

        T object = null;
        try {
            object = mapperToObject.readValue(jsonInString.getBytes(), aClass);
        } catch (IOException e) {
            logger.error("Can not construct object from json {}", e);
        }
        return object;
    }
}
