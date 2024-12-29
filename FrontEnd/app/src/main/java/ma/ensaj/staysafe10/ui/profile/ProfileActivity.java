package ma.ensaj.staysafe10.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.model.User;
import ma.ensaj.staysafe10.ui.auth.user.UserViewModel;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private UserViewModel userViewModel;
    private TextView  userEmail, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_profile1);
        initViews();
         //Ajout de valeurs par défaut pour vérifier que les TextView fonctionnent

        userEmail.setText("Email: Non chargé");
        userPhone.setText("Phone: Non chargé");


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                Log.d(TAG, "Received user update: " + user.getEmail());
                updateUI(user);
            } else {
                Log.d(TAG, "Received null user");
            }
        });

    }

    private void initViews() {

        userEmail = findViewById(R.id.emailUser1);
        userPhone = findViewById(R.id.phoneUser1);

    }

    private void updateUI(User user) {

        if (user.getEmail() != null) {
            userEmail.setText("Email: " + user.getEmail());
        }
        if (user.getPhone() != null) {
            userPhone.setText("Phone: " + user.getPhone());
        }
        Log.d(TAG, "UI updated with user data");
    }
}