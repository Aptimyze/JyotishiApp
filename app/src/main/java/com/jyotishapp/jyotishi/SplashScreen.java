package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

   private String SHARED_PREFS;
   private Boolean shouldShowOnBoard;
   private String KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        SHARED_PREFS = "sharedPrefs";
        KEY = "key";
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        shouldShowOnBoard = sharedPreferences.getBoolean(KEY,true);

        if(!shouldShowOnBoard){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, MainScreen.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, 3000);

        }

        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, OnBoardActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, 3000);
        }

    }
}
