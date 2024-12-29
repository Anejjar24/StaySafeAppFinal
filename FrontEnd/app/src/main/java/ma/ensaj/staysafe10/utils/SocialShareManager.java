package ma.ensaj.staysafe10.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/*
public class SocialShareManager {
    private final Context context;
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    private static final String FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String SOS_MESSAGE = "SOS je suis en danger!! sauver moi";
    private String currentLocationUrl = "";

    public SocialShareManager(Context context) {
        this.context = context;
    }

    public void showPlatformChoice() {
        LocationHelper.getLocation(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String locationUrl) {
                currentLocationUrl = locationUrl;
                showPlatformDialog();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Erreur de localisation: " + errorMessage, Toast.LENGTH_LONG).show();
                showPlatformDialog();
            }
        });
    }

    private void showPlatformDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir une plateforme")
                .setItems(new String[]{"Facebook", "Instagram"}, (dialog, which) -> {
                    if (which == 0) {
                        shareToFacebook();
                    } else {
                        shareToInstagram();
                    }
                })
                .show();
    }

    private void shareToFacebook() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Check if Facebook app is installed
            context.getPackageManager().getPackageInfo(FACEBOOK_PACKAGE, 0);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);
            intent.setPackage(FACEBOOK_PACKAGE);

            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook app not installed, use web sharing
            // Encode the location URL properly for the sharing URL
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);

            // Create a valid Facebook sharing URL
            String sharingUrl = FACEBOOK_SHARE_URL + encodedUrl + "&quote=" + encodedMessage;

            // Open the properly formatted sharing URL in browser
            openWebsite(sharingUrl);
        }

        showMessage();
    }

     private void shareToInstagram() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Vérifier si Instagram est installé
            context.getPackageManager().getPackageInfo(INSTAGRAM_PACKAGE, 0);

            // Créer un intent pour partager sur Instagram
            Intent intent = new Intent("com.instagram.share.ADD_TO_FEED");
            intent.setPackage(INSTAGRAM_PACKAGE);

            // Ajouter le message comme description
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);

            // Démarrer l'activité de partage Instagram
            context.startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            // Si Instagram n'est pas installé
            Toast.makeText(context,
                    "Instagram n'est pas installé. Veuillez installer l'application pour partager.",
                    Toast.LENGTH_LONG).show();

            // Ouvrir le Play Store pour installer Instagram
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + INSTAGRAM_PACKAGE)));
            } catch (android.content.ActivityNotFoundException anfe) {
                // Si le Play Store n'est pas disponible, ouvrir le site web
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + INSTAGRAM_PACKAGE)));
            }
        } catch (Exception e) {
            // En cas d'erreur lors du partage
            Toast.makeText(context,
                    "Erreur lors du partage sur Instagram: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        showMessage();
    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private void showMessage() {
        String fullMessage = "SOS!! aidez-moi je suis en danger!! " + SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nPosition: " + currentLocationUrl;
        }
        Toast.makeText(context, fullMessage, Toast.LENGTH_LONG).show();
    }
}

*/

