package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference mRef;
    EditText name, age;
    FirebaseAuth mAuth;
    Button proceed;
    List<String> langList;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinner;
    String language="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().setTitle(R.string.user_info);

        //references
        name = (EditText) findViewById(R.id.userName);
        age = (EditText) findViewById(R.id.userAge);
        proceed = (Button) findViewById(R.id.proceed);
        spinner = (Spinner) findViewById(R.id.spinnerLang);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            name.setText(extras.getString("name"));
            age.setText(extras.getString("age"));
        }

        langList = new ArrayList<>();
        langList.add(getString(R.string.select_lang));
        langList.add("English");
        langList.add("தமிழ்");
        langList.add("हिंदी");
        langList.add("ਪੰਜਾਬੀ");

        spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, langList
        ){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                language = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
    }

    public void proceedClicked(View view){
        String userName = name.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        if(!TextUtils.isEmpty(userAge) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(language) && !language.equals(getString(R.string.select_lang))){
            mRef.child("Name").setValue(userName);
            mRef.child("Age").setValue(userAge);
            mRef.child("commLang").setValue(language);
            mRef.child("Chat").child("TotalMessages").setValue("0");
            mRef.child("timestamp").setValue(0);
            mRef.child("Premium").setValue(false);

            startActivity(new Intent(InformationActivity.this, MainScreen.class));
        }
        else {
            Toast.makeText(InformationActivity.this, getString(R.string.both_fields), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.both_fields), Toast.LENGTH_SHORT).show();
    }
}
