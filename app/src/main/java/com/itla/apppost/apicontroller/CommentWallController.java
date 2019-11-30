package com.itla.apppost.apicontroller;

import android.content.Context;
import android.os.AsyncTask;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.response.Post;
import com.itla.apppost.restevent.Get;

import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final class CommentWallController extends AsyncTask<Integer, Void, ResponseEntity<Post[]>> implements ResponseContent<ResponseEntity<Post[]>> {

	private Context context;
	private Consumer<ResponseEntity<Post[]>> consumer;

	public CommentWallController(Context context) {
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
	protected ResponseEntity<Post[]> doInBackground(Integer... idPost) {

		ResponseEntity<Post[]> post = Get.call(context, Post[].class, "post/"+idPost[0]+"/comment");

		return post;
	}

	@Override
	public void apply(Consumer<ResponseEntity<Post[]>> consumer) {
		this.consumer = consumer;
	}
}
