package com.example.temoporaryagenda;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactRepository {
    private ContactDAO contactDAO;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application){
        ContactDatabase database = ContactDatabase.getInstance(application);
        contactDAO = database.contactDAO();
        allContacts = contactDAO.getAllContacts();
    }

    //We need to handle this, because ROOM doesn't allow database operations on the main thread
    //so it's on us to take care of it

    //Basically this is the API for outside
    //and abstraction layer
    //So the ViewModel only needs to call insert, update, delete, deleteAll.
    public void insert(Contact contact){
        new InsertContactsAsync(contactDAO).execute(contact);
    }

    public void update(Contact contact){
        new UpdateContactsAsync(contactDAO).execute(contact);
    }

    public void delete(Contact contact){
        new DeleteContactsAsync(contactDAO).execute(contact);
    }

    public void deleteAll(){
        new DeleteAllContactsAsync(contactDAO).execute();
    }

    //For this one ROOM will handle all the necessary things
    public LiveData<List<Contact>> getAllContacts(){
        return allContacts;
    }


    //An asynchronous task is defined by a computation that runs on a background thread and whose result is published on the UI thread.
    // An asynchronous task is defined by 3 generic types, called Params, Progress and Result.
    // And by 4 steps, called onPreExecute, doInBackground, onProgressUpdate and onPostExecute.

    //Insert asynch
    private static class InsertContactsAsync extends AsyncTask<Contact, Void, Void>{
        //static class cannot access no-static members directly so we will use a constructor
        private ContactDAO contactDAO = null;

        private InsertContactsAsync(ContactDAO contactDAO){
            this.contactDAO = contactDAO;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.insert(contacts[0]);
            return null;
        }
    }

    //UPDATE async
    private static class UpdateContactsAsync extends AsyncTask<Contact, Void, Void>{
        //static class cannot access no-static members directly so we will use a constructor
        private ContactDAO contactDAO = null;

        private UpdateContactsAsync(ContactDAO contactDAO){
            this.contactDAO = contactDAO;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.update(contacts[0]);
            return null;
        }
    }

    //DELETE asynch
    private static class DeleteContactsAsync extends AsyncTask<Contact, Void, Void>{
        //static class cannot access no-static members directly so we will use a constructor
        private ContactDAO contactDAO = null;

        private DeleteContactsAsync(ContactDAO contactDAO){
            this.contactDAO = contactDAO;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.delete(contacts[0]);
            return null;
        }
    }

    //DeleteAll asynch
    private static class DeleteAllContactsAsync extends AsyncTask<Void, Void, Void>{
        //static class cannot access no-static members directly so we will use a constructor
        private ContactDAO contactDAO = null;

        private DeleteAllContactsAsync(ContactDAO contactDAO){
            this.contactDAO = contactDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            contactDAO.deleteAll();
            return null;
        }
    }
}
