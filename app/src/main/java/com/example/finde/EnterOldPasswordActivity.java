package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class EnterOldPasswordActivity extends AppCompatActivity {
    private Button proceedButton;
    EditText oldPassword;
    boolean flag;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_old_password);

        // Set the action bar details
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        // Initialize buttons
        proceedButton = findViewById(R.id.enterOldPasswordProceed);
        oldPassword = (EditText)findViewById(R.id.oldPassword);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passw = oldPassword.getText().toString();
                if(passw.equals("")) {
                    oldPassword.setError("Please enter your old password");
                }
                else{
                    //Toast.makeText(EnterOldPasswordActivity.this, "Here", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    mAuth = FirebaseAuth.getInstance();
                    AuthCredential credential = EmailAuthProvider.getCredential(email,passw);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(EnterOldPasswordActivity.this, "Correct password entered", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EnterOldPasswordActivity.this, EnterNewPasswordActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(EnterOldPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EnterOldPasswordActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
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
