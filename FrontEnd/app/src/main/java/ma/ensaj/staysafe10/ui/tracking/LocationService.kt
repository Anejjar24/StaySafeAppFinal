package ma.ensaj.staysafe10.ui.tracking

import android.app.NotificationManager
import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ma.ensaj.staysafe10.R

class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var notificationManager: NotificationManager

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LocationService", "onStartCommand called with action: ${intent?.action}")
        when(intent?.action) {
            ACTION_START -> {
                Log.d("LocationService", "Starting location updates")
                startLocationTracking()
            }
            ACTION_STOP -> {
                Log.d("LocationService", "Stopping location updates")
                stopLocationTracking()
            }
        }
        return START_STICKY
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 2000 // 2 seconds
            fastestInterval = 1000 // 1 second
            smallestDisplacement = 0f // Update on any movement
            setNumUpdates(Int.MAX_VALUE)
            // Important pour l'émulateur : forcer les mises à jour
            setMaxWaitTime(1000)
        }
    }

    private fun startLocationTracking() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Attente de la localisation...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        startForeground(1, notification.build())

        // Pour l'émulateur, on utilise un système de mise à jour personnalisé
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d("LocationService", "Nouvelle location: ${location.latitude}, ${location.longitude}")

                    val updatedNotification = notification.setContentText(
                        "Location: (${location.latitude}, ${location.longitude})"
                    )
                    notificationManager.notify(1, updatedNotification.build())
                }
            }
        }

        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationRequest = createLocationRequest()
                LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Erreur lors du démarrage du suivi", e)
        }
    }

    private fun stopLocationTracking() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}

/*
class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var notificationManager: NotificationManager
    private var lastLocation: Location? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LocationService", "onStartCommand called with action: ${intent?.action}")
        when(intent?.action) {
            ACTION_START -> {
                Log.d("LocationService", "Starting location updates")
                startLocationTracking()
            }
            ACTION_STOP -> {
                Log.d("LocationService", "Stopping location updates")
                stopLocationTracking()
            }
        }
        return START_STICKY
    }

    private fun createNotification(location: Location? = null): NotificationCompat.Builder {
        // Create an intent to open the app when notification is clicked
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText(
                when (location) {
                    null -> "Waiting for location..."
                    else -> "Location: (${location.latitude}, ${location.longitude})"
                }
            )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    private fun startLocationTracking() {
        try {
            // Start with initial notification
            val initialNotification = createNotification()
            startForeground(NOTIFICATION_ID, initialNotification.build())

            // Begin location updates
            serviceScope.launch {
                locationClient.getLocationUpdates(5000L) // Update every 5 seconds
                    .catch { e ->
                        Log.e("LocationService", "Error getting location updates", e)
                        // Update notification with error
                        val errorNotification = createNotification()
                            .setContentText("Error: ${e.message}")
                        notificationManager.notify(NOTIFICATION_ID, errorNotification.build())
                    }
                    .collect { location ->
                        Log.d("LocationService", "New location: ${location.latitude}, ${location.longitude}")

                        // Check if location has changed significantly (more than 10 meters)
                        if (shouldUpdateLocation(location)) {
                            lastLocation = location

                            // Update notification with new location
                            val updatedNotification = createNotification(location)
                            notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Error starting location tracking", e)
            stopSelf()
        }
    }

    private fun shouldUpdateLocation(newLocation: Location): Boolean {
        return lastLocation?.let { last ->
            val distance = newLocation.distanceTo(last)
            distance > 10 // Update if moved more than 10 meters
        } ?: true // Always update if it's the first location
    }

    private fun stopLocationTracking() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val NOTIFICATION_ID = 1
    }
}

 */
/*
class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when(intent?.action) {
//            ACTION_START -> start()
//            ACTION_STOP -> stop()
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }

    override fun onStartCommand(intent:Intent? , flags: Int, startId: Int) : Int {
        Log.d("LocationService", "onStartCommand called with action: " + intent?.action);
        when(intent?.action) {
            ACTION_START -> {
                Log.d("LocationService", "Starting location updates")
                start()
            }
            ACTION_STOP -> {
                Log.d("LocationService", "Stopping location updates")
                stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                Log.d("LocationService", "Location update: ${location.latitude}, ${location.longitude}")
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
*/