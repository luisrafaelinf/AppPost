package com.itla.apppost.apicontroller;

import android.content.Context;
import android.os.AsyncTask;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.response.Post;
import com.itla.apppost.restevent.Get;

import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final class PostWallController extends AsyncTask<Void, Void, ResponseEntity<Post[]>> implements ResponseContent<ResponseEntity<Post[]>> {

	private Context context;
	private Consumer<ResponseEntity<Post[]>> consumer;

	public PostWallController(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(ResponseEntity<Post[]> responseEntity) {
		super.onPostExecute(responseEntity);

		if (Objects.nonNull(consumer)) {
			consumer.accept(responseEntity);
		}

	}

	@Override
	protected ResponseEntity<Post[]> doInBackground(Void... voids) {

		ResponseEntity<Post[]> post = Get.call(context, Post[].class, "post");

		return post;
	}

	@Override
	public void apply(Consumer<ResponseEntity<Post[]>> consumer) {
		this.consumer = consumer;
	}
}
