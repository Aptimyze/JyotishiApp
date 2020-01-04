package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.view.View;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {
    LinearLayout help_butt, back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();

        help_butt = (LinearLayout) findViewById(R.id.help_butt);
        back_button = (LinearLayout) findViewById(R.id.back_button);

    }

    public void back_butt_click(View view){
        finish();
    }

    public void help_butt_click(View view){
        PopupMenu popupMenu = new PopupMenu(ChatActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.help_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.clear_chat:
                        Toast.makeText(ChatActivity.this, "Chat cleared", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }


}
