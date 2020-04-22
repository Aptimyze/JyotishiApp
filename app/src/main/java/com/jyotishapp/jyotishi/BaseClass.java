package com.jyotishapp.jyotishi;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    LinearLayout incomingCall, accceptButton, declineButton;

    public void onIncomingCall(){
        incomingCall = (LinearLayout) findViewById(R.id.incoming_call);
        accceptButton = (LinearLayout) findViewById(R.id.accept_button);
        declineButton = (LinearLayout) findViewById(R.id.decline_button);

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

        accceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.setValue(false);
                Intent intent = new Intent(getApplicationContext(), VoiceCallActivity.class);
                intent.putExtra("incomingCall", "true");
                startActivity(intent);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.setValue(false);
                incomingCall.setVisibility(View.GONE);
                incomingCall.setClickable(true);
                Toast.makeText(getApplicationContext(), "Call Declined", Toast.LENGTH_LONG).show();
            }
        });
    }

}
