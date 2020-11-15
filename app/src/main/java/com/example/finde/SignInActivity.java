package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends Activity {

    private FirebaseAuth mAuth;
    EditText signInEmail, passw;

    private boolean checkEmail() {
        // Get the email
        String email = signInEmail.getText().toString();

        if(email.equals("")) {
            signInEmail.setError("Please enter email address");
            return false;
        }

        return true;
    }

    private boolean checkPassword() {
        String password = passw.getText().toString();

        if(password.equals("")) {
            passw.setError("Please enter the password");
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Button loginButton = findViewById(R.id.loginButton);
        TextView signUpButton = findViewById(R.id.gotoSignUp);
        signInEmail = (EditText)findViewById(R.id.signInEmailAddress);
        passw = (EditText)findViewById(R.id.signInPassword);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean check1 = checkEmail();
                boolean check2 = checkPassword();

                if(check1 && check2){
                    mAuth.signInWithEmailAndPassword(signInEmail.getText().toString(), passw.getText().toString())
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //when sign Up is successful
                                        Toast.makeText(SignInActivity.this, "Signed In Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SignInActivity.this, DashboardActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        // If sign In fails
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SignInActivity.this, "Not able to sign in  now, Please check your internet connection!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }


            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
