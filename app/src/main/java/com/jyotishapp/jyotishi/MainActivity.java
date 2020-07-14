package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Util;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    BoomMenuButton bmb;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private int RC_SIGNIN = 9001;
    FirebaseDatabase database;
    DatabaseReference mRef;
    TextView tnc, pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, MainScreen.class));
            finish();
            return;
        }

        tnc = (TextView) findViewById(R.id.tnc);
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TnCActivity.class));
            }
        });

        pp = (TextView) findViewById(R.id.pp);
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
            }
        });


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        //animation buttons
        bmb = (BoomMenuButton) findViewById(R.id.bmbHam);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setBottomHamButtonTopMargin(Util.dp2px(70));

        //google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("69525632215-182s7a2e18dvi6rqmpnveci143t3olv3.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        addBuilder();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    public void addBuilder() {
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_mail)
                .normalTextRes(R.string.mail)
                .typeface(Typeface.DEFAULT_BOLD)
                .normalColorRes(R.color.button0)
                .highlightedColorRes(R.color.bittonHigh0)
                .containsSubText(false)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        googleSignIn();
                    }
                })
                .imagePadding(new Rect(10, 10, 10, 1));
        HamButton.Builder builder1 = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_phone)
                .normalTextRes(R.string.phone)
                .normalColorRes(R.color.button1)
                .highlightedColorRes(R.color.colorAccent)
                .typeface(Typeface.DEFAULT_BOLD)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent phoneIntent = new Intent(MainActivity.this, PhoneLogin.class);
                        startActivity(phoneIntent);
                    }
                })
                .containsSubText(false)
                .imagePadding(new Rect(10, 10, 10, 10));
        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_exit)
                .normalTextRes(R.string.exit)
                .typeface(Typeface.DEFAULT_BOLD)
                .normalColorRes(R.color.button2)
                .highlightedColorRes(R.color.buttonHigh2)
                .containsSubText(false)
                .buttonHeight(Util.dp2px(60))
                .buttonWidth(Util.dp2px(200))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                        finish();
                    }
                })
                .imagePadding(new Rect(10, 10, 10, 10));
        bmb.addBuilder(builder);
        bmb.addBuilder(builder1);
        bmb.addBuilder(builder2);

    }

    protected void googleSignIn() {
        if (firebaseUser != null)
            Toast.makeText(MainActivity.this, getString(R.string.signed_in_as) + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mAuth.getCurrentUser() != null)
            return;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Toast.makeText(MainActivity.this, getString(R.string.signed_in_as) + account.getDisplayName(), Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, getString(R.string.couldnt_sign_in), Toast.LENGTH_SHORT).show();
                String error = e.getMessage();
                if(error!=null)
                Log.d("AAA",error);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.v("AAAA", "Success");
                            mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Name");
                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        startActivity(new Intent(MainActivity.this, InformationActivity.class));
                                        return;
                                    } else {
                                        startActivity(new Intent(MainActivity.this, MainScreen.class));
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                            startActivity(new Intent(MainActivity.this, MainScreen.class));
//
//
//                            startActivity(new Intent(MainActivity.this, MainScreen.class));
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
