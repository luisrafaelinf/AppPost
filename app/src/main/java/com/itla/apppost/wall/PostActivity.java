package com.itla.apppost.wall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itla.apppost.R;
import com.itla.apppost.apicontroller.PostController;
import com.itla.apppost.eventform.EventForm;
import com.itla.apppost.response.Post;
import com.itla.apppost.util.UtilsApp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class PostActivity extends AppCompatActivity implements EventForm<ResponseEntity<Post>> {

	private EditText txtTitleNewPost;
	private EditText txtContentNewPost;
	private EditText txtTagNewPost;
	private Button btnCreatePost;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.post);

		txtTitleNewPost = findViewById(R.id.txtTitleNewPost);
		txtContentNewPost = findViewById(R.id.txtContentNewPost);
		txtTagNewPost = findViewById(R.id.txtTagNewPost);
		btnCreatePost = findViewById(R.id.btnCreatePost);

		btnCreatePost.setOnClickListener(this::createPost);

	}

	private void createPost(View view) {

		if (!valid()) {
			return;
		}

		PostController postController = new PostController(this.getApplicationContext());
		postController.apply(this::fillViewContent);

		Post post = new Post();
		post.setTitle(txtTitleNewPost.getText().toString().trim());
		post.setBody(txtContentNewPost.getText().toString().trim());
		post.setTags(txtTagNewPost.getText().toString().trim().split(","));
		post.setUserId(UtilsApp.userId(getApplicationContext()));

		postController.execute(post);

	}

	private Boolean valid() {

		if (txtTitleNewPost.getText().toString().trim().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Debe especificar el título del post", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		if (txtContentNewPost.getText().toString().trim().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Debe especificar el contenido del post", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		if (txtTagNewPost.getText().toString().trim().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Debe especificar al menos un tag", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		return true;

	}

	@Override
	public void fillViewContent(@NonNull ResponseEntity<Post> entity) {

		if (entity.getStatusCode() == HttpStatus.CREATED) {
			Toast.makeText(getBaseContext(), "Post creado correctamente", Toast.LENGTH_SHORT)
				.show();

			txtTitleNewPost.setText("");
			txtContentNewPost.setText("");
			txtTagNewPost.setText("");

			Intent intent = new Intent();
			intent.putExtra("post", entity.getBody());
			setResult(Activity.RESULT_OK, intent);

			supportFinishAfterTransition();
			//finish();

		} else if (entity.getStatusCode().value() >= HttpStatus.BAD_REQUEST.value()) {

				Toast.makeText(getBaseContext(), "Error al crear el post", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
