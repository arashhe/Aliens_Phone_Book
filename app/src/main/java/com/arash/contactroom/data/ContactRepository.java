package com.arash.contactroom.data;

        import android.app.Application;

        import com.arash.contactroom.model.Contact;
        import com.arash.contactroom.util.ContactRoomDatabase;

        import java.util.List;

        import androidx.lifecycle.LiveData;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();

    }
    public LiveData<List<Contact>> getAllData() { return allContacts; }
    public void insert(Contact contact) {
        ContactRoomDatabase.databaseWriteExecuter.execute(() -> contactDao.insert(contact));
    }
    public void insert2(Contact contact) {
        ContactRoomDatabase.databaseWriteExecuter.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.insert(contact);
            }
        });
    }

    public LiveData<Contact> get(int id) {

        return contactDao.get(id);
    }
    public void update(Contact contact) {
        ContactRoomDatabase.databaseWriteExecuter.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.update(contact);
            }
        });
    }
    public void delete(Contact contact) {
        ContactRoomDatabase.databaseWriteExecuter.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.delete(contact);
            }
        });
    }

}
