package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEmergencyContactsActivity extends Activity {
    private Button nextButton;
    private Button emerContactButton1, emerContactButton2;
    private String contact1 ="", contact2 ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_emergency_contacts);

        emerContactButton1 = findViewById(R.id.addEmergencyContact1);
        emerContactButton2 =  findViewById(R.id.addEmergencyContact2);
        nextButton = findViewById(R.id.addEmergencyNextButton);

        emerContactButton1.setOnClickListener(new View.OnClickListener() {
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

        emerContactButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myActivity = new Intent( Intent.ACTION_PICK,
                            android.provider.ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(myActivity, 444);
                } catch (Exception e) {
                    Toast.makeText( getBaseContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!contact1.equals("")  &&  !contact2.equals("")) {
                    saveContacts();
                    Intent i = new Intent(AddEmergencyContactsActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(contact1.equals("")  &&  contact2.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select the contacts", Toast.LENGTH_SHORT).show();
                }
                else if(contact1.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select the contact 1", Toast.LENGTH_SHORT).show();
                }
                else if(contact2.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select the contact 2", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveContacts(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        Map<String, Object> userContact = new HashMap<>();
        userContact.put("contact1", contact1);
        userContact.put("contact2", contact2);
        userContact.put("email", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userContacts")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Successfully added the contacts", Toast.LENGTH_SHORT).show();
                    }
                });
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

}
