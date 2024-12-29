package ma.ensaj.staysafe10.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TwilioService {
    private final OkHttpClient smsClient;
    private final OkHttpClient whatsappClient;
    private final Context context;
    private final Handler mainHandler;
    private final Semaphore rateLimiter = new Semaphore(1);
    private static final long RATE_LIMIT_TIMEOUT_MS = 1000;

    public TwilioService(Context context) {
        this.context = context;
        this.smsClient = initSMSClient();
        this.whatsappClient = initWhatsAppClient();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    private OkHttpClient initSMSClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .header("Authorization",
                                    Credentials.basic(TwilioConfig.SMS_ACCOUNT_SID, TwilioConfig.SMS_AUTH_TOKEN))
                            .method(original.method(), original.body());
                    return chain.proceed(builder.build());
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private OkHttpClient initWhatsAppClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .header("Authorization",
                                    Credentials.basic(TwilioConfig.WHATSAPP_ACCOUNT_SID, TwilioConfig.WHATSAPP_AUTH_TOKEN))
                            .method(original.method(), original.body());
                    return chain.proceed(builder.build());
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private String formatToInternationalNumber(String phoneNumber) {
        String cleanNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (!cleanNumber.startsWith("+")) {
            if (cleanNumber.startsWith("0")) {
                cleanNumber = cleanNumber.substring(1);
            }
            cleanNumber = "+212" + cleanNumber;
        }
        return cleanNumber;
    }

    private void showToast(String message, boolean isLongDuration) {
        mainHandler.post(() ->
                Toast.makeText(context, message,
                        isLongDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show()
        );
    }

    private boolean acquireRateLimitPermit() {
        try {
            return rateLimiter.tryAcquire(RATE_LIMIT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void releaseRateLimitPermit() {
        rateLimiter.release();
    }

    public void sendSMS(String phoneNumber, String message) {
        if (!acquireRateLimitPermit()) {
            showToast("Service temporairement occupé, réessayez dans quelques secondes", true);
            return;
        }

        String formattedNumber = formatToInternationalNumber(phoneNumber);
        Log.d(TAG, "Tentative d'envoi SMS à: " + formattedNumber);

        new Thread(() -> {
            try {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.twilio.com/2010-04-01/Accounts/"
                        + TwilioConfig.SMS_ACCOUNT_SID + "/Messages.json").newBuilder();

                FormBody formBody = new FormBody.Builder()
                        .add("To", formattedNumber)
                        .add("From", TwilioConfig.TWILIO_PHONE_NUMBER)
                        .add("Body", message)
                        .build();

                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(formBody)
                        .build();

                try (okhttp3.Response response = smsClient.newCall(request).execute()) {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    if (response.isSuccessful()) {
                        showToast("SMS envoyé avec succès", false);
                        Log.d(TAG, "SMS envoyé avec succès à " + formattedNumber);
                    } else {
                        String errorMessage = "Erreur " + response.code() + ": " + response.message();
                        showToast(errorMessage, true);
                        Log.e(TAG, "Erreur Twilio: " + errorMessage + "\nBody: " + responseBody);
                    }
                }
            } catch (Exception e) {
                String errorMessage = "Erreur lors de l'envoi: " + e.getMessage();
                showToast(errorMessage, true);
                Log.e(TAG, "Exception Twilio: " + e.getMessage());
            } finally {
                releaseRateLimitPermit();
            }
        }).start();
    }

    public void sendWhatsAppMessage(String phoneNumber,String message, String date, String time) {
        if (!acquireRateLimitPermit()) {
            showToast("Service temporairement occupé, réessayez dans quelques secondes", true);
            return;
        }

        String formattedNumber = formatToInternationalNumber(phoneNumber);
        Log.d(TAG, "Tentative d'envoi WhatsApp via Twilio à : " + formattedNumber);

        new Thread(() -> {
            try {
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.twilio.com/2010-04-01/Accounts/"
                        + TwilioConfig.WHATSAPP_ACCOUNT_SID + "/Messages.json").newBuilder();

                String contentVariables = String.format("{\"1\":\"%s\",\"2\":\"%s\"}", date, time);

                FormBody formBody = new FormBody.Builder()
                        .add("To", "whatsapp:" + formattedNumber)
                        .add("From", "whatsapp:" + TwilioConfig.TWILIO_PHONE_NUMBER_WHATSAPP)
                        .add("Body", message + date + " at " + time)
                        .add("TemplateSid", TwilioConfig.TEMPLATE_SID)
                        .build();

                Request request = new Request.Builder()
                        .url(urlBuilder.build())
                        .post(formBody)
                        .build();

                try (okhttp3.Response response = whatsappClient.newCall(request).execute()) {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    if (response.isSuccessful()) {
                        showToast("Message WhatsApp envoyé avec succès", false);
                        Log.d(TAG, "WhatsApp envoyé avec succès à " + formattedNumber);
                    } else {
                        String errorMessage = "Erreur " + response.code() + ": " + response.message();
                        showToast(errorMessage, true);
                        Log.e(TAG, "Erreur Twilio WhatsApp: " + errorMessage + "\nBody: " + responseBody);
                    }
                }
            } catch (Exception e) {
                String errorMessage = "Erreur lors de l'envoi WhatsApp: " + e.getMessage();
                showToast(errorMessage, true);
                Log.e(TAG, "Exception Twilio WhatsApp: " + e.getMessage());
            } finally {
                releaseRateLimitPermit();
            }
        }).start();
    }
}