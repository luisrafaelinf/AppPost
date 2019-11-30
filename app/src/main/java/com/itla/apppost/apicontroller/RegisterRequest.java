package com.itla.apppost.apicontroller;

import android.content.Context;
import android.os.AsyncTask;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.credential.RegisterActivity;
import com.itla.apppost.request.Register;
import com.itla.apppost.response.UserRegister;
import com.itla.apppost.restevent.Post;

import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final class RegisterRequest extends AsyncTask<Register, Void, ResponseEntity<UserRegister>> implements ResponseContent<ResponseEntity<UserRegister>> {

	private Context context;
	private Consumer<ResponseEntity<UserRegister>> consumer;

	public RegisterRequest(Context context) {
		this.context = context;
	}

	@Override
	protected ResponseEntity<UserRegister> doInBackground(Register... registers) {
		ResponseEntity<UserRegister> response = Post.call(context, UserRegister.class, "register", registers[0]);
		return response;
	}

	@Override
	protected void onPostExecute(ResponseEntity<UserRegister> response) {
		super.onPostExecute(response);

		if (Objects.nonNull(consumer)) {
			consumer.accept(response);
		}

	}

	@Override
	public void apply(Consumer<ResponseEntity<UserRegister>> consumer) {
		this.consumer = consumer;
	}
}
