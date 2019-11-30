package com.itla.apppost.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.itla.apppost.R;
import com.itla.apppost.apicontroller.PostLikePlus;
import com.itla.apppost.eventform.EventForm;
import com.itla.apppost.response.Post;
import com.itla.apppost.util.DateUtil;
import com.itla.apppost.wall.PostViewActivity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EventForm<ResponseEntity> {

	protected List<Post> posts;
	protected Context context;
	int lastPosition = -1;

	private TextView txtLikesChange;
	private ImageButton btnLikeChange;
	private Boolean liked;
	private Post post;

	public PostAdapter(List<Post> posts, Context context) {
		this.posts = posts;
		this.context = context;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);

		return new RecyclerView.ViewHolder(view) {};
	}


	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		final View itemView = holder.itemView;
		final Post post = posts.get(holder.getAdapterPosition());

		final CardView cardPost = itemView.findViewById(R.id.cardPost);
		final TextView txtTitlePost = itemView.findViewById(R.id.txtTitlePost);
		final TextView txtPostContent = itemView.findViewById(R.id.txtPostContent);
		final TextView txtAuthor = itemView.findViewById(R.id.txtAuthor);
		final TextView txtDatePost = itemView.findViewById(R.id.txtDatePost);
		final TextView txtComments = itemView.findViewById(R.id.txtComments);
		final TextView txtLikes = itemView.findViewById(R.id.txtLikes);
		final TextView txtVisibility = itemView.findViewById(R.id.txtVisibility);
		final ImageButton btnLike = itemView.findViewById(R.id.btnLike);

		cardPost.setOnClickListener(e -> viewPost(e, post));

		txtTitlePost.setText(post.getTitle());
		txtPostContent.setText(post.getBody());
		txtAuthor.setText(post.getUserName());

		String dateTime = DateUtil.longToDateTimeFormartter(Long.parseLong(post.getCreatedAt()));
		txtDatePost.setText(dateTime);

		txtComments.setText(post.getComments());

		if (post.isLiked()) {
			btnLike.setImageResource(R.drawable.plus_one);
		} else {
			btnLike.setImageResource(R.drawable.minus_one);
		}

		txtLikes.setText(post.getLikes() + "");
		txtVisibility.setText(post.getViews()+"");

		btnLike.setOnClickListener(e -> {
			this.btnLikeChange = btnLike;
			this.txtLikesChange = txtLikes;
			this.liked = post.isLiked();
			this.post = post;
			likePost();
		});

	}

	protected void viewPost(View view, Post post) {

		final String transitionName = context.getResources().getString(R.string.fab_transformation_scrim_behavior);
		final Pair<View, String> pair = Pair.create(view, transitionName);

		Activity activity = (AppCompatActivity) context;
		ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);

		Bundle bundle = new Bundle();
		bundle.putSerializable("currentPost", post);

		Intent intent = new Intent(context, PostViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(bundle);
		context.startActivity(intent, activityOptionsCompat.toBundle());

	}

	private void likePost() {

		PostLikePlus likePlus = new PostLikePlus(context);
		likePlus.apply(this::fillViewContent);
		likePlus.execute(post);

	}

	@Override
	public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
		super.onViewDetachedFromWindow(holder);
		holder.itemView.clearAnimation();

	}

	@Override
	public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
		super.onViewAttachedToWindow(holder);

		if (holder.getLayoutPosition() > lastPosition) {
			TranslateAnimation animation = new TranslateAnimation(0, 0, 1400, 0);
			animation.setDuration(600);
			animation.setRepeatCount(0);
			animation.setInterpolator(new DecelerateInterpolator());
			holder.itemView.startAnimation(animation);
		}

		lastPosition = holder.getLayoutPosition();

	}

	@Override
	public int getItemCount() {
		return posts.size();
	}

	@Override
	public void fillViewContent(@NonNull ResponseEntity entity) {

		if (entity.getStatusCode().value() >= HttpStatus.BAD_REQUEST.value()) {
			Toast.makeText(context, "Error al asignar like", Toast.LENGTH_SHORT)
				.show();
			return;
		}

		exangeLike();

	}

	private void exangeLike() {

		int likes = Integer.parseInt(txtLikesChange.getText().toString());

		if (this.liked) {
			this.post.setLiked(false);
			this.btnLikeChange.setImageResource(R.drawable.minus_one);
			this.txtLikesChange.setText((likes - 1)+"");
		} else {
			this.post.setLiked(true);
			this.btnLikeChange.setImageResource(R.drawable.plus_one);
			this.txtLikesChange.setText((likes + 1)+"");
		}

		this.btnLikeChange = null;
		this.txtLikesChange = null;
		this.liked = null;
		this.post = null;
	}
}
