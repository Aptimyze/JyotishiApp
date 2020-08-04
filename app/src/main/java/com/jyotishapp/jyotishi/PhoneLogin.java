package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;


public class PhoneLogin extends AppCompatActivity {

    CountryCodePicker ccp;
    Button requestOtp;
    EditText number;
    OtpEditText otp;
    TextView resendOtp, enterOtp, didntGetOtp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String verificationId;
    FirebaseDatabase database;
    DatabaseReference mRef;
    View loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //references
        ccp = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        requestOtp = (Button) findViewById(R.id.reqestotp);
        number = (EditText) findViewById(R.id.phoneNumber);
        otp = (OtpEditText) findViewById(R.id.otp);
        resendOtp = (TextView) findViewById(R.id.resendOtpLink);
        ccp.registerCarrierNumberEditText(number);
        enterOtp = (TextView) findViewById(R.id.enterOtp);
        didntGetOtp = (TextView) findViewById(R.id.didntGetOtp);
        loadingView = (View) findViewById(R.id.loading_view);

        //backend references
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users");

        //phone login
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.v("AAA", "Verification Completed");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.v("AAA", e.getMessage() + " failed verification");
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                Log.v("AAA", "OTP Sent successfully");
                Toast.makeText(PhoneLogin.this, getString(R.string.code_sent), Toast.LENGTH_SHORT).show();
            }


        };

        currentUser = mAuth.getCurrentUser();

        otp.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                loadingView.setVisibility(View.VISIBLE);
                verifyNumberWithCode(otp.getText().toString(), verificationId);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.v("AAA", "Back Pressed");
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void requested(View view) {
        closeKeyboard();
        String fNumber = ccp.getFullNumberWithPlus();
        if (!ccp.isValidFullNumber()) {
            Toast.makeText(PhoneLogin.this, getString(R.string.enter_valid_number), Toast.LENGTH_LONG).show();
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
        otpProcessing(fNumber);
        Toast.makeText(PhoneLogin.this, getString(R.string.otp_request_sent), Toast.LENGTH_SHORT).show();
    }

    protected void otpProcessing(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                10,
                TimeUnit.MILLISECONDS,
                this,
                mCallbacks
        );
    }

    protected void verifyNumberWithCode(String otp, String verificationId) {
        if (verificationId == null) {
            Toast.makeText(PhoneLogin.this, getString(R.string.error_occured), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //TODO: spinner stop
                            Log.v("AAA", "Sign in successful");
                            Toast.makeText(PhoneLogin.this, getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
                            mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Name");
                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        startActivity(new Intent(PhoneLogin.this, InformationActivity.class));
                                        return;
                                    } else {
                                        startActivity(new Intent(PhoneLogin.this, MainScreen.class));
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                            startActivity(new Intent(PhoneLogin.this, MainScreen.class));

                        } else {
                            Toast.makeText(PhoneLogin.this, getString(R.string.enter_correct_otp), Toast.LENGTH_SHORT).show();
                            loadingView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void resendOTP(View view) {
        closeKeyboard();
        String fNumber = ccp.getFullNumberWithPlus();
        if (!ccp.isValidFullNumber()) {
            Toast.makeText(PhoneLogin.this, getString(R.string.enter_valid_number), Toast.LENGTH_LONG).show();
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
        otpProcessing(fNumber);
        Toast.makeText(PhoneLogin.this, getString(R.string.otp_request_sent), Toast.LENGTH_SHORT).show();
    }
}
