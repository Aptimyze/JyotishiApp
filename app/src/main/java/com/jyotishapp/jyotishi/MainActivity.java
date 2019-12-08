package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

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
        for(int i=0; i<bmb.getPiecePlaceEnum().pieceNumber(); i++){
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.shape_oval_normal)
                    .normalTextRes(R.string.texty)
                    .containsSubText(false);
            bmb.addBuilder(builder);
        }
    }
}
