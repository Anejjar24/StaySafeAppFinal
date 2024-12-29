package ma.ensaj.staysafe10.ui.contacts.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import ma.ensaj.staysafe10.model.Contact;
import ma.ensaj.staysafe10.repository.ContactRepository;

public class ContactViewModel extends AndroidViewModel {
    private final ContactRepository repository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ContactViewModel(Application application) {
        super(application);
        repository = new ContactRepository(application.getApplicationContext());
        loadContacts();
    }

    public LiveData<List<Contact>> getContacts() {
        return repository.getContactsLiveData();
    }

    public LiveData<String> getError() {
        return repository.getErrorLiveData();
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public void loadContacts() {
        loading.setValue(true);
        repository.fetchContacts();
        loading.setValue(false);
    }

    public void addContact(String name, String phone) {
        loading.setValue(true);
        Contact newContact = new Contact(null, name, phone);

        repository.addContact(newContact, new ContactRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
            }

            @Override
            public void onError(String error) {
                loading.setValue(false);
            }
        });
    }
    public void deleteContact(Contact contact) {
        loading.setValue(true);
        repository.deleteContact(contact, new ContactRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
            }

            @Override
            public void onError(String error) {
                loading.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }

    public void updateContact(Contact contact) {
        loading.setValue(true);
        repository.updateContact(contact, new ContactRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
            }

            @Override
            public void onError(String error) {
                loading.setValue(false);
                errorLiveData.setValue(error);
            }
        });
    }
}
