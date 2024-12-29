package ma.ensaj.staysafe10;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ma.ensaj.staysafe10.ui.contacts.ContactActivity;
import ma.ensaj.staysafe10.ui.location.MapsActivity2;
import ma.ensaj.staysafe10.ui.profile.ProfileActivity;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.staysafe10.model.Contact;
import ma.ensaj.staysafe10.ui.contacts.viewmodel.ContactViewModel;
import ma.ensaj.staysafe10.ui.riskzone.service.DangerDetectionService;
import ma.ensaj.staysafe10.ui.tracking.LocationService;
import ma.ensaj.staysafe10.utils.EmergencyManager;
import ma.ensaj.staysafe10.utils.EmergencyManager2;
import ma.ensaj.staysafe10.utils.ShakeDetector;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String CHANNEL_ID = "counter_channel";
    private static final String LOCATION_CHANNEL_ID = "location";

    // UI Elements
    private ImageView goToProfile, goToMap, goToContacts;
    private Button btnShakeDetection;
    private ImageView btnSOS;

    // Sensors
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    // Emergency Management
    private EmergencyManager emergencyManager;
    private EmergencyManager2 emergencyManage2;
    private ContactViewModel contactViewModel;
    private List<Contact> emergencyContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSensors();
        initEmergencySystem();
        setupNavigationListeners();
        setupEmergencyButton();
        createLocationNotificationChannel();
        createDangerNotificationChannel();
        startLocationService();
        startDangerDetectionService();
        setupbtnSOS();
    }
    private void startDangerDetectionService() {
        Log.d(TAG, "Attempting to start danger detection service");
        if (checkLocationPermissions()) {
            Intent serviceIntent = new Intent(this, DangerDetectionService.class);
            serviceIntent.setAction(DangerDetectionService.ACTION_START);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(TAG, "Starting foreground danger detection service");
                    startForegroundService(serviceIntent);
                } else {
                    Log.d(TAG, "Starting normal danger detection service");
                    startService(serviceIntent);
                }
                Log.d(TAG, "Danger detection service start command sent successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error starting danger detection service: ", e);
            }
        } else {
            Log.d(TAG, "Location permissions not granted for danger detection");
            requestLocationPermissions();
        }
    }

    private void createDangerNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Creating danger notification channel");
                NotificationChannel channel = new NotificationChannel(
                        "danger_alerts",
                        "Alertes de danger",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Notifications pour les zones dangereuses");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0, 500, 200, 500});
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Danger notification channel created successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating danger notification channel: ", e);
        }
    }



    private boolean checkLocationPermissions() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
                startDangerDetectionService();

            } else {
                Toast.makeText(this, "Location permissions are required for tracking",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void initViews() {
        goToProfile = findViewById(R.id.button2);
        goToMap = findViewById(R.id.button3);
        goToContacts = findViewById(R.id.contact);
        btnShakeDetection=findViewById(R.id.btnShakeDetection);
        btnSOS=findViewById(R.id.btnSOS);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(this::onDeviceShaken);

        if (accelerometer == null) {
            Toast.makeText(this, "Votre appareil ne dispose pas d'accéléromètre",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initEmergencySystem() {
        emergencyManager = new EmergencyManager(this);
        emergencyManage2 = new EmergencyManager2(this);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        emergencyContacts = new ArrayList<>();

        // Observer pour les contacts
        contactViewModel.getContacts().observe(this, contacts -> {
            if (contacts != null && !contacts.isEmpty()) {
                Log.d(TAG, "Contacts chargés: " + contacts.size());
                emergencyContacts = contacts;
            }
        });

        // Observer pour les erreurs
        contactViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Erreur: " + error, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Erreur de chargement des contacts: " + error);
            }
        });
    }

    private void setupNavigationListeners() {
        goToProfile.setOnClickListener(v -> {
            try {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } catch (Exception e) {
                Log.e(TAG, "Error starting ProfileActivity", e);
                Toast.makeText(this, "Erreur lors de l'ouverture du profil", Toast.LENGTH_SHORT).show();
            }
        });

        goToMap.setOnClickListener(v -> {
            try {
                startActivity(new Intent(MainActivity.this, MapsActivity2.class));
            } catch (Exception e) {
                Log.e(TAG, "Error starting MapsActivity", e);
                Toast.makeText(this, "Erreur lors de l'ouverture de la carte", Toast.LENGTH_SHORT).show();
            }
        });

        goToContacts.setOnClickListener(v -> {
            try {
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
            } catch (Exception e) {
                Log.e(TAG, "Error starting ContactActivity", e);
                Toast.makeText(this, "Erreur lors de l'ouverture des contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onDeviceShaken() {
        Log.d(TAG, "Secousse détectée!");
        if (emergencyContacts != null && !emergencyContacts.isEmpty()) {
            Toast.makeText(this, "Envoi des alertes d'urgence...", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Nombre de contacts d'urgence: " + emergencyContacts.size());
            emergencyManager.handleEmergency(emergencyContacts);
        } else {
            Toast.makeText(this, "Aucun contact d'urgence disponible", Toast.LENGTH_LONG).show();
            Log.w(TAG, "Aucun contact d'urgence trouvé");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(shakeDetector, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (accelerometer != null) {
            sensorManager.unregisterListener(shakeDetector);
        }
    }

    private void setupEmergencyButton() {
        btnShakeDetection.setOnClickListener(v -> {
            Log.d(TAG, "Bouton d'urgence pressé");
            emergencyManager.handleEmergency(emergencyContacts);
            showNotification();
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Counter Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                mainIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo_non_transparent)
                .setContentTitle("Danger!")
                .setContentText("bouton SOS cliqué! êtes-vous en danger?")
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        notificationManager.notify(1, builder.build());
    }



    private void startLocationService() {
        Log.d(TAG, "Attempting to start location service");
        // Check for location permissions
        if (checkLocationPermissions()) {
            Log.d(TAG, "Location permissions granted");
            Intent serviceIntent = new Intent(this, LocationService.class);
            serviceIntent.setAction(LocationService.ACTION_START);

            try {
                // Start the service
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(TAG, "Starting foreground service");
                    startForegroundService(serviceIntent);
                } else {
                    Log.d(TAG, "Starting normal service");
                    startService(serviceIntent);
                }
                Log.d(TAG, "Service start command sent successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error starting service: ", e);
            }
        } else {
            Log.d(TAG, "Location permissions not granted, requesting...");
            requestLocationPermissions();
        }
    }

    private void createLocationNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Creating notification channel");
                NotificationChannel channel = new NotificationChannel(
                        LOCATION_CHANNEL_ID,
                        "Location",
                        NotificationManager.IMPORTANCE_LOW
                );
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating notification channel: ", e);
        }
    }
    private void setupbtnSOS() {
        btnSOS.setOnClickListener(v -> {
            Log.d(TAG, "Bouton de sos pressé");
            emergencyManage2.handleEmergency(emergencyContacts);
            showNotification();
        });
    }
}