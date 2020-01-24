package com.jyotishapp.jyotishi;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VidCallActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID = 22;
    private RtcEngine rtcEngine;
    private RelativeLayout mRemoteContainer;
    private FrameLayout mLocalContainer;
    private ImageView mCallButt, mMuteButt, mSwitchCameraButt;
    private SurfaceView mLocalView, mRemoteView;
    private boolean CallEnd, mMuted;
    FirebaseAuth mAuth;
    String name = "";
    int f=0;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid_call);
        getSupportActionBar().hide();

        //get References
        mRemoteContainer = (RelativeLayout) findViewById(R.id.remote_video_view_container);
        mLocalContainer = (FrameLayout) findViewById(R.id.local_video_view_container);
        mCallButt = (ImageView) findViewById(R.id.btn_call);
        mMuteButt = (ImageView) findViewById(R.id.btn_mute);
        mSwitchCameraButt = (ImageView) findViewById(R.id.btn_switch_camera);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("CurrentVidCall").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    FirebaseDatabase.getInstance().getReference().child("CurrentVidCall").setValue(mAuth.getCurrentUser().getUid());
                    OneSignal.startInit(VidCallActivity.this)
                            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                            .unsubscribeWhenNotificationsAreDisabled(true)
                            .init();
                    OneSignal.setSubscription(true);
                    OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                        @Override
                        public void idsAvailable(String userId, String registrationId) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                                    .child("NotificationKey").setValue(userId);
                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("Admin").child("NotificationKey").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            new SendNotificationForCall("Join Video Call" + "\nch" + mAuth.getCurrentUser().getUid(), name+ " is calling you, please join the video call", dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    OneSignal.startInit(VidCallActivity.this)
                            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                            .unsubscribeWhenNotificationsAreDisabled(true)
                            .init();
                    OneSignal.setSubscription(true);
                    OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                        @Override
                        public void idsAvailable(String userId, String registrationId) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                                    .child("NotificationKey").setValue(userId);
                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("Admin").child("NotificationKey").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            new SendNotificationForCall("Missed Video Call"+ "\nch" + mAuth.getCurrentUser().getUid(), "You have a missed call from "+name , dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    f=1;
                    finish();
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(f==1)
            return;

        //checkingg permissions
        if(checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)){
            initEngineAndJoinChannel();
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(this, permission ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            Log.v("AAA", permission);
            return false;
        }
        return true;
    }

    private void initEngineAndJoinChannel(){
        initializeEngine();
        setUpVideoConfig();
        setUpLocalVideo();
        joinChannel();
    }

    private void initializeEngine(){
        try{
            rtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), rtcEngineEventHandler);
        }catch (Exception e){
            Log.v("AAA", e.getMessage().toString());
        }
    }

    private void setUpVideoConfig(){
        rtcEngine.enableVideo();
        rtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        ));
    }

    private void setUpLocalVideo(){
        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        rtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    private void joinChannel(){
        String token = getString(R.string.access_token);
        if(TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN"))
            token = null;
        rtcEngine.joinChannel(token, mAuth.getCurrentUser().getUid(), "Extra Optional Data", 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!CallEnd)
            leaveChannel();
        RtcEngine.destroy();
        FirebaseDatabase.getInstance().getReference().child("CurrentVidCall").removeValue();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void leaveChannel(){
        rtcEngine.leaveChannel();
    }

    private void onRemoteUserLeft(){
        removeRemoteVideo();
    }

    private void removeRemoteVideo(){
        if(mRemoteView != null)
            mRemoteContainer.removeView(mRemoteView);
        mRemoteView = null;
    }

    private final IRtcEngineEventHandler rtcEngineEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("AAA", "Join channer success: " + (uid & 0xFFFFFFFL));
                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("AAA", "First remote video decoded " + uid);
                    setUpRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("AAAA", "User offline " + uid);
                    onRemoteUserLeft();
                }
            });
        }
    };



    private void setUpRemoteVideo(int uid){
        int count = mRemoteContainer.getChildCount();
        View view= null;
        for(int i=0; i<count; i++){
            View v = mRemoteContainer.getChildAt(i);
            if(v.getTag() instanceof Integer && ((int) v.getTag()) == uid)
                view =v;
        }
        if(view!=null)
            return;
        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        rtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
    }

    public void onCallClicked(View view){
        if(CallEnd){
            startCall();
            CallEnd = false;
            mCallButt.setImageResource(R.drawable.stop_call);
        } else{
            endCall();
            CallEnd = true;
            mCallButt.setImageResource(R.drawable.resume_call);
        }
        showButtons(!CallEnd);
    }

    private void startCall(){
        setUpLocalVideo();
        joinChannel();
    }

    private void endCall(){
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
    }

    private void removeLocalVideo(){
        if(mLocalView != null){
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    private void showButtons(boolean CallEnd){
        int visibility = CallEnd ? View.VISIBLE : View.GONE;
        mMuteButt.setVisibility(visibility);
        mSwitchCameraButt.setVisibility(visibility);
    }

    public void onSwitchCameraClicked(View view){
        rtcEngine.switchCamera();
    }

    public void onLocalAudioMuteClicked(View view){
        mMuted = !mMuted;
        rtcEngine.muteLocalAudioStream(mMuted);
        int res= mMuted ? R.drawable.mic_on : R.drawable.mic_off;
        mMuteButt.setImageResource(res);
    }
}
