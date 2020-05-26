package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TnCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tn_c);
        getSupportActionBar().setTitle(R.string.tnc);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
