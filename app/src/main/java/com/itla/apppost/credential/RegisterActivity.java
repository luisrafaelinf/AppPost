package com.itla.apppost.credential;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.itla.apppost.R;
import com.itla.apppost.apicontroller.RegisterRequest;
import com.itla.apppost.eventform.EventForm;
import com.itla.apppost.request.Register;
import com.itla.apppost.response.UserRegister;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Fragment implements EventForm<ResponseEntity<UserRegister>> {

	private EditText txtName;
	private EditText txtMail;
	private EditText txtPass;
	private EditText txtConfirmPassRegister;
	private Button btnRegister;

	public RegisterActivity() {
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.register, container, false);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		txtName = view.findViewById(R.id.txtName);
		txtMail = view.findViewById(R.id.txtMail);
		txtPass = view.findViewById(R.id.txtPassRegister);
		txtConfirmPassRegister = view.findViewById(R.id.txtConfirmPassRegister);
		btnRegister = view.findViewById(R.id.btnRegister);

		btnRegister.setOnClickListener(this::register);
	}

	private void register(View view) {

		if (!valid()) {
			return;
		}

		RegisterRequest task = new RegisterRequest(this.getContext());
		task.apply(this::fillViewContent);

		Register register = new Register();
		register.setName(txtName.getText().toString().trim());
		register.setEmail(txtMail.getText().toString().trim());
		register.setPassword(txtPass.getText().toString().trim());

		task.execute(register);

	}

	private boolean valid() {

		if (txtName.getText().toString().trim().isEmpty()) {
			Toast.makeText(this.getContext(), "Debe especificar el nombre", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		if (txtMail.getText().toString().trim().isEmpty()) {
			Toast.makeText(this.getContext(), "Debe especificar el correo", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		if (txtPass.getText().toString().trim().isEmpty()) {
			Toast.makeText(this.getContext(), "Debe especificar la contraseña", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		if (!txtPass.getText().toString().trim().equals(txtConfirmPassRegister.getText().toString().trim())) {
			Toast.makeText(this.getContext(), "Contraseña incorrecta. Verifique la confirmación", Toast.LENGTH_SHORT)
				.show();
			return false;
		}

		return true;
	}

	@Override
	public void fillViewContent(@NonNull ResponseEntity<UserRegister> entity) {

		UserRegister body = entity.getBody();

		if (entity.getStatusCode() == HttpStatus.CREATED) {
			Toast.makeText(getContext(), "Usuario registrado correctament", Toast.LENGTH_SHORT)
				.show();
			txtName.setText("");
			txtPass.setText("");
			txtMail.setText("");
			txtConfirmPassRegister.setText("");
			moveToLoginActivity(body);


		} else if (entity.getStatusCode() == HttpStatus.BAD_REQUEST) {

			if (body.getMessage().contains("Duplicate entry")) {
				Toast.makeText(getContext(), "Correo digitado ya existe", Toast.LENGTH_SHORT)
					.show();
			}
		}

	}

	private void moveToLoginActivity(UserRegister userRegister) {

		List<Fragment> fragmentTabs = getFragmentManager().getFragments();

		for (Fragment fragment : fragmentTabs) {

			if (fragment instanceof LoginActivity) {
				TabLayout tabLayout = getActivity().findViewById(R.id.tabCredentials);
				tabLayout.getTabAt(0).select();

				LoginActivity login = (LoginActivity) fragment;
				login.setUserName(userRegister.getEmail());
				login.setPassword(userRegister.getPassword());
				break;
			}

		}
	}
}
