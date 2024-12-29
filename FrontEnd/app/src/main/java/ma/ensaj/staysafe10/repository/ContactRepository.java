package ma.ensaj.staysafe10.repository;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.staysafe10.data.remote.ContactService;
import ma.ensaj.staysafe10.model.Contact;
import ma.ensaj.staysafe10.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.ContactsContract;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;
import ma.ensaj.staysafe10.model.Contact;

import ma.ensaj.staysafe10.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactRepository {
    private final ContactService contactService;
    private final ContentResolver contentResolver;
    private final MutableLiveData<List<Contact>> contactsLiveData;
    private final MutableLiveData<String> errorLiveData;

    public ContactRepository(Context context) {
        this.contactService = RetrofitClient.getRetrofitInstance().create(ContactService.class);
        this.contentResolver = context.getContentResolver();
        this.contactsLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Contact>> getContactsLiveData() {
        return contactsLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchContacts() {
        contactService.getAllContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contactsLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Erreur lors de la récupération des contacts");
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau: " + t.getMessage());
            }
        });
    }

    public void addContact(Contact contact, final OperationCallback callback) {
        contactService.createContact(contact).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addToPhoneContacts(contact);
                    callback.onSuccess();
                    fetchContacts(); // Rafraîchir la liste
                } else {
                    callback.onError("Erreur lors de l'ajout du contact");
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    private void addToPhoneContacts(Contact contact) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        contact.getName())
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        contact.getNumber())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }
    public void deleteContact(Contact contact, final OperationCallback callback) {
        contactService.deleteContact(contact.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    deleteFromPhoneContacts(contact.getNumber());
                    callback.onSuccess();
                    fetchContacts(); // Rafraîchir la liste
                } else {
                    callback.onError("Erreur lors de la suppression");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    public void updateContact(Contact contact, final OperationCallback callback) {
        contactService.updateContact(contact.getId(), contact).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    updatePhoneContact(contact);
                    callback.onSuccess();
                    fetchContacts(); // Rafraîchir la liste
                } else {
                    callback.onError("Erreur lors de la mise à jour");
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    } private void deleteFromPhoneContacts(String phoneNumber) {
        try {
            Uri lookupUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(phoneNumber));

            Cursor cur = contentResolver.query(lookupUri, new String[] {
                            ContactsContract.PhoneLookup._ID},
                    null, null, null);

            if (cur != null && cur.moveToFirst()) {
                do {
                    String lookupKey = cur.getString(
                            cur.getColumnIndex(ContactsContract.PhoneLookup._ID));
                    Uri uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                    contentResolver.delete(uri, null, null);
                } while (cur.moveToNext());
                cur.close();
            }
            if (cur != null) {
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePhoneContact(Contact contact) {
        // D'abord supprimer l'ancien contact
        deleteFromPhoneContacts(contact.getNumber());
        // Puis ajouter le nouveau
        addToPhoneContacts(contact);
    }
}