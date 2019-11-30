package com.itla.apppost.apicontroller;

import android.content.Context;
import android.os.AsyncTask;

import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.response.Post;
import com.itla.apppost.restevent.Del;
import com.itla.apppost.restevent.Put;

import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;

public final  class PostLikePlus extends AsyncTask<Post, Void, ResponseEntity> implements ResponseContent<ResponseEntity> {

	private Context context;
	private Consumer<ResponseEntity> consumer;

	public PostLikePlus(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(ResponseEntity responseEntity) {
		super.onPostExecute(responseEntity);

		if (Objects.nonNull(responseEntity)) {
			this.consumer.accept(responseEntity);
		}

	}

	@Override
	protected ResponseEntity doInBackground(Post... posts) {

		Post post = posts[0];
		String url = "post/" + post.getId() + "/like";

		if (post.isLiked()) {
			return Del.call(context, url);
		}
		else {
			return Put.call(context, url);
		}
	}

	@Override
	public void apply(Consumer<ResponseEntity> consumer) {
		this.consumer = consumer;
	}
}