/*
public class SocialShareManager {
    private final Context context;
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    private static final String FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String INSTAGRAM_SHARE_URL = "https://www.instagram.com/share?url=";
    private static final String SOS_MESSAGE = "SOS je suis en danger!! sauver moi";
    private String currentLocationUrl = "";

    public SocialShareManager(Context context) {
        this.context = context;
    }

    public void showPlatformChoice() {
        LocationHelper.getLocation(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String locationUrl) {
                currentLocationUrl = locationUrl;
                showPlatformDialog();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Erreur de localisation: " + errorMessage, Toast.LENGTH_LONG).show();
                showPlatformDialog();
            }
        });
    }

    private void showPlatformDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir une plateforme")
                .setItems(new String[]{"Facebook", "Instagram"}, (dialog, which) -> {
                    if (which == 0) {
                        shareToFacebook();
                    } else {
                        shareToInstagram();
                    }
                })
                .show();
    }

    private void shareToFacebook() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Check if Facebook app is installed
            context.getPackageManager().getPackageInfo(FACEBOOK_PACKAGE, 0);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);
            intent.setPackage(FACEBOOK_PACKAGE);

            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook app not installed, use web sharing
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);
            String sharingUrl = FACEBOOK_SHARE_URL + encodedUrl + "&quote=" + encodedMessage;
            openWebsite(sharingUrl);
        }

        showMessage();
    }

    private void shareToInstagram() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Check if Instagram app is installed
            context.getPackageManager().getPackageInfo(INSTAGRAM_PACKAGE, 0);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);
            intent.setPackage(INSTAGRAM_PACKAGE);

            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Instagram app not installed, use web sharing
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);

            // Utiliser un format similaire à Facebook pour le partage web Instagram
            String sharingUrl = INSTAGRAM_SHARE_URL + encodedUrl + "&text=" + encodedMessage;

            try {
                // Tenter d'ouvrir le lien de partage dans le navigateur
                openWebsite(sharingUrl);
            } catch (Exception ex) {
                // En cas d'échec, ouvrir Instagram dans le navigateur
                Toast.makeText(context,
                        "Impossible de partager directement. Ouverture d'Instagram...",
                        Toast.LENGTH_LONG).show();
                openWebsite("https://www.instagram.com");
            }
        }

        showMessage();
    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private void showMessage() {
        String fullMessage = "SOS!! aidez-moi je suis en danger!! " + SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nPosition: " + currentLocationUrl;
        }
        Toast.makeText(context, fullMessage, Toast.LENGTH_LONG).show();
    }
}*/
/*
public class SocialShareManager {
    private final Context context;
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    private static final String FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String INSTAGRAM_SHARE_URL = "https://www.instagram.com/create/story?url=";
    private static final String SOS_MESSAGE = "SOS je suis en danger!! sauver moi";
    private String currentLocationUrl = "";

    public SocialShareManager(Context context) {
        this.context = context;
    }

    public void showPlatformChoice() {
        LocationHelper.getLocation(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String locationUrl) {
                currentLocationUrl = locationUrl;
                showPlatformDialog();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Erreur de localisation: " + errorMessage, Toast.LENGTH_LONG).show();
                showPlatformDialog();
            }
        });
    }

    private void showPlatformDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir une plateforme")
                .setItems(new String[]{"Facebook", "Instagram"}, (dialog, which) -> {
                    if (which == 0) {
                        shareToFacebook();
                    } else {
                        shareToInstagram();
                    }
                })
                .show();
    }

    private void shareToFacebook() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Check if Facebook app is installed
            context.getPackageManager().getPackageInfo(FACEBOOK_PACKAGE, 0);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);
            intent.setPackage(FACEBOOK_PACKAGE);

            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook app not installed, use web sharing
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);

            // Create a valid Facebook sharing URL
            String sharingUrl = FACEBOOK_SHARE_URL + encodedUrl + "&quote=" + encodedMessage;

            // Open the properly formatted sharing URL in browser
            openWebsite(sharingUrl);
        }

        showMessage();
    }

    private void shareToInstagram() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Encodage de l'URL et du message pour le partage web
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);

            // Create a valid Instagram sharing URL (similar to Facebook)
            String sharingUrl = INSTAGRAM_SHARE_URL + encodedUrl + "&caption=" + encodedMessage;

            // Open the properly formatted sharing URL in browser
            openWebsite(sharingUrl);
        } catch (Exception e) {
            Toast.makeText(context,
                    "Erreur lors du partage sur Instagram: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();

            // Fallback to Instagram website
            openWebsite("https://www.instagram.com");
        }

        showMessage();
    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private void showMessage() {
        String fullMessage = "SOS!! aidez-moi je suis en danger!! " + SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nPosition: " + currentLocationUrl;
        }
        Toast.makeText(context, fullMessage, Toast.LENGTH_LONG).show();
    }
}*/
/*
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import android.app.AlertDialog;

public class SocialShareManager {
    private final Context context;
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    private static final String FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String SOS_MESSAGE = "SOS je suis en danger!! sauver moi";
    private String currentLocationUrl = "";

    public SocialShareManager(Context context) {
        this.context = context;
    }

    public void showPlatformChoice() {
        LocationHelper.getLocation(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String locationUrl) {
                currentLocationUrl = locationUrl;
                showPlatformDialog();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Erreur de localisation: " + errorMessage, Toast.LENGTH_LONG).show();
                showPlatformDialog();
            }
        });
    }

    private void showPlatformDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir une plateforme")
                .setItems(new String[]{"Facebook", "Instagram"}, (dialog, which) -> {
                    if (which == 0) {
                        shareToFacebook();
                    } else {
                        shareToInstagram();
                    }
                })
                .show();
    }

    private void shareToFacebook() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            // Check if Facebook app is installed
            context.getPackageManager().getPackageInfo(FACEBOOK_PACKAGE, 0);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);
            intent.setPackage(FACEBOOK_PACKAGE);

            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook app not installed, use web sharing
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);
            String sharingUrl = FACEBOOK_SHARE_URL + encodedUrl + "&quote=" + encodedMessage;
            openWebsite(sharingUrl);
        }

        showMessage();
    }

    private void shareToInstagram() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        // Copier le message dans le presse-papiers
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Message SOS", fullMessage);
        clipboard.setPrimaryClip(clip);

        try {
            // Vérifier si Instagram est installé
            context.getPackageManager().getPackageInfo(INSTAGRAM_PACKAGE, 0);

            // Ouvrir directement Instagram
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName(INSTAGRAM_PACKAGE, "com.instagram.android.activity.MainTabActivity"));
            context.startActivity(intent);

            // Afficher un message pour informer l'utilisateur
            Toast.makeText(context,
                    "Message copié dans le presse-papiers. Vous pouvez maintenant le coller dans Instagram",
                    Toast.LENGTH_LONG).show();

        } catch (PackageManager.NameNotFoundException e) {
            // Si Instagram n'est pas installé
            Toast.makeText(context,
                    "Instagram n'est pas installé. Veuillez installer l'application pour partager.",
                    Toast.LENGTH_LONG).show();

            // Ouvrir le Play Store pour installer Instagram
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + INSTAGRAM_PACKAGE)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + INSTAGRAM_PACKAGE)));
            }
        } catch (Exception e) {
            // En cas d'erreur lors du partage
            Toast.makeText(context,
                    "Erreur lors du partage sur Instagram: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        showMessage();
    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private void showMessage() {
        String fullMessage = "SOS!! aidez-moi je suis en danger!! " + SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nPosition: " + currentLocationUrl;
        }
        Toast.makeText(context, fullMessage, Toast.LENGTH_LONG).show();
    }
}*/
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ma.ensaj.staysafe10.R;

