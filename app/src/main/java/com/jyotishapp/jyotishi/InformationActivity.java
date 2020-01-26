package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InformationActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;
    EditText name, age;
    FirebaseAuth mAuth;
    MaterialButton proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().setTitle(R.string.user_info);

        //references
        name = (EditText) findViewById(R.id.userName);
        age = (EditText) findViewById(R.id.userAge);
        proceed = (MaterialButton) findViewById(R.id.proceed);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
    }

    public void proceedClicked(View view){
        String userName = name.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        if(!TextUtils.isEmpty(userAge) && !TextUtils.isEmpty(userName)){
            mRef.child("Name").setValue(userName);
            mRef.child("Age").setValue(userAge);
            mRef.child("Chat").child("TotalMessages").setValue("0");
            mRef.child("timestamp").setValue(0);
            startActivity(new Intent(InformationActivity.this, MainScreen.class));
        }
        else {
            Toast.makeText(InformationActivity.this, getString(R.string.both_fields), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
