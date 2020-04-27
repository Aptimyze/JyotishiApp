package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;

import java.io.IOException;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BuyPremiumActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private CircularProgressButton buyButton;
    private PaytmOrder paytmOrder;
    private PaymentHTTPRequestAsync paymentHTTPRequestAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_premium);
        getSupportActionBar().setTitle("Premium");
        getSupportActionBar().setHomeButtonEnabled(true);

        buyButton = (CircularProgressButton) findViewById(R.id.buy_button);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Premium");
//        paytmOrder = new PaytmOrder()

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyButton.startAnimation();
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals(true+"")) {
                    buyButton.revertAnimation();
                    Toast.makeText(BuyPremiumActivity.this, "You are now a premium member, enjoy the benefits",
                            Toast.LENGTH_LONG).show();
//                    buyButton.doneLoadingAnimation(R.color.defaultContentColor, );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        paymentHTTPRequestAsync = new PaymentHTTPRequestAsync();
        paymentHTTPRequestAsync.execute();
    }


}
