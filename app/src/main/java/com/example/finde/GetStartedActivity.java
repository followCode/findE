package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class GetStartedActivity extends Activity {
    private Button getStartedButton;
    private TextView Hiname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);

        getStartedButton = findViewById(R.id.getStartedButton);
        Hiname = findViewById(R.id.getStartedText);
        Hiname.setText("Hi "+getIntent().getStringExtra("username")+"!");
        /*Hiname.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String displayName = user.getDisplayName();

                for (UserInfo userInfo : user.getProviderData()) {
                    if (displayName == null && userInfo.getDisplayName() != null) {
                        displayName = userInfo.getDisplayName();
                    }
                }

                Hiname.setText("Hi "+ displayName);
            }
        },1000);*/


        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetStartedActivity.this, AddEmergencyContactsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}