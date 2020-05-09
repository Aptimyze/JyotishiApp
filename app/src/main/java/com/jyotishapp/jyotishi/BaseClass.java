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

public abstract class BaseClass extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    LinearLayout incomingCall, accceptButton, declineButton,
        incomingVideoCall, accceptVideoButton, declineVideoButton;

    public void onIncomingCall(){
        incomingCall = (LinearLayout) findViewById(R.id.incoming_call);
        accceptButton = (LinearLayout) findViewById(R.id.accept_button);
        declineButton = (LinearLayout) findViewById(R.id.decline_button);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mRef.child("IncomingCall").addValueEventListener(new ValueEventListener() {
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
                mRef.child("IncomingCall").setValue(false);
                Intent intent = new Intent(getApplicationContext(), VoiceCallActivity.class);
                intent.putExtra("incomingCall", "true");
                startActivity(intent);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("IncomingCall").setValue(false);
                incomingCall.setVisibility(View.GONE);
                incomingCall.setClickable(true);
                Toast.makeText(getApplicationContext(), "Call Declined", Toast.LENGTH_LONG).show();
                VoiceCall voiceCall = new VoiceCall("Jyotish Id", "JyotishJi", "jyotish@gmail.com",
                        -System.currentTimeMillis(), "missed", 0 );
                String pushKey = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("voiceCalls").push().getKey();
                database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("voiceCalls")
                        .child(pushKey)
                        .setValue(voiceCall);
            }
        });
    }

    public void onIncomingVideoCall(){
        incomingVideoCall = (LinearLayout) findViewById(R.id.incoming_video_call);
        accceptVideoButton = (LinearLayout) findViewById(R.id.accept_video_button);
        declineVideoButton = (LinearLayout) findViewById(R.id.decline_video_button);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mRef.child("IncomingVideoCall").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals(true+"")){
                    incomingVideoCall.setVisibility(View.VISIBLE);
                    incomingVideoCall.setClickable(true);
                }
                else {
                    incomingVideoCall.setVisibility(View.GONE);
                    incomingVideoCall.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accceptVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("IncomingVideoCall").setValue(false);
                Intent intent = new Intent(getApplicationContext(), VoiceCallActivity.class);
                intent.putExtra("incomingVideoCall", "true");
                startActivity(intent);
            }
        });

        declineVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("IncomingVideoCall").setValue(false);
                incomingVideoCall.setVisibility(View.GONE);
                incomingVideoCall.setClickable(true);
                Toast.makeText(getApplicationContext(), "Video Call Declined", Toast.LENGTH_LONG).show();
                VideoCall videoCall = new VideoCall("Jyotish Id", "JyotishJi", "jyotish@gmail.com",
                        -System.currentTimeMillis(), "missed" );
                String pushKey = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("videoCalls").push().getKey();
                database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("videoCalls")
                        .child(pushKey)
                        .setValue(videoCall);
            }
        });
    }
}
