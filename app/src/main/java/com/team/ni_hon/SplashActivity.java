package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        openApp();
    }

    private void openApp() {

        SharedPreferences sharedPreferences = getSharedPreferences("Config", MODE_PRIVATE);
        boolean nighmode=sharedPreferences.getBoolean("nightMode",false);
        String lang= sharedPreferences.getString("language", "");

        if (!lang.isEmpty()) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);

            Configuration configuration = new Configuration();
            configuration.setLocale(locale);

            Resources resources = getResources();
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }

        if (nighmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                // Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }

}