public class SocialShareManager {
    private final Context context;
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String INSTAGRAM_WEB_URL = "https://www.instagram.com";
    private static final String SOS_MESSAGE = "SOS je suis en danger!! sauver moi";
    private String currentLocationUrl = "";

    public SocialShareManager(Context context) {
        this.context = context;
    }

    public void showPlatformChoice() {
        LocationHelper.getLocation(context, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String locationUrl) {
                currentLocationUrl = locationUrl;
                showPlatformDialog();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, "Erreur de localisation: " + errorMessage, Toast.LENGTH_LONG).show();
                showPlatformDialog();
            }
        });
    }

    private void showPlatformDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog, null);

        // Initialisation des éléments de la vue
        LinearLayout facebookOption = customView.findViewById(R.id.facebookOption);
        LinearLayout instagramOption = customView.findViewById(R.id.instagramOption);
        TextView cancelButton = customView.findViewById(R.id.cancelButton);

        // Création du dialogue
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(customView)
                .setCancelable(false)
                .create();

        // Gestion des clics
        facebookOption.setOnClickListener(v -> {
            shareToFacebook();
            dialog.dismiss();
        });

        instagramOption.setOnClickListener(v -> {
            shareToInstagramWeb();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Affichage du dialogue
        dialog.show();
    }

    private void shareToFacebook() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        try {
            context.getPackageManager().getPackageInfo(FACEBOOK_PACKAGE, 0);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, fullMessage);
            intent.setPackage(FACEBOOK_PACKAGE);
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            String encodedUrl = Uri.encode(currentLocationUrl);
            String encodedMessage = Uri.encode(fullMessage);
            String sharingUrl = FACEBOOK_SHARE_URL + encodedUrl + "&quote=" + encodedMessage;
            openWebsite(sharingUrl);
        }

        showMessage();
    }

    private void shareToInstagramWeb() {
        String fullMessage = SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nMa position actuelle: " + currentLocationUrl;
        }

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Message SOS", fullMessage);
        clipboard.setPrimaryClip(clip);

        openWebsite(INSTAGRAM_WEB_URL);

        Toast.makeText(context,
                "Message copié dans le presse-papiers. Vous pouvez maintenant le coller sur Instagram Web",
                Toast.LENGTH_LONG).show();

        showMessage();
    }

    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private void showMessage() {
        String fullMessage = "SOS!! aidez-moi je suis en danger!! " + SOS_MESSAGE;
        if (!currentLocationUrl.isEmpty()) {
            fullMessage += "\nPosition: " + currentLocationUrl;
        }
        Toast.makeText(context, fullMessage, Toast.LENGTH_LONG).show();
    }
}

