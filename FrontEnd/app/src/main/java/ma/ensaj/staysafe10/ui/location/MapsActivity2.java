package ma.ensaj.staysafe10.ui.location;
import ma.ensaj.staysafe10.model.Point;
import ma.ensaj.staysafe10.model.RiskZone;
import ma.ensaj.staysafe10.ui.riskzone.viewmodel.RiskZoneViewModel;
import ma.ensaj.staysafe10.utils.SessionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.databinding.ActivityMaps2Binding;

import ma.ensaj.staysafe10.ui.auth.user.UserViewModel;
import ma.ensaj.staysafe10.ui.location.viewmodel.LocationViewModel;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "TrackActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float DEFAULT_ZOOM = 15f;
    private static final long UPDATE_INTERVAL = 30000; // 30 seconds
    private static final long FASTEST_INTERVAL = 15000; // 15 seconds
    private GoogleMap mMap;
    private ActivityMaps2Binding binding;



    private LocationViewModel locationViewModel;
    private UserViewModel userViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastSavedLocation;


    private RiskZoneViewModel riskZoneViewModel;
    private Map<Long, Polygon> riskZonePolygons = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        riskZoneViewModel = new ViewModelProvider(this).get(RiskZoneViewModel.class);
        initializeComponents();
        setupMap();
    }
    private void setupMap() {
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map))
                .getMapAsync(this);
    }
    private void initializeComponents() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupLocationCallback();
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                Location currentLocation = locationResult.getLastLocation();
                if (shouldUpdateLocation(currentLocation)) {
                    userViewModel.getCurrentUser().observe(MapsActivity2.this, user -> {
                        if (user != null) {
                            saveLocation(currentLocation, user.getId());
                            lastSavedLocation = currentLocation;
                        }
                    });
                }
            }
        };
    }

    private boolean shouldUpdateLocation(Location currentLocation) {
        if (lastSavedLocation == null) return true;

        float distance = currentLocation.distanceTo(lastSavedLocation);
        return distance > 10; // Only update if moved more than 10 meters
    }

    private void startLocationUpdates() {
        if (!checkLocationServices()) return;
        if (!checkLocationPermission()) return;

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void saveLocation(Location location, Long userId) {
        ma.ensaj.staysafe10.model.Location locationData = createLocationData(location, userId);

        locationViewModel.createLocation(locationData).observe(this, created -> {
            if (created != null) {
                updateMapWithNewLocation(created);
            } else {
                Log.e(TAG, "Failed to save location");
            }
        });
    }

    private ma.ensaj.staysafe10.model.Location createLocationData(Location location, Long userId) {
        ma.ensaj.staysafe10.model.Location locationData = new ma.ensaj.staysafe10.model.Location();
        locationData.setLatitude(location.getLatitude());
        locationData.setLongitude(location.getLongitude());
        locationData.setTimestamp(LocalDateTime.now().toString());
        locationData.setUserId(userId);
        locationData.setIsEmergency(false);
        locationData.setStatus("ACTIVE");
        return locationData;
    }
    private void updateMapWithNewLocation(ma.ensaj.staysafe10.model.Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Ma position")
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
            loadRiskZones();
        }
    }

    private void loadRiskZones() {
        riskZoneViewModel.getRiskZones().observe(this, riskZones -> {
            clearExistingRiskZones();
            if (riskZones != null) {
                for (RiskZone zone : riskZones) {
                    displayRiskZone(zone);
                }
            }
        });
    }

    private void clearExistingRiskZones() {
        for (Polygon polygon : riskZonePolygons.values()) {
            polygon.remove();
        }
        riskZonePolygons.clear();
    }
