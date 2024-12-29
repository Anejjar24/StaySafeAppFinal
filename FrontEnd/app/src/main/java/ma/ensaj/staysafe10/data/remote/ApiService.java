package ma.ensaj.staysafe10.data.remote;


import ma.ensaj.staysafe10.model.LoginRequest;
import ma.ensaj.staysafe10.model.LoginResponse;
import ma.ensaj.staysafe10.model.PasswordResetRequest;
import ma.ensaj.staysafe10.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface ApiService {
    @POST("auth/signup")
    Call<User> signUp(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("auth/forgot-password")
    Call<ResponseBody> resetPassword(@Body PasswordResetRequest request);
    @GET("auth/user-info")
    Call<User> getCurrentUser();
}
