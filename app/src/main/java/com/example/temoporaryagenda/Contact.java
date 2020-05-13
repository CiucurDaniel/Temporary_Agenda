package com.example.temoporaryagenda;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String phone_number;

    private String tag;

    private String description;

    private String e_mail;

    private boolean is_archive;

    private String deletion_date; //TODO: Replace with a proper date format

    public Contact(String name, String phone_number, String tag, String description, String e_mail, boolean is_archive, String deletion_date) {
        this.name = name;
        this.phone_number = phone_number;
        this.tag = tag;
        this.description = description;
        this.e_mail = e_mail;
        this.is_archive = is_archive;
        this.deletion_date = deletion_date;
    }

    //GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public boolean isIs_archive() {
        return is_archive;
    }

    public void setIs_archive(boolean is_archive) {
        this.is_archive = is_archive;
    }

    public String getDeletion_date() {
        return deletion_date;
    }

    public void setDeletion_date(String deletion_date) {
        this.deletion_date = deletion_date;
    }
}
