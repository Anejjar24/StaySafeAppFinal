package ma.ensaj.staysafe10.data.remote;

import java.util.List;

import ma.ensaj.staysafe10.model.Location;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LocationApiService {
    @POST("locations")
    Call<Location> createLocation(@Body Location location);

    @GET("locations")
    Call<List<Location>> getAllLocations();

    @GET("locations/{id}")
    Call<Location> getLocationById(@Path("id") Long id);

    @PUT("locations/{id}")
    Call<Location> updateLocation(@Path("id") Long id, @Body Location location);

    @DELETE("locations/{id}")
    Call<Void> deleteLocation(@Path("id") Long id);
}