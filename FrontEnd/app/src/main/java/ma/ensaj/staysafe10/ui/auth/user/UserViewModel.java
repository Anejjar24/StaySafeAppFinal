package ma.ensaj.staysafe10.ui.auth.user;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ma.ensaj.staysafe10.model.User;
import ma.ensaj.staysafe10.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<User> currentUser;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        currentUser = repository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void refreshUser() {
        repository.refreshUser();
    }
}