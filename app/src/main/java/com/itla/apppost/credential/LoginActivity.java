package com.itla.apppost.credential;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itla.apppost.MainActivity;
import com.itla.apppost.R;
import com.itla.apppost.apicontroller.LoginRequest;
import com.itla.apppost.constant.SessionManager;
import com.itla.apppost.eventform.EventForm;
import com.itla.apppost.request.Login;
import com.itla.apppost.response.User;
import com.itla.apppost.wall.PostWallActivity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


public class LoginActivity extends Fragment implements EventForm<ResponseEntity<User>> {

	private EditText txtUser;
	private EditText txtPass;
	private Button btnLogin;

	private SharedPreferences sharedPreferences;

	public LoginActivity() {


	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_name), getContext().MODE_PRIVATE);

		if (Objects.nonNull(sharedPreferences.getString(SessionManager.TOKEN, null))) {
			moveToPosts();

		}

		View view = inflater.inflate(R.layout.login, container, false);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		txtUser = view.findViewById(R.id.txtUser);
		txtPass = view.findViewById(R.id.txtPass);

		btnLogin = view.findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(this::login);


	}

	private void login(View view) {

		if (!valid()) {
			return;
		}


		LoginRequest task = new LoginRequest(this);
		task.apply(this::fillViewContent);

		Login login = new Login();
		login.setEmail(txtUser.getText().toString().trim());
		login.setPassword(txtPass.getText().toString().trim());

		task.execute(login);
		try {
			ResponseEntity<User> responseEntity = task.get();

			if (responseEntity.getStatusCode() == HttpStatus.CREATED) {

				prepareSharedPreferences(responseEntity);

				moveToPosts();
			}

		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void fillViewContent(@NonNull ResponseEntity<User> user) {

		if (user.getStatusCode() == HttpStatus.BAD_REQUEST) {
			Toast.makeText(getContext(), "Usuario incorrecto", Toast.LENGTH_SHORT)
				.show();

		} else if (user.getStatusCode() == HttpStatus.CREATED) {
			System.out.println(user.getBody().getEmail());
		}

	}

	private void moveToPosts() {
		Intent intent = new Intent(getContext(), PostWallActivity.class);

		startActivity(intent);
		Process.killProcess(Process.myPid()); //Kill de Main Activity
	}

	private void prepareSharedPreferences(ResponseEntity<User> responseEntity) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(SessionManager.TOKEN, responseEntity.getBody().getToken());
		editor.putString(SessionManager.MAIL, responseEntity.getBody().getEmail());
		editor.putString(SessionManager.USER_NAME, responseEntity.getBody().getName());
		editor.putInt(SessionManager.USER_ID, responseEntity.getBody().getId());
		editor.commit();
	}

	private Boolean valid() {

		if (txtUser.getText().toString().trim().isEmpty()) {
			Toast.makeText(getContext(), "Digite el usuario.", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		if (txtPass.getText().toString().trim().isEmpty()) {
			Toast.makeText(getContext(), "Digite la contraseña.", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		return true;
	}

	public void setUserName(String userName) {
		this.txtUser.setText(userName);
	}

	public void setPassword(String password) {
		this.txtPass.setText(password);
	}
}
