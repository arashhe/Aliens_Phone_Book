package com.arash.contactroom.util;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arash.contactroom.data.ContactDao;
import com.arash.contactroom.model.Contact;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//imported from source
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDao contactDao() ;

    private static volatile ContactRoomDatabase INSTANCE;

    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecuter =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecuter.execute(() -> {
                        ContactDao contactDao = INSTANCE.contactDao();
                        contactDao.deleteAll();

                        Contact contact = new Contact("Paulo", "Teacher");
                        contactDao.insert(contact);

                        contact = new Contact("Bond", "Spy");
                        contactDao.insert(contact);

                        contact = new Contact("Bruce", "Fighter");
                        contactDao.insert(contact);


                    });
                }
            };

    public static final RoomDatabase.Callback sRoomDatabaseCallback2 =
            new RoomDatabase.Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecuter.execute(new Runnable() {
                        @Override
                        public void run() {
                            ContactDao contactDao = INSTANCE.contactDao();
                            contactDao.deleteAll();

                            Contact contact = new Contact("Paolo","Teacher");
                            contactDao.insert(contact);
                            contact = new Contact("Bond","Spy");
                            contactDao.insert(contact);
                            contact = new Contact("Bruce","Fighter");
                            contactDao.insert(contact);

                        }
                    });
                }
            };




    public static ContactRoomDatabase getDatabase (final Context context){
        if (INSTANCE == null){
            synchronized (ContactRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDatabase.class, "contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
