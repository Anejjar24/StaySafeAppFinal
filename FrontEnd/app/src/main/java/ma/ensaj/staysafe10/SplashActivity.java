package ma.ensaj.staysafe10;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import ma.ensaj.staysafe10.ui.auth.LoginRegisterActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Cacher la barre d'action
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Attendre 7 secondes avant de lancer l'activité suivante
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000); // 7 secondes
                    Intent intent = new Intent(SplashActivity.this, LoginRegisterActivity.class); // Remplacez MainActivity par votre activité principale
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

         */
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 7000);
    }
}
