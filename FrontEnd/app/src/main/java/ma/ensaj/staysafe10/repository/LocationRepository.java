package ma.ensaj.staysafe10.repository;


import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import ma.ensaj.staysafe10.data.remote.LocationApiService;
import ma.ensaj.staysafe10.model.Location;
import ma.ensaj.staysafe10.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LocationRepository {
    private LocationApiService locationApiService;

    public LocationRepository() {
        this.locationApiService = RetrofitClient.getRetrofitInstance().create(LocationApiService.class);
    }

    public MutableLiveData<Location> createLocation(Location location) {
        MutableLiveData<Location> locationData = new MutableLiveData<>();
        locationApiService.createLocation(location).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                locationData.setValue(null);
                // Handle error appropriately
            }
        });
        return locationData;
    }

    public MutableLiveData<List<Location>> getAllLocations() {
        MutableLiveData<List<Location>> locationsData = new MutableLiveData<>();
        locationApiService.getAllLocations().enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful()) {
                    locationsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                locationsData.setValue(null);
                // Handle error appropriately
            }
        });
        return locationsData;
    }

    public MutableLiveData<Location> getLocationById(Long id) {
        MutableLiveData<Location> locationData = new MutableLiveData<>();
        locationApiService.getLocationById(id).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                    locationData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                locationData.setValue(null);
                // Handle error appropriately
            }
        });
        return locationData;
    }

    public MutableLiveData<Location> updateLocation(Long id, Location location) {
        MutableLiveData<Location> locationData = new MutableLiveData<>();
        locationApiService.updateLocation(id, location).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                    locationData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                locationData.setValue(null);
                // Handle error appropriately
            }
        });
        return locationData;
    }

    public MutableLiveData<Boolean> deleteLocation(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        locationApiService.deleteLocation(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(false);
                // Handle error appropriately
            }
        });
        return result;
    }
    public Location createLocationSync(Location location) {
        try {
            Response<Location> response = locationApiService.createLocation(location).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