//    private void displayRiskZone(RiskZone zone) {
//        if (zone.getPoints() == null || zone.getPoints().isEmpty()) return;
//
//        List<LatLng> polygonPoints = new ArrayList<>();
//        for (Point point : zone.getPoints()) {
//            polygonPoints.add(new LatLng(point.getLatitude(), point.getLongitude()));
//        }
//
//        int fillColor;
//        String riskLevel = zone.getRiskLevel();
//        if (riskLevel != null) {
//            switch (riskLevel.toUpperCase()) {
//                case "HIGH":
//                    fillColor = Color.argb(70, 255, 0, 0); // Red with transparency
//                    break;
//                case "MEDIUM":
//                    fillColor = Color.argb(70, 255, 165, 0); // Orange with transparency
//                    break;
//                case "LOW":
//                    fillColor = Color.argb(70, 255, 255, 0); // Yellow with transparency
//                    break;
//                default:
//                    fillColor = Color.argb(70, 128, 128, 128); // Gray with transparency
//            }
//        } else {
//            // Default color for null risk level
//            fillColor = Color.argb(70, 128, 128, 128); // Gray with transparency
//        }
//
//        PolygonOptions polygonOptions = new PolygonOptions()
//                .addAll(polygonPoints)
//                .fillColor(fillColor)
//                .strokeColor(Color.BLACK)
//                .strokeWidth(2);
//
//        Polygon polygon = mMap.addPolygon(polygonOptions);
//        riskZonePolygons.put(zone.getId(), polygon);
//
//        // Add a marker at the center of the risk zone
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng point : polygonPoints) {
//            builder.include(point);
//        }
//        LatLng center = builder.build().getCenter();
//
//        String riskLevelText = riskLevel != null ? riskLevel : "Unknown";
//        mMap.addMarker(new MarkerOptions()
//                .position(center)
//                .title(zone.getName())
//                .snippet("Risk Level: " + riskLevelText));
//    }




    private void displayRiskZone(RiskZone zone) {
        if (zone.getPoints() == null || zone.getPoints().isEmpty()) return;

        List<LatLng> polygonPoints = new ArrayList<>();
        for (Point point : zone.getPoints()) {
            polygonPoints.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }

        int fillColor;
        String riskLevel = zone.getRiskLevel();
        if (riskLevel != null) {
            switch (riskLevel.toUpperCase()) {
                case "HIGH":
                    fillColor = Color.argb(70, 139, 0, 0); // Dark red with transparency
                    break;
                case "MEDIUM":
                    fillColor = Color.argb(70, 178, 34, 34); // Firebrick red with transparency
                    break;
                case "LOW":
                    fillColor = Color.argb(70, 205, 92, 92); // Indian red with transparency
                    break;
                default:
                    fillColor = Color.argb(70, 220, 20, 60); // Crimson with transparency
            }
        } else {
            // Default color for null risk level
            fillColor = Color.argb(70, 220, 20, 60); // Crimson with transparency
        }

        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(polygonPoints)
                .fillColor(fillColor)
                .strokeColor(Color.rgb(139, 0, 0)) // Dark red border
                .strokeWidth(2);

        Polygon polygon = mMap.addPolygon(polygonOptions);
        riskZonePolygons.put(zone.getId(), polygon);

        // Add a marker at the center of the risk zone
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : polygonPoints) {
            builder.include(point);
        }
        LatLng center = builder.build().getCenter();

        String riskLevelText = riskLevel != null ? riskLevel : "Unknown";
        mMap.addMarker(new MarkerOptions()
                .position(center)
                .title(zone.getName())
                .snippet("Risk Level: " + riskLevelText));
    }
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private boolean checkLocationServices() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isEnabled) {
            Toast.makeText(this, "Veuillez activer la localisation dans les paramètres", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        return isEnabled;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

}

/*
public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "TrackActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float DEFAULT_ZOOM = 15f;
    private static final long UPDATE_INTERVAL = 30000;
    private static final long FASTEST_INTERVAL = 15000;
    private static final int MIN_DISTANCE_UPDATE = 10; // meters

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private LocationViewModel locationViewModel;
    private UserViewModel userViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastSavedLocation;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Object locationLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeComponents();
        setupMap();
    }

    private void initializeComponents() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupLocationCallback();
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                executorService.execute(() ->
                        processLocationUpdate(locationResult.getLastLocation()));
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if (!locationAvailability.isLocationAvailable()) {
                    mainHandler.post(() ->
                            Toast.makeText(MapsActivity2.this,
                                    "Signal GPS perdu", Toast.LENGTH_SHORT).show());
                }
            }
        };
    }

    private void processLocationUpdate(Location currentLocation) {
        synchronized (locationLock) {
            if (!shouldUpdateLocation(currentLocation)) return;

            mainHandler.post(() -> {
                userViewModel.getCurrentUser().observe(this, user -> {
                    if (user != null) {
                        executorService.execute(() -> {
                            ma.ensaj.staysafe10.model.Location locationData =
                                    createLocationData(currentLocation, user.getId());
                            saveLocationInBackground(locationData);
                        });
                    }
                });
            });

            lastSavedLocation = currentLocation;
        }
    }

    private boolean shouldUpdateLocation(Location currentLocation) {
        if (lastSavedLocation == null) return true;
        float distance = currentLocation.distanceTo(lastSavedLocation);
        return distance > MIN_DISTANCE_UPDATE;
    }

    private void startLocationUpdates() {
        if (!checkLocationServices()) return;
        if (!checkLocationPermission()) return;

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e(TAG, "Lost location permission", e);
        }
    }

    private void saveLocationInBackground(ma.ensaj.staysafe10.model.Location locationData) {
        locationViewModel.createLocation(locationData).observe(this, created -> {
            if (created != null) {
                mainHandler.post(() -> updateMapWithNewLocation(created));
            } else {
                mainHandler.post(() ->
                        Toast.makeText(MapsActivity2.this,
                                "Erreur d'enregistrement de la position",
                                Toast.LENGTH_SHORT).show());
            }
        });
    }

    private ma.ensaj.staysafe10.model.Location createLocationData(Location location, Long userId) {
        ma.ensaj.staysafe10.model.Location locationData = new ma.ensaj.staysafe10.model.Location();
        locationData.setLatitude(location.getLatitude());
        locationData.setLongitude(location.getLongitude());
        locationData.setTimestamp(LocalDateTime.now().toString());
        locationData.setUserId(userId);
        locationData.setIsEmergency(false);
        locationData.setStatus("ACTIVE");
        return locationData;
    }

    private void updateMapWithNewLocation(ma.ensaj.staysafe10.model.Location location) {
        if (mMap == null) return;

        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title("Ma position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mainHandler.post(() -> {
            mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        });
    }

    private boolean checkLocationServices() {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS désactivé")
                    .setMessage("Veuillez activer la localisation pour continuer")
                    .setPositiveButton("Paramètres", (dialog, which) ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Annuler", null)
                    .show();
        }
        return isEnabled;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permission de localisation requise",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}


 */
/*
public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "TrackActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float DEFAULT_ZOOM = 15f;
    private static final long UPDATE_INTERVAL = 30000; // 30 seconds
    private static final long FASTEST_INTERVAL = 15000; // 15 seconds
    private GoogleMap mMap;
    private ActivityMaps2Binding binding;



    private LocationViewModel locationViewModel;
    private UserViewModel userViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastSavedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeComponents();
        setupMap();
    }
    private void setupMap() {
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map))
                .getMapAsync(this);
    }
    private void initializeComponents() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupLocationCallback();
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                Location currentLocation = locationResult.getLastLocation();
                if (shouldUpdateLocation(currentLocation)) {
                    userViewModel.getCurrentUser().observe(MapsActivity2.this, user -> {
                        if (user != null) {
                            saveLocation(currentLocation, user.getId());
                            lastSavedLocation = currentLocation;
                        }
                    });
                }
            }
        };
    }

    private boolean shouldUpdateLocation(Location currentLocation) {
        if (lastSavedLocation == null) return true;

        float distance = currentLocation.distanceTo(lastSavedLocation);
        return distance > 10; // Only update if moved more than 10 meters
    }

    private void startLocationUpdates() {
        if (!checkLocationServices()) return;
        if (!checkLocationPermission()) return;

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void saveLocation(Location location, Long userId) {
        ma.ensaj.staysafe10.model.Location locationData = createLocationData(location, userId);

        locationViewModel.createLocation(locationData).observe(this, created -> {
            if (created != null) {
                updateMapWithNewLocation(created);
            } else {
                Log.e(TAG, "Failed to save location");
            }
        });
    }

    private ma.ensaj.staysafe10.model.Location createLocationData(Location location, Long userId) {
        ma.ensaj.staysafe10.model.Location locationData = new ma.ensaj.staysafe10.model.Location();
        locationData.setLatitude(location.getLatitude());
        locationData.setLongitude(location.getLongitude());
        locationData.setTimestamp(LocalDateTime.now().toString());
        locationData.setUserId(userId);
        locationData.setIsEmergency(false);
        locationData.setStatus("ACTIVE");
        return locationData;
    }
    private void updateMapWithNewLocation(ma.ensaj.staysafe10.model.Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Ma position")
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private boolean checkLocationServices() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isEnabled) {
            Toast.makeText(this, "Veuillez activer la localisation dans les paramètres", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        return isEnabled;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

}
*/