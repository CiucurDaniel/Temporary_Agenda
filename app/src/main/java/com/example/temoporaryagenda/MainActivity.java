package com.example.temoporaryagenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
/*
Main Activity class - the starting point of our application
Here we create the RecyclerView which will display the contacts
Every contact is wrapped in a CardView

 */
public class MainActivity extends AppCompatActivity {
    public static final int ADD_CONTACT_REQUEST = 1;
    public static final int EDIT_CONTACT_REQUEST = 2;
    private static final int REQUEST_CALL = 1;

    private String NUMBER = null;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ADD CONTACT PLUS BUTTON   --- FLOATING ACTION BUTTON
        FloatingActionButton addContactButton = findViewById(R.id.button_add_contact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
                startActivityForResult(intent, ADD_CONTACT_REQUEST);
                //now go on onActivityResultMethod() outside of onCreate
            }
        });

        //RECYCLER VIEW CODE HERE
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter contactAdapter = new ContactAdapter();
        recyclerView.setAdapter(contactAdapter);

        //here we asign the view model
        //but we don't call new because like that we make a new one for each activity
        //instead we ask the android system for it by calling a method that knows that
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                //update our RecyclerView
                //Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show(); //Old code
                contactAdapter.submitList(contacts);
            }
        });

        //FUNCTIONALITY TO SLIDE IN ORDER TO DELETE A CONTACT
        //SWIPE LEFT TO DELETE A CONTACT
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                contactViewModel.delete(contactAdapter.getContactAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Contact DELETED", Toast.LENGTH_SHORT);
            }
        }).attachToRecyclerView(recyclerView);

        //CALL A CONTACT ON SWIPE RIGHT
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                NUMBER = contactAdapter.getContactAt(viewHolder.getAdapterPosition()).getPhone_number();
                makePhoneCall(NUMBER);
            }
        }).attachToRecyclerView(recyclerView);

        contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
                intent.putExtra(AddEditContactActivity.EXTRA_NAME, contact.getName());
                intent.putExtra(AddEditContactActivity.EXTRA_PHONE_NUMBER, contact.getPhone_number());
                intent.putExtra(AddEditContactActivity.EXTRA_TAG, contact.getTag());
                intent.putExtra(AddEditContactActivity.EXTRA_EMAIL, contact.getE_mail());
                intent.putExtra(AddEditContactActivity.EXTRA_DESCRIPTION, contact.getDescription());
                //ROOM ALSO NEEDS THE ID TO FIGURE OUT WHICH CONTACT ARE WE TALKING ABOUT
                intent.putExtra(AddEditContactActivity.EXTRA_ID, contact.getId());
                startActivityForResult(intent, EDIT_CONTACT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //by this way, we find out which request we are handling here
        if (requestCode == ADD_CONTACT_REQUEST && resultCode == RESULT_OK) {
            //if this is ok, then retrieve the extras we left over
            String name = data.getStringExtra(AddEditContactActivity.EXTRA_NAME);
            String number = data.getStringExtra(AddEditContactActivity.EXTRA_PHONE_NUMBER);
            String tag = data.getStringExtra(AddEditContactActivity.EXTRA_TAG);
            String email = data.getStringExtra(AddEditContactActivity.EXTRA_EMAIL);
            String description = data.getStringExtra(AddEditContactActivity.EXTRA_DESCRIPTION);
            //TODO: Add calendar...

            Contact contact = new Contact(name, number, tag, description, email, false, "empty");
            contactViewModel.insert(contact);

            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_CONTACT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditContactActivity.EXTRA_ID, -1);

            if(id == - 1){
                Toast.makeText(this,"Contact cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditContactActivity.EXTRA_NAME);
            String number = data.getStringExtra(AddEditContactActivity.EXTRA_PHONE_NUMBER);
            String tag = data.getStringExtra(AddEditContactActivity.EXTRA_TAG);
            String email = data.getStringExtra(AddEditContactActivity.EXTRA_EMAIL);
            String description = data.getStringExtra(AddEditContactActivity.EXTRA_DESCRIPTION);
            //TODO: Add date...

            Contact contact = new Contact(name, number, tag, description, email, false, "empty");
            contact.setId(id); // needed such that ROOM knows which contact is updated
            contactViewModel.update(contact);

            Toast.makeText(this, "Contact succesfully updated", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Contact NOT SAVED", Toast.LENGTH_SHORT).show();

        }
    }

    //HERE TAKE CARE OF THE MENU IN THE 2 METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.menu.main_menu:
                contactViewModel.deleteAll();
                Toast.makeText(MainActivity.this, "All contacts have been deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //method used in order to make a phone call
    private void makePhoneCall(String phone_number){
        if (phone_number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phone_number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(MainActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
        }
    } //end-of method phone call

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(NUMBER);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}//end-main activity class
