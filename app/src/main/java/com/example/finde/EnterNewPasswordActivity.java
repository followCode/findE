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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterNewPasswordActivity extends AppCompatActivity {
    private Button updatePassword;
    private EditText passw;
    private EditText repassw;
    private FirebaseAuth mAuth;
    private String password;
    private String email;

    private boolean checkPassword() {
        // Get the password strings
        String passwords[] = { passw.getText().toString(), repassw.getText().toString() };

        for(String p:passwords) {
            // Check if password contains special characters
            Pattern pattern = Pattern.compile("[^a-zA-Z]");
            Matcher matcher = pattern.matcher(p);
            if(!matcher.find()) {
                passw.setError("Must contain at least 1 special character");
                repassw.setError("Must contain at least 1 special character");
                return false;
            }

            // Check if password length is less than 8 characters
            if( p.length()<8 ) {
                passw.setError("Length of the password less than 8 characters");
                repassw.setError("Length of the password less than 8 characters");
                return false;
            }

        }

        return true;
    }

    private boolean samePassword() {
        // Get the password strings
        String password = passw.getText().toString();
        String confirmPassword = repassw.getText().toString();

        // Return if matching
        return password.equals(confirmPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_new_password);

        // Set the action bar details
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        // Setup the buttons
        updatePassword = findViewById(R.id.enterNewPasswordProceed);
        passw = findViewById(R.id.Password);
        repassw =  findViewById(R.id.RePassword);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        password = intent.getStringExtra("password");


        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if password meets the required criteria
                boolean check1 = checkPassword();
                // Check if the passwords match
                boolean check2 = samePassword();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //email = user.getEmail();
                //final AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                if (check1 && check2) {

                    user.updatePassword(passw.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password Updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: Please try again later!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent i = new Intent(EnterNewPasswordActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        /*
        *  End of button click
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
