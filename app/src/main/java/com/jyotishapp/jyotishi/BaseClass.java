package com.jyotishapp.jyotishi;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BaseClass extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    LinearLayout incomingCall;

    public void onIncomingCall(){
        incomingCall = (LinearLayout) findViewById(R.id.incoming_call);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("IncomingCall");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals(true+"")){
                    incomingCall.setVisibility(View.VISIBLE);
                    incomingCall.setClickable(true);
                }
                else {
                    incomingCall.setVisibility(View.GONE);
                    incomingCall.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
