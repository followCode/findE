package com.example.finde;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        AlertDialog.Builder dialog = new AlertDialog.Builder(SignOutActivity.this);
        dialog.setTitle("SIGN OUT")
                .setCancelable(false)
                .setMessage("Do you really want to Sign Out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getApplicationContext(), "Signed Out successfully", Toast.LENGTH_SHORT).show();
                        Intent signIn = new Intent(SignOutActivity.this, SignInActivity.class);
                        signIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(signIn);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SignOutActivity.this, DashboardActivity.class));
                        finish();
                    }
                });

        dialog.create().show();
    }
}