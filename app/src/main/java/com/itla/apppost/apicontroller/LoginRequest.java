package com.itla.apppost.apicontroller;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.credential.LoginActivity;
import com.itla.apppost.request.Login;
import com.itla.apppost.response.User;
import com.itla.apppost.restevent.Post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final class LoginRequest extends AsyncTask<Login, Void, ResponseEntity<User>> implements ResponseContent<ResponseEntity<User>> {

	private Context context;
	private Consumer<ResponseEntity<User>> consumer;

	public LoginRequest(LoginActivity loginActivity) {
		this.context = loginActivity.getContext();
	}

	@Override
	protected void onPostExecute(ResponseEntity<User> user) {
		super.onPostExecute(user);

		if (Objects.nonNull(consumer)) {
			consumer.accept(user);
		}

	}

	@Override
	protected ResponseEntity<User> doInBackground(Login... login) {

		ResponseEntity<User> response = Post.<User>call(context, User.class, "login", login[0]);

		return response;
	}


	@Override
	public void apply(Consumer<ResponseEntity<User>> consumer) {
		this.consumer = consumer;
	}
}
