package ma.ensaj.staysafe10.ui.auth.forgotpassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.ensaj.staysafe10.data.remote.ApiService;
import ma.ensaj.staysafe10.model.LoginRequest;
import ma.ensaj.staysafe10.model.LoginResponse;
import ma.ensaj.staysafe10.model.PasswordResetRequest;
import ma.ensaj.staysafe10.network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends ViewModel {
    private MutableLiveData<String> message = new MutableLiveData<>();
    private ApiService apiService;

    public ForgotPasswordViewModel() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void resetPassword(String email) {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail(email);

        apiService.resetPassword(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    message.setValue("Un nouveau mot de passe a été envoyé à votre email");
                } else {
                    message.setValue("Erreur: Email non trouvé");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                message.setValue("Erreur de connexion");
            }
        });
    }
}



