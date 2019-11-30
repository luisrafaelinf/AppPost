package com.itla.apppost.restevent;

import android.content.Context;

import com.itla.apppost.converter.RestConverter;
import com.itla.apppost.error.ErrorHandler;
import com.itla.apppost.util.UtilsApp;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public final class Get {

	private Get(){}

	public final static <T> ResponseEntity<T> call(Context context, Class<T> classResponse, String uri) {


		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ErrorHandler());
		restTemplate.setMessageConverters(RestConverter.getConverter());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAuthorization(new HttpAuthentication() {
			@Override
			public String getHeaderValue() {
				return "Bearer "+UtilsApp.token(context);
			}
		});

		HttpEntity httpEntity = new HttpEntity(headers);

		return restTemplate.exchange(UtilsApp.uriApi(context) +uri, HttpMethod.GET, httpEntity, classResponse);

	}

}
