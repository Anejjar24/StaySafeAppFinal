package ma.ensaj.staysafe10.ui.auth.signup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ma.ensaj.staysafe10.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ma.ensaj.staysafe10.R;

public class SignupTabFragment extends Fragment {
    private SignUpViewModel signUpViewModel;
    private EditText email, password, confirmPassword, phone;
    private Button signupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_tab, container, false);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirmPassword = root.findViewById(R.id.confirm_password);
        phone = root.findViewById(R.id.phone);
        signupButton = root.findViewById(R.id.button);

        // Animate views
        animateViews(root);

        // Set signup click listener
        signupButton.setOnClickListener(v -> signUp());

        // Observe signup status
        signUpViewModel.getSignUpLiveData().observe(getViewLifecycleOwner(), user -> {
            Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
        });

        // Observe errors
        signUpViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    private void animateViews(View root) {
        email.setTranslationX(800);
        password.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        phone.setTranslationX(800);
        signupButton.setTranslationY(0);

        email.setAlpha(0);
        password.setAlpha(0);
        confirmPassword.setAlpha(0);
        phone.setAlpha(0);
        signupButton.setAlpha(0);

        phone.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        signupButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
    }

    private void signUp() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();
        String phoneText = phone.getText().toString();

        // Basic validation
        if (!passwordText.equals(confirmPasswordText)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpViewModel.signUp(emailText, passwordText, phoneText);
    }
}