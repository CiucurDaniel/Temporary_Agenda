package com.example.temoporaryagenda;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
/*
This is the ViewModel
the intermediary between UI and DATABASE
 */
public class ContactViewModel extends AndroidViewModel {
    private ContactRepository repository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(application);
        allContacts =repository.getAllContacts();
    }

    public void insert(Contact contact){
        repository.insert(contact);
    }

    public void delete(Contact contact){
        repository.delete(contact);
    }

    public void update(Contact contact){
        repository.update(contact);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Contact>> getAllContacts(){
        return allContacts;
    }
}
