package com.jyotishapp.jyotishi;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

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
    BoomMenuButton bmb;
    FirebaseAuth mAuth;
    FabSpeedDial fabsd;
    LinearLayout imageBorder;
    FirebaseDatabase database;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainScreen.this, MainActivity.class));
            Toast.makeText(MainScreen.this, "Please Login Again", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, hh:mm a", Locale.US);
        Date date = new Date(System.currentTimeMillis());
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users");
        mRef = mRef.child(mAuth.getCurrentUser().getUid());
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
                .normalText("Video");
        bmb.addBuilder(builder1);

        fabsd.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        Toast.makeText(MainScreen.this, "Profile", Toast.LENGTH_SHORT).show();
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

        //rotating the border
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imageBorder.setAnimation(rotateAnimation);
    }

    @Override
    public void onBackPressed() {
    }

    public void picClick(View view){
        bmb.boom();
    }

    public void logout(){
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
