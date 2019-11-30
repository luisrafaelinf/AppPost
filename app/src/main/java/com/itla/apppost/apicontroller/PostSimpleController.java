package com.itla.apppost.apicontroller;

import android.content.Context;
import android.os.AsyncTask;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.response.Post;
import com.itla.apppost.restevent.Get;

import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final class PostSimpleController extends AsyncTask<Integer, Void, ResponseEntity<Post>> implements ResponseContent<ResponseEntity<Post>> {

	private Context context;
	private Consumer<ResponseEntity<Post>> consumer;

	public PostSimpleController(Context context) {
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
	protected ResponseEntity<Post> doInBackground(Integer... postId) {

		ResponseEntity<Post> response = Get.call(context, Post.class, "post/"+postId[0]);

		return response;
	}

	@Override
	public void apply(Consumer<ResponseEntity<Post>> consumer) {
		this.consumer = consumer;
	}
}
