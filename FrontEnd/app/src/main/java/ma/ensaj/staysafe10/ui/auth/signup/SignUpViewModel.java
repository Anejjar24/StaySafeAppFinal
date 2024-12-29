package ma.ensaj.staysafe10.ui.auth.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensaj.staysafe10.data.remote.ApiService;
import ma.ensaj.staysafe10.model.User;
import ma.ensaj.staysafe10.network.RetrofitClient;

// Retrofit pour la gestion des appels API
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {


    private MutableLiveData<User> signUpLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<User> getSignUpLiveData() {
        return signUpLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void signUp(String email, String password, String phone) {
        // Créer l'objet User à envoyer à l'API
        User user = new User(email, password, phone);

        // Appel de l'API via Retrofit
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.signUp(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    signUpLiveData.setValue(response.body());  // Sucessful response
                } else {
                    errorLiveData.setValue("Signup failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorLiveData.setValue("Network error");
            }
        });
    }

/*
    private MutableLiveData<User> signUpLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<User> getSignUpLiveData() {
        return signUpLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void signUp(String email, String password, String phone) {
        // Créer l'objet User à envoyer à l'API
        User user = new User(email, password, phone);

        // Appel de l'API via Retrofit
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.signUp(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    signUpLiveData.setValue(response.body());  // Sucessful response
                } else {
                    errorLiveData.setValue("Signup failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorLiveData.setValue("Network error");
            }
        });
    }

 */
}
