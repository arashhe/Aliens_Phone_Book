package com.arash.contactroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arash.contactroom.adapter.RecyclerViewAdapter;
import com.arash.contactroom.model.Contact;
import com.arash.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    public static final String CONTACT_ID = "contact_id";
    private ContactViewModel contactViewModel;
    private TextView textView;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler_view);


        contactViewModel = new
                ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                recyclerViewAdapter = new RecyclerViewAdapter(contacts,MainActivity.this, MainActivity.this);

                recyclerView.setAdapter(recyclerViewAdapter);


            }
        });



        FloatingActionButton fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this , NewContact.class);
                startActivityForResult(intent , NEW_CONTACT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

        String name = data.getStringExtra(NewContact.NAME_REPLY);
        String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

        Contact contact = new Contact(name, occupation);

        ContactViewModel.insert(contact);
    }

    }
    public static void createSomeContact(){

        for (int i = 1; i < 10; i++) {

            String name = "b";
            for (int j = 1; j < i; j++) {
                name+="b";
            }

            String occupation = "testmb"+Integer.toString(i);

            Contact contact = new Contact(name, occupation);

            ContactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int position) {

        Contact contact = contactViewModel.allContacts.getValue().get(position);
        Log.d("GetName" , "onContactClick: "+ contact.getName());

        Intent intent = new Intent(MainActivity.this , NewContact.class);
        intent.putExtra(CONTACT_ID , contact.getId());
        startActivity(intent);

    }
}