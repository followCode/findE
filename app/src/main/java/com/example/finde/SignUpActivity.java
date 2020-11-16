package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    private EditText passw;
    private EditText confirmPassw;
    private EditText signUpEmail;
    private EditText userName;

    private String specialCharacters = ".*[!@#$%^&_-~].*";

    /*
     * Method to check if email id is in correct format
     *
     * Parameters:
     *  - Nil
     *
     * Returns:
     *  - boolean: True - Meets format
     *             False - Doesn't meet format
     * */
    private boolean checkEmail() {
        // Get the email
        String email = signUpEmail.getText().toString();

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if( matcher.matches()==false ) {

            signUpEmail.setError("Invalid email address");

            return false;
        }

        return true;
    }


    /*
    * Method to check if password meets the following requirements:
    *  - Password length is at least 8
    *  - Contains special characters
    *
    * Parameters:
    *  - Nil
    *
    * Returns:
    *  - boolean: True - Meets requirements
    *             False - Doesn't meet requirements
    * */
    private boolean checkPassword() {
        // Get the password strings
        String passwords[] = { passw.getText().toString(), confirmPassw.getText().toString() };

        for(String p:passwords) {
            // Check if password contains special characters
            Pattern pattern = Pattern.compile("[^a-zA-Z]");
            Matcher matcher = pattern.matcher(p);
            if( matcher.find()==false ) {
                passw.setError("Must contain at least 1 special character");
                confirmPassw.setError("Must contain at least 1 special character");
                return false;
            }

            // Check if password length is less than 8 characters
            if( p.length()<8 ) {
                passw.setError("Length of the password less than 8 characters");
                confirmPassw.setError("Length of the password less than 8 characters");
                return false;
            }

        }

        return true;
    }

    /*
     * Method to check if password and confirm password match
     *
     * Parameters:
     *  - Nil
     *
     * Returns:
     *  - boolean: True - Match
     *             False - Don't match
     * */
    private boolean samePassword() {
        // Get the password strings
        String password = passw.getText().toString();
        String confirmPassword = confirmPassw.getText().toString();

        // Return if matching
        if( password.equals(confirmPassword) )
            return true;

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // Initialize the firebase authentication
        mAuth = FirebaseAuth.getInstance();

        Button signUpButton = findViewById(R.id.signUpButton);
        TextView signInButton = findViewById(R.id.gotoSignIn);
        passw = findViewById(R.id.signUpPassword);
        confirmPassw = findViewById(R.id.signUpRePassword);
        signUpEmail = findViewById(R.id.signUpEmail);
        userName = findViewById(R.id.signUpName);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if password meets the required criteria
                boolean check1 = checkPassword();
                // Check if the passwords match
                boolean check2 = samePassword();
                // Check if email is in correct format
                boolean check3 = checkEmail();
                if( check1&&check2&&check3 ) {
                    // Create the user account on Firebase

                    //Implementation of Firebase account creation

                    mAuth.createUserWithEmailAndPassword(signUpEmail.getText().toString(), passw.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //when sign Up is successful
                                        Toast.makeText(SignUpActivity.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();

                                        // Get created user
                                        FirebaseUser currentUser = mAuth.getCurrentUser();

                                        // Update their display name
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName.getText().toString())
                                                .build();

                                        // Make update request
                                        currentUser.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("DEBUG", "User profile updated.");
                                                        }
                                                    }
                                                });

                                        Intent i = new Intent(SignUpActivity.this, GetStartedActivity.class);
                                        i.putExtra("username", userName.getText().toString());
                                        startActivity(i);
                                        finish();
                                    } else {
                                        // If sign Up fails, and user email is already registered.
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(SignUpActivity.this, "Provided Email Is Already Registered!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Not able to register now, Please check your internet connection!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
