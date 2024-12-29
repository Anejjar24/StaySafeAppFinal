package ma.ensaj.staysafe10.utils;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {
    private static final String TAG = "LocationHelper";
    public interface LocationCallback {
        void onLocationReceived(String locationUrl);
        void onError(String errorMessage);
    }

    public static void getLocation(Context context, LocationCallback callback) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Vérification des permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onError("Permissions de localisation non accordées.");
            return;
        }

        // Récupération de la dernière localisation connue
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String locationUrl = "https://maps.google.com/?q=" + latitude + "," + longitude;
                            Log.d(TAG, "location est : "+locationUrl);
                            callback.onLocationReceived(locationUrl);
                        } else {
                            Log.d(TAG, "Impossible de récupérer la localisation.");
                            callback.onError("Impossible de récupérer la localisation.");
                        }
                    }
                })
                .addOnFailureListener(e -> callback.onError("Erreur : " + e.getMessage()));
    }
}