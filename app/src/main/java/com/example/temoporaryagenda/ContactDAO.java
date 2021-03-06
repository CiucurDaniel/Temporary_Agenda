package com.example.temoporaryagenda;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contacts")
    void deleteAll();

    @Query("SELECT * FROM contacts ORDER BY tag")
    LiveData<List<Contact>> getAllContacts();

}
