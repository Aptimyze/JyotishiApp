package com.jyotishapp.jyotishi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;


public class PhoneLogin extends AppCompatActivity {
    CountryCodePicker ccp;
    MaterialButton requestOtp;
    EditText number;
    OtpEditText otp;
    TextView resendOtp, enterOtp, didntGetOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ccp = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        requestOtp = (MaterialButton) findViewById(R.id.reqestotp);
        number = (EditText) findViewById(R.id.phoneNumber);
        otp = (OtpEditText) findViewById(R.id.otp);
        resendOtp = (TextView) findViewById(R.id.resendOtpLink);
        ccp.registerCarrierNumberEditText(number);
        enterOtp = (TextView) findViewById(R.id.enterOtp);
        didntGetOtp = (TextView) findViewById(R.id.didntGetOtp);
        otp.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                if(otp.getText().toString().equals("123456")){
                    Toast.makeText(PhoneLogin.this, "Login Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PhoneLogin.this, MainScreen.class));
                }
            }
        });
    }

    public void requested(View view)
    {
        String fNumber = ccp.getFullNumberWithPlus();
        if(!ccp.isValidFullNumber()) {
            Toast.makeText(PhoneLogin.this, "Please enter a valid number", Toast.LENGTH_LONG).show();
            return;
        }
        enterOtp.setVisibility(View.VISIBLE);
        otp.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                didntGetOtp.setVisibility(View.VISIBLE);
                resendOtp.setVisibility(View.VISIBLE);
            }
        }, 5000);
        Log.v("AAAA", ccp.getFullNumberWithPlus());
        requestOtp.setClickable(false);
        Toast.makeText(PhoneLogin.this, "OTP request Sent", Toast.LENGTH_SHORT).show();
    }
}
