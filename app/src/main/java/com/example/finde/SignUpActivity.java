package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    private EditText passw;
    private EditText confirmPassw;
    private EditText signUpEmail;

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

                    // TODO: Implement Firebase account creation


                    Toast.makeText(SignUpActivity.this, "Matching Passwords!!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();

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
