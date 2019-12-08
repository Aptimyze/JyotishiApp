package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;

public class MainActivity extends AppCompatActivity {
    BoomMenuButton bmb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bmb = (BoomMenuButton) findViewById(R.id.bmbHam);
        bmb.setButtonEnum(ButtonEnum.Ham);
        addBuilder();
    }

    public void addBuilder(){
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.mail)
                .highlightedTextRes(R.string.mail)
                .containsSubText(false)
                .imagePadding(new Rect(10, 10, 10, 10));
        HamButton.Builder builder1 = new HamButton.Builder()
                .normalImageRes(R.drawable.phone)
                .normalTextRes(R.string.phone)
                .containsSubText(false)
                .imagePadding(new Rect(10, 10, 10, 10));
        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.baseline_exit_to_app_white_18dp)
                .normalTextRes(R.string.exit)
                .containsSubText(false)
                .imagePadding(new Rect(10, 10, 10, 10));
            bmb.addBuilder(builder);
            bmb.addBuilder(builder1);
            bmb.addBuilder(builder2);

    }
}
