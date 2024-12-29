package ma.ensaj.staysafe10.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ma.ensaj.staysafe10.data.remote.ApiService;
import ma.ensaj.staysafe10.model.User;
import ma.ensaj.staysafe10.network.RetrofitClientSecure;
import ma.ensaj.staysafe10.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserRepository {
    private static final String TAG = "UserRepository";
    private ApiService apiService;
    private SessionManager sessionManager;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public UserRepository(Context context) {
        apiService = RetrofitClientSecure.getRetrofitInstance(context).create(ApiService.class);
        sessionManager = new SessionManager(context);
    }

    public LiveData<User> getCurrentUser() {
        refreshUser();
        return currentUser;
    }

    public void refreshUser() {
        apiService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.d(TAG, "User received: " + user.getEmail());
                    currentUser.setValue(user);
                } else {
                    Log.e(TAG, "Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
            }
        });
    }
}