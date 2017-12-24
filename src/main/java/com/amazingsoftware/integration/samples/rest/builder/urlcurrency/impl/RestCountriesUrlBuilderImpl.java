package com.amazingsoftware.integration.samples.rest.builder.urlcurrency.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazingsoftware.integration.samples.consts.BaseServiceConst;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.IRestCountriesUrlBuilder;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.enums.FieldsToRecoverInCountryRest;
import com.amazingsoftware.integration.samples.utils.HttpUtils;


@Component
public class RestCountriesUrlBuilderImpl implements IRestCountriesUrlBuilder {

	@Autowired
	HttpUtils httpUtils;


	@Value("${endpoint.rest.countries}")
	private String restCountriesBaseEndPoint;


	@Override
	public String buildFullUrl(FieldsToRecoverInCountryRest... fieldsToRecover) {

		StringBuilder fullUrlSb = new StringBuilder(restCountriesBaseEndPoint);
		StringBuilder fieldsToRecoverValuesSb = new StringBuilder();

		String requestParamerQueryString = "";

		if (fieldsToRecover != null) {
			for (FieldsToRecoverInCountryRest field : fieldsToRecover) {
				fieldsToRecoverValuesSb.append(field.getValue()).append(";");
			}

			HashMap<String, String> queryParameters = new HashMap<>();
			queryParameters.put(BaseServiceConst.REST_COUNTRY_FIELD_SELECTOR,
					fieldsToRecoverValuesSb.substring(0, fieldsToRecoverValuesSb.length() - 1).toString());
			requestParamerQueryString = httpUtils.createRequestParameterQueryString(queryParameters);
		}

		return fullUrlSb.append(requestParamerQueryString).toString();

	}

}
