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

public class AddEmergencyContactsActivity extends Activity {
    private Button nextButton;
    private Button emerContactButton1, emerContactButton2;
    private String contact1, contact2;

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
                Intent i = new Intent(AddEmergencyContactsActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
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
                        /*Uri contactUri = data.getData();
                        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                        Cursor cursor = getApplicationContext().getContentResolver().query(contactUri, projection,
                                null, null, null);

                        if (cursor != null && cursor.moveToFirst()) {
                            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String number = cursor.getString(numberIndex);

                            contact1 = number;
                            Toast.makeText(getApplicationContext(), contact1, Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();*/

                        Uri contactData = data.getData();

                        Cursor phones = getContentResolver()
                                .query(contactData,
                                        null,
                                        null,
                                        null,
                                        null);

                        String name = "", phoneNumber = "";

                        if( phones.moveToFirst() ) {
                            phoneNumber = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }

                        /*while (phones.moveToNext()) {
                            name = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            phoneNumber = phones.getString(phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }*/
                        contact1 = phoneNumber;
                        Toast.makeText(getApplicationContext(), "Contact: "+contact1, Toast.LENGTH_SHORT).show();
                        phones.close();
                    }else{
                        Toast.makeText(getApplicationContext(), "Contact1 not selected", Toast.LENGTH_SHORT).show();
                    }
                        break;

                case 444:
                    if (resultCode == Activity.RESULT_OK){
                        /*Uri contactUri = data.getData();
                        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                        Cursor cursor = getApplicationContext().getContentResolver().query(contactUri, projection,
                                null, null, null);

                        if (cursor != null && cursor.moveToFirst()) {
                            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String number = cursor.getString(numberIndex);

                            contact2 = number;
                            Toast.makeText(getApplicationContext(), contact2, Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();*/
                        Uri contactData = data.getData();
                        Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                        cursor.moveToFirst();
                        String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact2 = number;
                        cursor.close();
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
