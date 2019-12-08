package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Util;

public class MainActivity extends AppCompatActivity {
    BoomMenuButton bmb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bmb = (BoomMenuButton) findViewById(R.id.bmbHam);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setBottomHamButtonTopMargin(Util.dp2px(70));
        addBuilder();
    }

    public void addBuilder(){
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.mail)
                .normalTextRes(R.string.mail)
                .typeface(Typeface.DEFAULT_BOLD)
                .normalColorRes(R.color.button0)
                .highlightedColorRes(R.color.bittonHigh0)
                .textPadding(new Rect(0, 30, 0, 0))
                .containsSubText(false)
                .imagePadding(new Rect(10, 10, 10, 1));
        HamButton.Builder builder1 = new HamButton.Builder()
                .normalImageRes(R.drawable.phone)
                .normalTextRes(R.string.phone)
                .normalColorRes(R.color.button1)
                .highlightedColorRes(R.color.colorAccent)
                .typeface(Typeface.DEFAULT_BOLD)
                .textPadding(new Rect(0, 30, 0, 0))
                .containsSubText(false)
                .imagePadding(new Rect(10, 10, 10, 10));
        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.baseline_exit_to_app_white_18dp)
                .normalTextRes(R.string.exit)
                .typeface(Typeface.DEFAULT_BOLD)
                .normalColorRes(R.color.button2)
                .highlightedColorRes(R.color.buttonHigh2)
                .textPadding(new Rect(0, 30, 0, 0))
                .containsSubText(false)
                .buttonHeight(Util.dp2px(60))
                .buttonWidth(Util.dp2px(200))
                .imagePadding(new Rect(10, 10, 10, 10));
            bmb.addBuilder(builder);
            bmb.addBuilder(builder1);
            bmb.addBuilder(builder2);

    }
}
