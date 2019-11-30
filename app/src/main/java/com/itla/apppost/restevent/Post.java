package com.itla.apppost.restevent;

import android.content.Context;
import android.content.res.Resources;

import com.itla.apppost.R;
import com.itla.apppost.converter.RestConverter;
import com.itla.apppost.error.ErrorHandler;
import com.itla.apppost.request.Register;
import com.itla.apppost.util.UtilsApp;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public final class Post {

	private Post(){}

	public final static <T> ResponseEntity<T> call(Context context, Class<T> classResponse, String uri, Object request) {

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

		HttpEntity httpEntity = new HttpEntity(request, headers);

		return restTemplate.exchange(UtilsApp.uriApi(context) +uri, HttpMethod.POST, httpEntity, classResponse);

	}

}
