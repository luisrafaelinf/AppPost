package com.itla.apppost.error;

import android.util.Log;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public final class ErrorHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {

		return false;//response.getStatusCode().value() >= 400 ;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		HttpStatus statusCode = response.getStatusCode();
		String statusText = response.getStatusText();

		Log.e("Error "+statusCode, statusText);

	}
}
