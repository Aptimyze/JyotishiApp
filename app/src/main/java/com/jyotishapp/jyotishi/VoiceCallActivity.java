package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class VoiceCallActivity extends AppCompatActivity {
    private final static int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private RtcEngine rtcEngine;
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
    };

    private void onRemoteUserLeft(int uid, int reason){
        Toast.makeText(VoiceCallActivity.this, "The user has left.", Toast.LENGTH_LONG).show();
    }

    private void onRemoteUserMutedAudio(int uid, boolean muted){
        Toast.makeText(VoiceCallActivity.this, "The user has put you call on hold.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        if(checkSelfPermission(PERMISSION_REQ_ID_RECORD_AUDIO))
            initializeAgoraEngineAndJoinChannel();
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
        rtcEngine.leaveChannel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        leaveChannel();
        RtcEngine.destroy();
        rtcEngine = null;
    }
}
