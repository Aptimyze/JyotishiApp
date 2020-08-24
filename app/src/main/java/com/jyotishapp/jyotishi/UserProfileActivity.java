package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.view.View;

import org.w3c.dom.Text;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    TextView name, age, lang, contact;
    DatabaseReference mRef;
    String nameI = "", ageI ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gradientEndYellow)));


        //references
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        lang = (TextView) findViewById(R.id.lang);
        contact = (TextView) findViewById(R.id.contact);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        getSupportActionBar().setTitle(R.string.profile);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameI = dataSnapshot.child("Name").getValue().toString();
                ageI = dataSnapshot.child("Age").getValue().toString();
                if(dataSnapshot.child("commLang").getValue() != null){
                    lang.setText(dataSnapshot.child("commLang").getValue().toString());
                }
                name.setText(nameI);
                age.setText(ageI + " years");
                if(mAuth.getCurrentUser().getPhoneNumber() !=null &&
                        !mAuth.getCurrentUser().getPhoneNumber().equals(""))
                    contact.setText(mAuth.getCurrentUser().getPhoneNumber());
                else
                    contact.setText("N/A");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void editProfileClicked(View view){
        Intent intent = new Intent(UserProfileActivity.this, InformationActivity.class);
        intent.putExtra("name", nameI);
        intent.putExtra("age", ageI);
        startActivity(intent);
        finish();
    }
}
