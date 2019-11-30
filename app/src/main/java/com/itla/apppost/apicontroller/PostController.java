package com.itla.apppost.apicontroller;

import android.content.Context;
import android.os.AsyncTask;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.response.Post;

import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final class PostController extends AsyncTask<Post, Void, ResponseEntity<Post>> implements ResponseContent<ResponseEntity<Post>> {

	private Context context;
	private Consumer<ResponseEntity<Post>> consumer;

	public PostController(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(ResponseEntity<Post> responseEntity) {
		super.onPostExecute(responseEntity);

		if (Objects.nonNull(consumer)) {
			consumer.accept(responseEntity);
		}

	}

	@Override
	protected ResponseEntity<Post> doInBackground(Post... posts) {

		Post post = posts[0];

		ResponseEntity<Post> response = com.itla.apppost.restevent.Post.call(context, Post.class, "post", post);

		return response;
	}

	@Override
	public void apply(Consumer<ResponseEntity<Post>> consumer) {
		this.consumer = consumer;
	}
}
