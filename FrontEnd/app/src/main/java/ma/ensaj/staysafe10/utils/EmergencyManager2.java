package ma.ensaj.staysafe10.utils;

import android.content.Context;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ma.ensaj.staysafe10.model.Contact;

public class EmergencyManager2 {
    private final TwilioService twilioService;
    private final SocialShareManager socialShareManager;
    private final Context context;

    public EmergencyManager2(Context context) {
        this.twilioService = new TwilioService(context);
        this.socialShareManager = new SocialShareManager(context);
        this.context = context;
    }

    public void handleEmergency(List<Contact> emergencyContacts) {
        String baseMessage = "SOS ! Je suis en danger ! Ma localisation : ";

        // Récupérer la localisation avant d'envoyer les messages
        LocationHelper.getLocation(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String locationUrl) {
                // Obtenir la date et l'heure actuelles
                LocalDateTime now = LocalDateTime.now();

                // Formater séparément la date et l'heure
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                String currentDate = now.format(dateFormatter); // Exemple : 28/12/2024
                String currentTime = now.format(timeFormatter); // Exemple : 15:45

                String message = baseMessage + locationUrl;



                // Partage via d'autres plateformes
                socialShareManager.showPlatformChoice();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Erreur lors de la récupération de la localisation : " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}