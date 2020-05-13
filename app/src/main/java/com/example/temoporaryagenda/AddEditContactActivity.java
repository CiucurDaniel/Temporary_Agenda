package com.example.temoporaryagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditContactActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.temoporaryagenda.ID";
    public static final String EXTRA_NAME = "com.example.temoporaryagenda.EXTRA_NAME";
    public static final String EXTRA_PHONE_NUMBER = "com.example.temoporaryagenda.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_TAG = "com.example.temoporaryagenda.EXTRA_TAG";
    public static final String EXTRA_EMAIL = "com.example.temoporaryagenda.EMAIL";
    public static final String EXTRA_DESCRIPTION = "com.example.temoporaryagenda.DESCRIPTION";
    //TODO: Add calendar also

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextTag;
    private EditText editTextEmail;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editTextName = findViewById(R.id.edit_text_name);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextTag = findViewById(R.id.edit_text_tag);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextDescription = findViewById(R.id.edit_text_description);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        /*
        Display the title of the screen depending on the following:
        if the intent is sharing some data mean the user is editing a contact
        there for title is Edit contact
        otherwise the intent is empty which means the user tapped on the floating action button
        and wishes to add a new contact
         */
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit contact");
            //If we edit the contact then we need to get all information already in the EditTexts so they are not empty at first
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextPhoneNumber.setText(intent.getStringExtra(EXTRA_PHONE_NUMBER));
            editTextTag.setText(intent.getStringExtra(EXTRA_TAG));
            editTextEmail.setText(intent.getStringExtra(EXTRA_EMAIL));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            //TODO: Add calendar:

        } else {
            setTitle("Add contact");
        }

    }

    private void saveContact(){
        String name = editTextName.getText().toString();
        String number = editTextPhoneNumber.getText().toString();
        String tag = editTextTag.getText().toString();
        String email = editTextEmail.getText().toString();
        String description = editTextDescription.getText().toString();

        if( name.trim().isEmpty() || number.trim().isEmpty() || tag.trim().isEmpty() ){
            Toast.makeText(this, "Please enter a name and a phone number", Toast.LENGTH_SHORT);
            return;
        }

        Intent data = new Intent();

        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_PHONE_NUMBER, number);
        data.putExtra(EXTRA_TAG, tag);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_DESCRIPTION, description);
        //TODO: Add calendar

        //this is the case where we Edit a contact we also needs to send the ID
        //because an already existing contact does have an ID
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if( id != -1 ){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
    //simple tell the system to use the add_contact_menu as the menu of this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.save_contact:
                saveContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
