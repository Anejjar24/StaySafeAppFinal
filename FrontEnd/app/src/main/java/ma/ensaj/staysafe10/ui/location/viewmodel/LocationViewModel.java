package ma.ensaj.staysafe10.ui.location.viewmodel;


import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ma.ensaj.staysafe10.model.Location;
import ma.ensaj.staysafe10.model.User;
import ma.ensaj.staysafe10.repository.LocationRepository;
import ma.ensaj.staysafe10.ui.auth.user.UserViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ma.ensaj.staysafe10.model.Location;
import ma.ensaj.staysafe10.model.User;
import ma.ensaj.staysafe10.repository.LocationRepository;
import ma.ensaj.staysafe10.ui.auth.user.UserViewModel;




public class LocationViewModel extends ViewModel {
    private LocationRepository repository;
    private MutableLiveData<List<Location>> locations;
    private MutableLiveData<Location> currentLocation;

    public LocationViewModel() {
        repository = new LocationRepository();
        locations = new MutableLiveData<>();
        currentLocation = new MutableLiveData<>();
    }

    public LiveData<Location> createLocation(Location location) {
        return repository.createLocation(location);
    }

    public LiveData<List<Location>> getAllLocations() {
        return repository.getAllLocations();
    }

    public LiveData<Location> getLocationById(Long id) {
        return repository.getLocationById(id);
    }

    public LiveData<Location> updateLocation(Long id, Location location) {
        return repository.updateLocation(id, location);
    }

    public LiveData<Boolean> deleteLocation(Long id) {
        return repository.deleteLocation(id);
    }


}

/*
public class LocationViewModel extends ViewModel {
    private static final String TAG = "LocationActivity";

    private LocationRepository repository;
    private UserViewModel userViewModel;
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LocationViewModel(UserViewModel userViewModel) {
        repository = new LocationRepository();
        this.userViewModel = userViewModel;
    }

    public void addCurrentUserLocation(double latitude, double longitude, boolean isEmergency) {
        isLoading.setValue(true);

        // Récupérer directement la valeur actuelle de l'utilisateur
        LiveData<User> userLiveData = userViewModel.getCurrentUser();
        User currentUser = userLiveData.getValue();

        if (currentUser != null) {
            Location location = new Location(latitude, longitude, isEmergency, currentUser.getId());
            repository.addLocation(location, new LocationRepository.LocationCallback() {
                @Override
                public void onSuccess(Location location) {
                    message.setValue("Position ajoutée avec succès");
                    isLoading.setValue(false);
                }

                @Override
                public void onError(String error) {
                    message.setValue(error);
                    isLoading.setValue(false);
                }
            });
        } else {
            message.setValue("Erreur: Utilisateur non connecté");
            isLoading.setValue(false);
        }
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}

 */