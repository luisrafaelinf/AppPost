package com.itla.apppost.wall;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itla.apppost.R;
import com.itla.apppost.adapter.PostCommentAdapter;
import com.itla.apppost.apicontroller.CommentWallController;
import com.itla.apppost.apicontroller.PostSimpleController;
import com.itla.apppost.apicontroller.interfaces.ResponseContent;
import com.itla.apppost.constant.TypePost;
import com.itla.apppost.eventform.EventForm;
import com.itla.apppost.response.Post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class PostViewActivity extends AppCompatActivity implements EventForm<ResponseEntity<Post>> {

	private SwipeRefreshLayout swipeRefresh;
	private FloatingActionButton btnNewPost;
	private ProgressBar loadingPost;
	private RecyclerView lstPosts;

	private List<Post> posts = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_wall);

		loadingPost = findViewById(R.id.loadingPost);
		swipeRefresh = findViewById(R.id.swipeRefresh);
		btnNewPost = findViewById(R.id.btnNewPost);
		btnNewPost.hide();

		btnNewPost.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
		lstPosts = findViewById(R.id.lstPosts);

		swipeRefresh.setOnRefreshListener(this::refresListener);

		lstPosts.setLayoutManager(new LinearLayoutManager(this));

		Post post = (Post) getIntent().getExtras().getSerializable("currentPost");
		post.setType(TypePost.POST);

		PostCommentAdapter postCommentAdapter = new PostCommentAdapter(posts, getApplicationContext());
		lstPosts.setAdapter(postCommentAdapter);

		posts.add(post);
		postCommentAdapter.notifyItemInserted(0);

		loadingPost.setVisibility(View.VISIBLE);
		findPost();
		loadingPost.setVisibility(View.INVISIBLE);


	}

	@Override
	protected void onPause() {
		super.onPause();
		btnNewPost.hide();
	}

	private void refresListener() {

		findPost();
		swipeRefresh.setRefreshing(false);
	}

	private void findPost() {

		PostSimpleController postSimpleController = new PostSimpleController(this);
		postSimpleController.apply(this::fillViewContent);

		Post post = posts.get(0);
		postSimpleController.execute(post.getId());

	}


	@Override
	public void fillViewContent(@NonNull ResponseEntity<Post> entity) {

		Post post = entity.getBody();

		if (entity.getStatusCode().value() >= HttpStatus.BAD_REQUEST.value()) {
			Toast.makeText(this, "Error al consultar el post", Toast.LENGTH_SHORT)
				.show();
			return;
		}

		posts.clear();
		lstPosts.getAdapter().notifyDataSetChanged();

		post.setType(TypePost.POST);
		posts.add(post);
		lstPosts.getAdapter().notifyItemChanged(0);

		String[] tags = post.getTags();

		if (Objects.nonNull(tags) && tags.length > 0) {

			Post postTags = new Post();
			postTags.setId(post.getId());
			postTags.setType(TypePost.TAG);
			postTags.setTags(post.getTags());
			posts.add(postTags);
			lstPosts.getAdapter().notifyItemInserted(1);
		}

		findComments(post.getId());
	}

	private void findComments(int postId) {

		CommentWallController commentWallController = new CommentWallController(this);
		commentWallController.apply(this::fillComment);
		commentWallController.execute(postId);

	}

	public void fillComment(@NonNull ResponseEntity<Post[]> entity) {

		if (entity.getStatusCode() == HttpStatus.OK) {

			List<Post> comments = Arrays.asList(entity.getBody());
			comments.sort((c1, c2) -> c1.getId() > c2.getId() ? 1 : -1);
			int sizeList = posts.size();

			for (int i = 0; i < comments.size(); i++) {

				Post comment = comments.get(i);
				comment.setType(TypePost.COMMENT);
				posts.add(comment);
				lstPosts.getAdapter().notifyItemInserted(i + sizeList);
			};

		}
	}
}
