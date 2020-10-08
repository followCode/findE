package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;


public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_intro);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide1)
                .title("Emergencies can be stressful")
                .description("We are here to help you")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide2)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide2)
                .title("Access to immediate emergency centres")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide3)
                .title("Map route from your path to the location")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide4)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide4)
                .title("Call emergency services closest to you")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide5)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide5)
                .title("Detail View")
                .description("We show all the services on a google map closest to you")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide6)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide6)
                .title("Automatic updates to contacts")
                .description("We send automatic SMS updates to your emergency contacts")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.slide7)
                .title("Let's get started!")
                .neededPermissions(new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS, Manifest.permission.INTERNET,
                        Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE})
                .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }
}