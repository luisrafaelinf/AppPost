package com.itla.apppost.wall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itla.apppost.R;
import com.itla.apppost.adapter.PostAdapter;
import com.itla.apppost.apicontroller.PostWallController;
import com.itla.apppost.eventform.EventForm;
import com.itla.apppost.response.Post;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PostWallActivity extends AppCompatActivity implements EventForm<ResponseEntity<Post[]>> {

	private SwipeRefreshLayout swipeRefresh;
	private ProgressBar loadingPost;
	private RecyclerView lstPosts;
	private FloatingActionButton btnNewPost;
	private List<Post> posts = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_wall);

		swipeRefresh = findViewById(R.id.swipeRefresh);
		loadingPost = findViewById(R.id.loadingPost);
		btnNewPost = findViewById(R.id.btnNewPost);
		lstPosts = findViewById(R.id.lstPosts);
		lstPosts.setLayoutManager(new LinearLayoutManager(this));

		btnNewPost.setOnClickListener(this::createPost);
		swipeRefresh.setOnRefreshListener(this::refresListener);
		swipeRefresh.setColorSchemeResources(R.color.blue_primary);

		loadingPost.setVisibility(View.VISIBLE);
		findPosts();
	}

	private void refresListener() {

		findPosts();
		swipeRefresh.setRefreshing(false);
	}

	private void createPost(View view) {

		final String transitionName = getResources().getString(R.string.fab_transformation_scrim_behavior);
		final Pair<View, String> pair = Pair.create(view.findViewById(R.id.btnNewPost), transitionName);

		ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);

		Intent intent = new Intent(this, PostActivity.class);
		startActivityForResult(intent, 1, activityOptionsCompat.toBundle());

	}

	private void findPosts() {

		PostWallController task = new PostWallController(getApplicationContext());
		task.apply(this::fillViewContent);

		task.execute();

	}

	@Override
	public void fillViewContent(@NonNull ResponseEntity<Post[]> entity) {

		posts.clear();
		posts.addAll(Arrays.asList(entity.getBody()));
		posts.sort((p1, p2) -> p1.getId() < p2.getId() ? 1 : -1 );

		PostAdapter postAdapter = new PostAdapter(posts, this);

		lstPosts.setAdapter(postAdapter);
		postAdapter.notifyDataSetChanged();
		loadingPost.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			Post newPost = (Post) data.getExtras().getSerializable("post");
			posts.add(0, newPost);
			lstPosts.getAdapter().notifyItemInserted(0);
			lstPosts.smoothScrollToPosition(0);
		}

	}
}
