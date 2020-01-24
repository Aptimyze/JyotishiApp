package com.jyotishapp.jyotishi;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainScreen extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID = 22;
    BoomMenuButton bmb;
    FirebaseAuth mAuth;
    FabSpeedDial fabsd;
    LinearLayout imageBorder;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainScreen.this, MainActivity.class));
            Toast.makeText(MainScreen.this, "Please Login Again", Toast.LENGTH_SHORT).show();
        }
        for(int i=0; i<2; i++){
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, hh:mm a", Locale.US);
        Date date = new Date(System.currentTimeMillis());
        try {
            database = FirebaseDatabase.getInstance();
            mRef = database.getReference().child("Users");
            mRef = mRef.child(mAuth.getCurrentUser().getUid());
        }catch (Exception e) {
            startActivity(new Intent(MainScreen.this, MainActivity.class));
            finish();
            Toast.makeText(MainScreen.this, "Please Login Again", Toast.LENGTH_SHORT).show();
            return;
        }
        mRef.child("UserId").setValue(mAuth.getCurrentUser().getUid());
        mRef.child("Last Active").setValue(sdf.format(date));

        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().hide();

        bmb = (BoomMenuButton) findViewById(R.id.mainPic);
        fabsd = (FabSpeedDial) findViewById(R.id.tool);
        imageBorder = (LinearLayout) findViewById(R.id.imageBorder);

        TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.mess)
                .normalText("Chat")
                .imagePadding(new Rect(15, 15, 15, 15))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        startActivity(new Intent(MainScreen.this, ChatActivity.class));
                    }
                });
        bmb.addBuilder(builder);
        TextInsideCircleButton.Builder builder1 = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.vid)
                .imagePadding(new Rect(15, 15, 15, 15))
                .normalText("Video")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        startActivity(new Intent(MainScreen.this, VidCallActivity.class));
                    }
                });
        bmb.addBuilder(builder1);

        fabsd.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(MainScreen.this, UserProfileActivity.class));
                        Log.v("AAA", menuItem.getItemId()+" " + R.id.profile);
                        break;
                    case R.id.signout:
                        Toast.makeText(MainScreen.this, "Logged Out", Toast.LENGTH_SHORT).show();
                        Log.v("AAA", menuItem.getItemId()+"");
                        logout();
                        break;
                        default:
                            Toast.makeText(MainScreen.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        OneSignal.startInit(this)
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

        //rotating the border
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imageBorder.setAnimation(rotateAnimation);
    }

    private boolean checkSelfPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(this, permission ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            Log.v("AAA", permission);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    public void picClick(View view){
        bmb.boom();
    }

    public void exploreOptionsClicked(View view){
        bmb.boom();
    }

    public void viewJyotishProfilesClicked(View view){
        startActivity(new Intent(MainScreen.this, JyotishProfilesActivity.class));
    }

    public void logout(){
        OneSignal.setSubscription(false);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainScreen.this, MainActivity.class));
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
