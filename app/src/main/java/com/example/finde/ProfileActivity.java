package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private Button emergencyContactButton;
    private Button changePasswordButton;
    private Button editProfile;
    private TextView profileUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        String email =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        profileUserName = findViewById(R.id.profileUserName);
        profileUserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        TextView userEmail = findViewById(R.id.profileUserEmail);

        userEmail.setText(email);
        // Set the action bar details
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        // Initialize the buttons
        emergencyContactButton = findViewById(R.id.profileEmergencyContacts);
        changePasswordButton = findViewById(R.id.profileChangePassword);
        editProfile = findViewById(R.id.profileEditProfile);

        /*
        *  Start of button events
        * */

        emergencyContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, ViewEmergencyContacts.class);
                startActivity(i);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, EnterOldPasswordActivity.class);
                startActivity(i);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, EnterPasswordActivity.class);
                startActivity(i);
            }
        });

        /*
        *  End of button events
        * */
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