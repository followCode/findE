package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ViewEmergencyContacts extends AppCompatActivity {

    Button contactButton1;
    Button contactButton2;
    Button updateContacts;
    String contact1= "", contact2= "";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_emergency_contacts);
        progressBar = findViewById(R.id.progressBar1);
        contactButton1 = findViewById(R.id.emergencyContactDetail1);
        contactButton2 = findViewById(R.id.emergencyContactDetail2);
        updateContacts = findViewById(R.id.viewEmergencyContactsUpdateDetails);


        updateButtonsText();

        // Set the action bar details
        getSupportActionBar().setTitle("Emergency Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        //setting button onclick listeners
        contactButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*Intent myActivity = new Intent( Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(myActivity, 222);*/

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, 222);
                } catch (Exception e) {
                    Toast.makeText( getBaseContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        contactButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Intent myActivity = new Intent( Intent.ACTION_PICK,
                    //        android.provider.ContactsContract.Contacts.CONTENT_URI);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, 444);
                } catch (Exception e) {
                    Toast.makeText( getBaseContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        updateContacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                progressBar.setVisibility(View.VISIBLE);
                if(!contact1.equals("") || !contact2.equals("")){
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                    final CollectionReference complaintsRef = rootRef.collection("userContacts");
                    complaintsRef.whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<Object, String> map = new HashMap<>();

                                    if(!contact1.equals(""))
                                        map.put("contact1", contact1);

                                    if(!contact2.equals(""))
                                        map.put("contact2", contact2);

                                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                                }

                                Toast.makeText(getApplicationContext(), "Updated the contacts", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            switch (requestCode){
                case 222:
                    if (resultCode == Activity.RESULT_OK){

                        Uri contactData = data.getData();

                        Cursor phones = getContentResolver()
                                .query(contactData,
                                        null,
                                        null,
                                        null,
                                        null);

                        String phoneNumber = "";

                        if( phones.moveToFirst() ) {
                            phoneNumber = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }

                        contact1 = phoneNumber;
                        contactButton1.setText(contact1);
                        Toast.makeText(getApplicationContext(), "Contact: "+contact1, Toast.LENGTH_SHORT).show();
                        phones.close();
                    }else{
                        Toast.makeText(getApplicationContext(), "Contact1 not selected", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 444:
                    if (resultCode == Activity.RESULT_OK){

                        Uri contactData = data.getData();

                        Cursor phones = getContentResolver()
                                .query(contactData,
                                        null,
                                        null,
                                        null,
                                        null);

                        String phoneNumber = "";

                        if( phones.moveToFirst() ) {
                            phoneNumber = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }

                        contact2 = phoneNumber;
                        contactButton2.setText(contact2);
                        Toast.makeText(getApplicationContext(), "Contact: "+contact2, Toast.LENGTH_SHORT).show();
                        phones.close();
                    }else{
                        Toast.makeText(getApplicationContext(), "Contact2 not selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void updateButtonsText(){
        progressBar.setVisibility(View.VISIBLE);
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference complaintsRef = rootRef.collection("userContacts");
        complaintsRef.whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        contactButton1.setText(document.get("contact1").toString());
                        contactButton2.setText(document.get("contact2").toString());
                    }
                }
            }
        });
    }
}