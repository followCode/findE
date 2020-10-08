package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * TODO: Check if user has logged in before.
        *
        * If user has logged in:
        *   --> Forward to the dashboard
        *
        * If user not logged in:
        *   --> Forward to Intro screens
        *
        * */

        // Currently just forwarding to the intro screens
        Intent i = new Intent(MainActivity.this, IntroActivity.class);
        startActivity(i);
        finish();
    }
}