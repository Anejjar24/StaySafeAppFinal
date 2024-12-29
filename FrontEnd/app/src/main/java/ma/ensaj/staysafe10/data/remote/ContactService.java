package ma.ensaj.staysafe10.data.remote;
import java.util.List;

import ma.ensaj.staysafe10.model.Contact;
import retrofit2.Call;
import retrofit2.http.*;
public interface ContactService {
    @GET("contacts")
    Call<List<Contact>> getAllContacts();

    @POST("contacts")
    Call<Contact> createContact(@Body Contact contact);

    @PUT("contacts/{id}")
    Call<Contact> updateContact(@Path("id") Long id, @Body Contact contact);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") Long id);
}
