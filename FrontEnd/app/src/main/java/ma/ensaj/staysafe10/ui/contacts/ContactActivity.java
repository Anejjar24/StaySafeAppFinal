package ma.ensaj.staysafe10.ui.contacts;
import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.model.Contact;
import ma.ensaj.staysafe10.ui.contacts.adapter.ContactAdapter;
import ma.ensaj.staysafe10.ui.contacts.viewmodel.ContactViewModel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import ma.ensaj.staysafe10.R;
import ma.ensaj.staysafe10.ui.contacts.adapter.ContactAdapter;
import ma.ensaj.staysafe10.ui.contacts.viewmodel.ContactViewModel;

public class ContactActivity extends AppCompatActivity implements ContactAdapter.ContactClickListener {
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private ContactViewModel viewModel;
    private ContactAdapter adapter;

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button addButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact); // Chargez le layout

        // Associez manuellement les vues
        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        addButton = findViewById(R.id.addBtn);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialisez le ViewModel
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
        checkAndRequestPermissions();
        if (!checkPermissions()) {
            requestPermissions();
        }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS
                },
                PERMISSIONS_REQUEST_CODE
        );
    }
    private void setupRecyclerView() {
        adapter = new ContactAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupObservers() {
        viewModel.getContacts().observe(this, contacts -> {
            adapter.setContacts(contacts);
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.isLoading().observe(this, isLoading -> {
            // Gérer l'affichage du chargement si nécessaire
        });
    }

//
private void setupListeners() {
    addButton.setOnClickListener(v -> {
        if (checkPermissions()) {  // Vérifier les permissions
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                viewModel.addContact(name, phone);
                nameEditText.setText("");
                phoneEditText.setText("");
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            checkAndRequestPermissions();  // Demander les permissions si nécessaire
        }
    });
}

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onEditClick(Contact contact) {
        showEditDialog(contact);
    }

    @Override
    public void onDeleteClick(Contact contact) {
        showDeleteConfirmationDialog(contact);
    }

    private void showEditDialog(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_contact, null);

        TextInputEditText nameEdit = dialogView.findViewById(R.id.editName);
        TextInputEditText phoneEdit = dialogView.findViewById(R.id.editPhone);

        nameEdit.setText(contact.getName());
        phoneEdit.setText(contact.getNumber());

        builder.setView(dialogView)
                .setTitle("Modifier le contact")
                .setPositiveButton("Enregistrer", (dialog, which) -> {
                    String newName = nameEdit.getText().toString();
                    String newPhone = phoneEdit.getText().toString();

                    if (!newName.isEmpty() && !newPhone.isEmpty()) {
                        Contact updatedContact = new Contact(
                                contact.getId(),
                                newName,
                                newPhone
                        );
                        viewModel.updateContact(updatedContact);
                    } else {
                        Toast.makeText(this,
                                "Veuillez remplir tous les champs",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
    private void showDeleteConfirmationDialog(Contact contact) {
        new AlertDialog.
Builder(this)
                .setTitle("Supprimer le contact")
                .setMessage("Êtes-vous sûr de vouloir supprimer " + contact.getName() + " ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    viewModel.deleteContact(contact);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.WRITE_CONTACTS,
                            android.Manifest.permission.READ_CONTACTS
                    },
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions accordées, on peut continuer
                viewModel.loadContacts();
            } else {
                Toast.makeText(this,
                        "Les permissions sont nécessaires pour gérer les contacts",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}