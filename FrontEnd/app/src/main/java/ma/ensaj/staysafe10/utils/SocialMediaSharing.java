package ma.ensaj.staysafe10.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class SocialMediaSharing {
    private static final String FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String TWITTER_SHARE_URL = "https://twitter.com/intent/tweet?text=";
    private static final String LINKEDIN_SHARE_URL = "https://www.linkedin.com/sharing/share-offsite/?url=";

    public static void shareToSocialMedia(Context context, String message, String url, SocialNetwork network) {
        String shareUrl = buildShareUrl(message, url, network);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));

        // Vérifier si une application peut gérer cette intention
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // Fallback vers le navigateur par défaut
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Aucun navigateur disponible", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String buildShareUrl(String message, String url, SocialNetwork network) {
        String encodedMessage = Uri.encode(message);
        String encodedUrl = Uri.encode(url);

        switch (network) {
            case FACEBOOK:
                return FACEBOOK_SHARE_URL + encodedUrl;
            case TWITTER:
                return TWITTER_SHARE_URL + encodedMessage + "&url=" + encodedUrl;
            case LINKEDIN:
                return LINKEDIN_SHARE_URL + encodedUrl;
            default:
                throw new IllegalArgumentException("Réseau social non supporté");
        }
    }

    // Enum pour les réseaux sociaux supportés
    public enum SocialNetwork {
        FACEBOOK,
        TWITTER,
        LINKEDIN
    }
}