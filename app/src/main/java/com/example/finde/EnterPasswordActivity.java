package com.example.finde;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class EnterPasswordActivity extends AppCompatActivity {
    Button proceedButton;
    EditText password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_curr_password);


        // Set the action bar details
        getSupportActionBar().setTitle("Enter Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        // Initialize buttons
        proceedButton = findViewById(R.id.enterPasswordProceed);
        password = (EditText)findViewById(R.id.currPassword);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String passw = password.getText().toString();
                if(passw.equals("")) {
                    password.setError("Please enter your password");
                }
                else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    mAuth = FirebaseAuth.getInstance();
                    AuthCredential credential = EmailAuthProvider.getCredential(email,passw);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(EnterPasswordActivity.this, EditDetailsActivity.class);
                                intent.putExtra("password", passw);
                                startActivity(intent);
                                finish();
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(EnterPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EnterPasswordActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            };
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
