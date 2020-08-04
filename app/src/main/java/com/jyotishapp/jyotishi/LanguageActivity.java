package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSupportActionBar().setTitle("Select Language / भाषा चुने");


    }

    public void hindiButt(View view){
        setLocale("hi");
        startActivity(new Intent(LanguageActivity.this, MainActivity.class));
        finish();
    }

    public void englishButt(View view){
        setLocale("en");
        startActivity(new Intent(LanguageActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.select_lang), Toast.LENGTH_SHORT).show();
    }

    public void setLocale(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }
}
