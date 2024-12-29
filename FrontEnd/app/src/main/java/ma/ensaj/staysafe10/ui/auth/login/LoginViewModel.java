package ma.ensaj.staysafe10.ui.auth.login;
import ma.ensaj.staysafe10.utils.SessionManager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ma.ensaj.staysafe10.data.remote.ApiService;
import ma.ensaj.staysafe10.model.LoginRequest;

// Retrofit pour la gestion des appels API

import ma.ensaj.staysafe10.model.LoginResponse;
import ma.ensaj.staysafe10.network.RetrofitClientSecure;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> loginStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private ApiService apiService;
    private SessionManager sessionManager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        sessionManager = new SessionManager(application);
        apiService = RetrofitClientSecure.getRetrofitInstance(application).create(ApiService.class);
    }


    public LiveData<String> getLoginStatusLiveData() {
        return loginStatusLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveToken(response.body().getToken());
                    loginStatusLiveData.setValue(response.body().getToken());
                } else {
                    errorLiveData.setValue("Login failed");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }


}



