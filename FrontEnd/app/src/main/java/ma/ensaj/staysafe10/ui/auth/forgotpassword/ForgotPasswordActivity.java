package ma.ensaj.staysafe10.ui.auth.forgotpassword;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import ma.ensaj.staysafe10.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ForgotPasswordViewModel viewModel;
    private EditText emailEditText;
    private Button resetButton;
    private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        emailEditText = findViewById(R.id.email);
        resetButton = findViewById(R.id.button);
        forgotPassword = findViewById(R.id.forgot_password);
        resetButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            if (!email.isEmpty()) {
                viewModel.resetPassword(email);
            }
        });

        viewModel.getMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}