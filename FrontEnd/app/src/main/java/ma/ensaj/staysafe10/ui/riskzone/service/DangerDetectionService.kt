package ma.ensaj.staysafe10.ui.riskzone.service
// DangerDetectionService.kt

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

// Pour les classes Java existantes
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ma.ensaj.staysafe10.R
import ma.ensaj.staysafe10.data.remote.RiskZoneService
import ma.ensaj.staysafe10.model.CheckLocationResponse
import ma.ensaj.staysafe10.model.Contact
import ma.ensaj.staysafe10.model.LocationRequest
import ma.ensaj.staysafe10.network.RetrofitClient
import ma.ensaj.staysafe10.ui.contacts.viewmodel.ContactViewModel
import ma.ensaj.staysafe10.ui.location.MapsActivity2
import ma.ensaj.staysafe10.ui.tracking.DefaultLocationClient
import ma.ensaj.staysafe10.ui.tracking.LocationClient
import ma.ensaj.staysafe10.utils.EmergencyManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer

import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.cancel
class DangerDetectionService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var riskZoneService: RiskZoneService
    private lateinit var emergencyManager: EmergencyManager
    private lateinit var contactViewModel: ContactViewModel
    private var emergencyContacts: List<Contact> = ArrayList()

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        emergencyManager = EmergencyManager(applicationContext)
        contactViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(ContactViewModel::class.java)

        // Observer pour les contacts comme dans MainActivity
        contactViewModel.getContacts().observeForever { contacts ->
            contacts?.let {
                if (contacts.isNotEmpty()) {
                    emergencyContacts = contacts
                    Log.d("DangerService", "Contacts chargés: ${contacts.size}")
                }
            }
        }

        riskZoneService = RetrofitClient.getRetrofitInstance().create(RiskZoneService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "danger_alerts",
                "Alertes de danger",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications pour les zones dangereuses"
            channel.setVibrationPattern(longArrayOf(0, 500, 200, 500))
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "danger_alerts")
            .setContentTitle("Surveillance des zones dangereuses")
            .setContentText("Service de détection actif")
            .setSmallIcon(R.drawable.ic_warning)
            .setOngoing(true)
            .build()

        startForeground(2, notification)

        locationClient.getLocationUpdates(5000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                checkDangerZone(location)
            }
            .launchIn(serviceScope)
    }
    private fun checkDangerZone(location: Location) {
        val locationRequest = LocationRequest(location.latitude, location.longitude)
        riskZoneService.checkLocation(locationRequest).enqueue(object : Callback<CheckLocationResponse> {
            override fun onResponse(
                call: Call<CheckLocationResponse>,
                response: Response<CheckLocationResponse>
            ) {
                if (response.isSuccessful && response.body()?.isInRiskZone == true) {
                    showDangerAlert(location)
                    if (emergencyContacts.isNotEmpty()) {
                        emergencyManager.handleEmergency(emergencyContacts)
                        Log.d("DangerService", "Alert d'urgence envoyée pour ${emergencyContacts.size} contacts")
                    } else {
                        Log.w("DangerService", "Aucun contact d'urgence disponible")
                    }
                }
            }

            override fun onFailure(call: Call<CheckLocationResponse>, t: Throwable) {
                Log.e("DangerService", "Erreur de vérification", t)
            }
        })
    }

    private fun showDangerAlert(location: Location) {
        val alertNotification = NotificationCompat.Builder(this, "danger_alerts")
            .setContentTitle("⚠️ ALERTE DE DANGER!")
            .setContentText("Vous êtes dans une zone dangereuse!\nPosition: ${location.latitude}, ${location.longitude}")
            .setSmallIcon(R.drawable.ic_warning)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setColor(Color.RED)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_warning,
                "Voir sur la carte",
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MapsActivity2::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        notificationManager.notify(3, alertNotification)
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "START_DANGER_DETECTION"
        const val ACTION_STOP = "STOP_DANGER_DETECTION"
    }
}
/*
class DangerDetectionService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var riskZoneService: RiskZoneService
    private lateinit var emergencyManager: EmergencyManager
    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Initialiser le service avec RetrofitClient
      riskZoneService = RetrofitClient.getRetrofitInstance().create(RiskZoneService::class.java)
      //  riskZoneService = RetrofitClient.getRetrofitInstance().create(RiskZoneService::class.java)
        // Créer le canal de notification pour les alertes de danger
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "danger_alerts",
                "Alertes de danger",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications pour les zones dangereuses"
            channel.setVibrationPattern(longArrayOf(0, 500, 200, 500))
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "danger_alerts")
            .setContentTitle("Surveillance des zones dangereuses")
            .setContentText("Service de détection actif")
            .setSmallIcon(R.drawable.ic_warning)
            .setOngoing(true)
            .build()

        startForeground(2, notification)

        locationClient.getLocationUpdates(5000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                checkDangerZone(location)
            }
            .launchIn(serviceScope)
    }

    private fun checkDangerZone(location: Location) {
        val locationRequest = LocationRequest(location.latitude, location.longitude)
        riskZoneService.checkLocation(locationRequest).enqueue(object : Callback<CheckLocationResponse> {
            override fun onResponse(
                call: Call<CheckLocationResponse>,
                response: Response<CheckLocationResponse>
            ) {
                if (response.isSuccessful && response.body()?.isInRiskZone == true) {
                    showDangerAlert(location)
                }
            }

            override fun onFailure(call: Call<CheckLocationResponse>, t: Throwable) {
                Log.e("DangerService", "Erreur de vérification", t)
            }
        })
    }

    private fun showDangerAlert(location: Location) {
        val alertNotification = NotificationCompat.Builder(this, "danger_alerts")
            .setContentTitle("⚠️ ALERTE DE DANGER!")
            .setContentText("Vous êtes dans une zone dangereuse!\nPosition: ${location.latitude}, ${location.longitude}")
            .setSmallIcon(R.drawable.ic_warning)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setColor(Color.RED)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_warning,
                "Voir sur la carte",
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MapsActivity2::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

        notificationManager.notify(3, alertNotification)
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "START_DANGER_DETECTION"
        const val ACTION_STOP = "STOP_DANGER_DETECTION"
    }
}
*/