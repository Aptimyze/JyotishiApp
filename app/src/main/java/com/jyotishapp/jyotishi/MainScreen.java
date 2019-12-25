package com.jyotishapp.jyotishi;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainScreen extends AppCompatActivity {
    BoomMenuButton bmb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().hide();
        bmb = (BoomMenuButton) findViewById(R.id.mainPic);

        TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.mess)
                .normalText("Message Chat")
                .imageRect(new Rect(Util.dp2px(10), Util.dp2px(10), Util.dp2px(70), Util.dp2px(70)));
        bmb.addBuilder(builder);
        TextInsideCircleButton.Builder builder1 = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.vid)
                .normalText("Video Chat");
        bmb.addBuilder(builder1);
    }

    public void picClick(View view){
        bmb.boom();
    }


}
