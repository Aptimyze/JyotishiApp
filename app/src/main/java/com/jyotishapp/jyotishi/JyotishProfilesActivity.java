package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class JyotishProfilesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jyotish_profiles);
        getSupportActionBar().setTitle("Your Jyotish's");
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.jyotishList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
//        JyotishListAdapter jla = new JyotishListAdapter()
    }
}
