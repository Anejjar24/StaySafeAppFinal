package ma.ensaj.staysafe10.network;


import android.content.Context;

import ma.ensaj.staysafe10.utils.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientSecure {
    private static final String BASE_URL = "http://10.0.2.2:8082/api/";
    private static Retrofit retrofit;
    private static SessionManager sessionManager;

    public static Retrofit getRetrofitInstance(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }

        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        String token = sessionManager.getToken();
                        if (token != null) {
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .method(original.method(), original.body());
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                        return chain.proceed(original);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
