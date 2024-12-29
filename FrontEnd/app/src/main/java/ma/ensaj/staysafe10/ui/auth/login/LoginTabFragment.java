package ma.ensaj.staysafe10.ui.auth.login;

import ma.ensaj.staysafe10.utils.SessionManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ma.ensaj.staysafe10.MainActivity;
import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.ui.auth.LoginRegisterActivity;
import ma.ensaj.staysafe10.ui.auth.forgotpassword.ForgotPasswordActivity;

public class LoginTabFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private Button loginButton;
    private EditText email, password;
    private TextView forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        loginButton = root.findViewById(R.id.button);
        forgotPassword = root.findViewById(R.id.forgot_password);

        // Animate views
        animateViews(root);

        // Set login click listener
        loginButton.setOnClickListener(v -> login());

        // Observe login status
        loginViewModel.getLoginStatusLiveData().observe(getViewLifecycleOwner(), token -> {
            // Successful authentication
            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Observe errors
        loginViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
            startActivity(intent);
        });


        return root;
    }

    private void animateViews(View root) {
        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPassword.setTranslationX(800);
        loginButton.setTranslationY(0);
        email.setAlpha(0);
        password.setAlpha(0);
        forgotPassword.setAlpha(0);
        loginButton.setAlpha(0);

        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        forgotPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        loginButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
    }

    private void login() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        loginViewModel.login(emailText, passwordText);
    }
}