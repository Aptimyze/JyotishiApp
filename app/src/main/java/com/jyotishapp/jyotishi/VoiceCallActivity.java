package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.Voice;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class VoiceCallActivity extends AppCompatActivity {

    boolean clickable = false, timeSet=false, activityFinished=false;
    boolean muteOn = false, speakerOn = false, holdOn = false;
    private TextView timer;
    private ImageView mute, speaker, hold;
    LinearLayout endCall, muteContainer, holdConatiner, speakerContainer;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef, callAdd;
    private final static int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private RtcEngine rtcEngine;
    private String callType = "outgo";
    private String pushKey = "";
    final Handler timerHandler = new Handler();
    private final IRtcEngineEventHandler rtcEngineEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserOffline(final int uid, final int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft(uid, reason);
                }
            });
        }

        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserMutedAudio(uid, muted);
                }
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
        }
    };

    private void onRemoteUserLeft(int uid, int reason){
        Toast.makeText(VoiceCallActivity.this, "Jyotish Ji has ended the call.", Toast.LENGTH_LONG).show();
        onStop();
        finish();
    }

    private void onRemoteUserMutedAudio(int uid, boolean muted){
        Toast.makeText(VoiceCallActivity.this, "The user has put your call on hold.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mRef.child("Calling").setValue(true);
        mRef.child("Engaged").setValue(false);
        mRef.child("InCallWith").setValue("Jyotish");

        timer = (TextView) findViewById(R.id.timer);
        mute = (ImageView) findViewById(R.id.mute);
        speaker = (ImageView) findViewById(R.id.speaker);
        hold = (ImageView) findViewById(R.id.hold);
        endCall = (LinearLayout) findViewById(R.id.end_call);
        muteContainer = (LinearLayout) findViewById(R.id.mute_container);
        speakerContainer = (LinearLayout) findViewById(R.id.speaker_container);
        holdConatiner = (LinearLayout) findViewById(R.id.hold_container);
        seconds =0;

        //attempt to enable in call mode of speaker but not allowed above android L
//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
//        audioManager.setSpeakerphoneOn(false);
//        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//        Toast.makeText(VoiceCallActivity.this, x+"", Toast.LENGTH_LONG).show();


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("incomingCall").equals("true")) {
                callType = "incom";
                mRef.child("Engaged").setValue(true);
//                changeUiToCall();
            }
        }



        database.getReference().child("Admin").child("InCallWith").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals(mAuth.getUid())){
                    mRef.child("Engaged").setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickable)
                    if(muteOn) {
                        rtcEngine.muteLocalAudioStream(false);
                        Toast.makeText(VoiceCallActivity.this, "Mic on", Toast.LENGTH_LONG).show();
                        muteOn = false;
                        muteContainer.setBackground(getDrawable(R.color.white));
                        mute.setColorFilter(Color.argb(255, 0, 0, 0));
                    } else {
                        rtcEngine.muteLocalAudioStream(true);
                        Toast.makeText(VoiceCallActivity.this, "Mic off", Toast.LENGTH_LONG).show();
                        muteOn = true;
                        muteContainer.setBackground(getDrawable(R.color.black));
                        mute.setColorFilter(Color.argb(255, 255, 255, 255));
                    }
            }
        });

        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickable)
                    if(holdOn) {
                        Toast.makeText(VoiceCallActivity.this, "Call resumed", Toast.LENGTH_LONG).show();
                        holdOn = false;
                        holdConatiner.setBackground(getDrawable(R.color.white));
                        hold.setColorFilter(Color.argb(255, 0, 0, 0));
                    } else {
                        Toast.makeText(VoiceCallActivity.this, "Call put on hold", Toast.LENGTH_LONG).show();
                        holdOn = true;
                        holdConatiner.setBackground(getDrawable(R.color.black));
                        hold.setColorFilter(Color.argb(255, 255, 255, 255));
                    }
            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickable)
                    if(speakerOn) {
                        Toast.makeText(VoiceCallActivity.this, "Hands Free On", Toast.LENGTH_LONG).show();
                        speakerOn = false;
                        speakerContainer.setBackground(getDrawable(R.color.white));
                        speaker.setColorFilter(Color.argb(255, 0, 0, 0));
                    } else {
                        Toast.makeText(VoiceCallActivity.this, "Speaker on", Toast.LENGTH_LONG).show();
                        speakerOn = true;
                        speakerContainer.setBackground(getDrawable(R.color.black));
                        speaker.setColorFilter(Color.argb(255, 255, 255, 255));
                    }
            }
        });

        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VoiceCallActivity.this, "Call ended", Toast.LENGTH_LONG).show();
                onStop();
                finish();
            }
        });

        mRef.child("Engaged").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals(true+"")){
                    if(!activityFinished)
                        changeUiToCall();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(checkSelfPermission(PERMISSION_REQ_ID_RECORD_AUDIO))
            initializeAgoraEngineAndJoinChannel();
    }

    private void changeUiToCall(){
        mute.setColorFilter(Color.argb(255, 0, 0, 0));
        speaker.setColorFilter(Color.argb(255, 0, 0, 0));
        hold.setColorFilter(Color.argb(255, 0, 0, 0));
        timerStart();
        VoiceCall voiceCall = new VoiceCall("Jyotish Id", "JyotishJi", "jyotish@gmail.com",
                -System.currentTimeMillis(), callType, -1 );
        pushKey = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("voiceCalls").push().getKey();
        callAdd = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("voiceCalls").child(pushKey);
        callAdd.setValue(voiceCall);
    }

    int seconds;

    private void timerStart(){
//        ViewGroup parent = (ViewGroup) this.timer.getParent();
//        parent.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        clickable = true;
        timer.setText("00:00");
        mute.setClickable(true);
        speaker.setClickable(true);
        hold.setClickable(true);
        timerHandler.post(new Runnable() {
            @Override
            public void run() {
                String timerText = String.format(Locale.getDefault(), "%02d:%02d", seconds/60, seconds%60);
                timer.setText(timerText);
                seconds++;
                timerHandler.postDelayed(this, 1000);
            }
        });
    }

    private void initializeAgoraEngineAndJoinChannel(){
        initializeAgoraEngine();
        joinChannel();
    }

    private void initializeAgoraEngine(){
        try{
            rtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), rtcEngineEventHandler);
            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        } catch (Exception e){
            throw new RuntimeException("Need to check RTC SDK init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void joinChannel(){
        String accessToken = getString(R.string.access_token);
        if(TextUtils.isEmpty(accessToken) || TextUtils.equals(accessToken, "YOUR ACCESS TOKEN")){
            accessToken = null;
        }
        rtcEngine.joinChannel(accessToken, "voiceDemoChannel", "Extra Optional Data", 0);
    }

    private boolean checkSelfPermission(int permissionReqIdRecordAudio) {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    permissionReqIdRecordAudio);
            return false;
        }
        return true;
    }

    private void leaveChannel(){
        if(rtcEngine!=null)
            rtcEngine.leaveChannel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityFinished = true;
        leaveChannel();
        RtcEngine.destroy();
        rtcEngine = null;
        setTime();
        seconds =-1;
        timerHandler.removeCallbacks(null);
        mRef.child("Calling").setValue(false);
        mRef.child("Engaged").setValue(false);
        finish();
    }

    void setTime(){
        try{
            if(!timeSet) {
                callAdd.child("duration").setValue(seconds);
                timeSet = true;
            }
        } catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        onStop();
    }
}
