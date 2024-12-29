package ma.ensaj.staysafe10.ui.contacts.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.model.Contact;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.model.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contacts = new ArrayList<>();
    private final ContactClickListener listener;

    public ContactAdapter(ContactClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_contacts_list_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView phoneTextView;
        private final ImageView editButton;
        private final ImageView deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            phoneTextView = itemView.findViewById(R.id.phone);
            editButton = itemView.findViewById(R.id.copy);
            deleteButton = itemView.findViewById(R.id.delete);
        }

        void bind(Contact contact) {
            nameTextView.setText(contact.getName());
            phoneTextView.setText(contact.getNumber());
            editButton.setOnClickListener(v -> listener.onEditClick(contact));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(contact));
        }
    }

    public interface ContactClickListener {
        void onEditClick(Contact contact);
        void onDeleteClick(Contact contact);
    }
}


