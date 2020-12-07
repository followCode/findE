package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditDetailsActivity extends AppCompatActivity {
    private Button updateDetails;
    EditText name, email;
    String password;
    private FirebaseAuth mAuth;

    private boolean checkEmail() {
        // Get the email
        String e = email.getText().toString();

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(e);

        if( matcher.matches()==false ) {

            email.setError("Invalid email address");

            return false;
        }

        return true;
    }

    private boolean checkName() {
        String n =  name.getText().toString();
        if(n== null){
            name.setError("Please enter a Display Name");

            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_details);

        // Set the action bar details
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        // Setup buttons
        updateDetails = findViewById(R.id.UpdateDetails);
        name = (EditText)findViewById(R.id.editDetailsName);
        email = (EditText)findViewById(R.id.editDetailsEmail);

        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        final String origEmail = email.getText().toString();

        password = getIntent().getStringExtra("password");
        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmail() && checkName()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name.getText().toString())
                                        .build();

                                // Make update request
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                                    final CollectionReference complaintsRef = rootRef.collection("userContacts");
                                                    complaintsRef.whereEqualTo("email",origEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    Map<Object, String> map = new HashMap<>();
                                                                    map.put("email", email.getText().toString());
                                                                    complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                                                                }
                                                            }
                                                        }
                                                    });

                                                    Toast.makeText(getApplicationContext(), "Updated details!", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "Error: Please try again later!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                Toast.makeText(getApplicationContext(), "Error: Please try again later!", Toast.LENGTH_SHORT).show();
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
}
