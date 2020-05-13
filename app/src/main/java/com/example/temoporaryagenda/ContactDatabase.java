package com.example.temoporaryagenda;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Database(entities = {Contact.class}, version=1 )
public abstract class ContactDatabase extends RoomDatabase {

    private static ContactDatabase instance;

    public abstract ContactDAO contactDAO(); //ROOM takes care of all the code, no need for a body

    public static synchronized ContactDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ContactDatabase.class, "conctacts")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)//delete this when you remove the sample data
                    .build();
        }
        return instance;
    }

    //TEMPORARY THINGS HERE SO WE HAVE SOME DATA IN OUR DATABASE FROM THE START IN ORDER TO TEST SEE AND STYLE DURING DEVELOPMENT
    private static RoomDatabase.Callback roomCallBack= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateContactsDbAsynchTask(instance).execute();
        }
    };

    private static class PopulateContactsDbAsynchTask extends AsyncTask<Void, Void, Void>{
        private ContactDAO contactDAO;

        private PopulateContactsDbAsynchTask(ContactDatabase db){
            contactDAO = db.contactDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //TODO: Take care of date format
            Contact c1 = new Contact("Ciucur Daniel", "0757541317", "job", "colegul de la job", "ciucur@email.com", false, "12-02-2020");
            Contact c2 = new Contact("Mihai Ionut", "0757554369", "Madrid", "he is my friend", "mihai@email.com", false, "22-02-2020");
            Contact c3 = new Contact("Andrei Iovescu", "0757348943", "team", "team man", "iovescu@email.com", false, "30-02-2020");
            Contact c4 = new Contact("Andrei Opra", "0757543217", "job", "colegul", "opra@email.com", false, "15-10-2020");
            Contact c5 = new Contact("Adrian Tuns", "0757543617", "Barcelona", "from local guide", "tuns@email.com", false, "16-10-2020");
            Contact c6 = new Contact("Borza Alexandru", "0756563617", "Barcelona", "from local guide", "borza@email.com", false, "16-10-2020");
            Contact c7 = new Contact("Turdasan Tudor", "0756563227", "Sevilla", "parliament tour", "turdasan@email.com", false, "20-10-2020");
            Contact c8 = new Contact("Comsa Alexandru", "0756563149", "job", "from friendly agency", "comsa@email.com", false, "30-10-2020");
            Contact c9 = new Contact("Dumitru Angela", "0754463149", "mentor", "former supervisor", "dumitru@email.com", false, "28-02-2020");
            Contact c10 = new Contact("Popa Daniel", "0754263148", "mentor", "former supervisor", "popa@email.com", false, "28-02-2020");
            Contact c11 = new Contact("Bogdan Voicu", "0736655249", "Barcelona", "from local guide", "voicu@email.com", false, "25-02-2020");
            contactDAO.insert(c1);
            contactDAO.insert(c2);
            contactDAO.insert(c3);
            contactDAO.insert(c4);
            contactDAO.insert(c5);
            contactDAO.insert(c6);
            contactDAO.insert(c7);
            contactDAO.insert(c8);
            contactDAO.insert(c9);
            contactDAO.insert(c10);
            contactDAO.insert(c11);
            return null;
        }
    }
}
