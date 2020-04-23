package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class VoiceLogsActivity extends AppCompatActivity {

    private RecyclerView callLogs;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    LinearLayout faceCard, expandableView;
    ImageView userIcon, viewProfile, viewChat, viewVoiceCall, viewVideoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_logs);
        getSupportActionBar().setTitle(R.string.voice_call_logs);
        getSupportActionBar().setHomeButtonEnabled(true);

        callLogs = (RecyclerView) findViewById(R.id.logs);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        callLogs.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("voiceCalls")
                .orderByChild("timestamp");

        inflateMessages(query);

    }

    void inflateMessages(Query query){
        FirebaseRecyclerOptions<VoiceCall> options = new FirebaseRecyclerOptions.Builder<VoiceCall>()
                .setQuery(query, VoiceCall.class)
                .build();

        final FirebaseRecyclerAdapter FBRA = new FirebaseRecyclerAdapter<VoiceCall, VoiceCallHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final VoiceCallHolder holder, int position, @NonNull VoiceCall model) {
                holder.setName(model.getContactUserName());
                holder.setContact(model.getContactInfo());
                holder.setLogIcon(model.getType());
                holder.setTimeDuration(model.getTimestamp(), model.getDuration());
                faceCard = (LinearLayout) holder.mView.findViewById(R.id.face_card);
                faceCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandableView = (LinearLayout) holder.mView.findViewById(R.id.expanded_view);
                        if(expandableView.getVisibility() == View.GONE) {
                            expandableView.animate()
                                    .translationY(0)
                                    .setDuration(1000)
                                    .alpha(1.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            super.onAnimationStart(animation);
                                            expandableView.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                        else {
                            expandableView.setVisibility(View.GONE);
//                            expandableView.animate()
//                                    .translationY(0)
//                                    .setDuration(1000)
//                                    .setListener(new AnimatorListenerAdapter() {
//                                        @Override
//                                        public void onAnimationEnd(Animator animation) {
//                                            super.onAnimationEnd(animation);
//
//                                        }
//                                    });

                        }
                    }
                });
                userIcon = (ImageView) holder.mView.findViewById(R.id.user_ico);
                Random rand = new Random();
                int x = rand.nextInt(5);
                switch (x){
                    case 1: userIcon.setColorFilter(Color.argb(255, 0, 128, 255));
                            break;
                    case 0: userIcon.setColorFilter(Color.argb(255, 246, 145, 65));
                        break;
                    case 2: userIcon.setColorFilter(Color.argb(255, 255, 97, 187));
                        break;
                    case 3: userIcon.setColorFilter(Color.argb(255, 173, 92, 247));
                        break;
                    case 4: userIcon.setColorFilter(Color.argb(255, 90, 179, 113));
                        break;
                }
                viewChat = (ImageView) holder.mView.findViewById(R.id.view_chat);
                viewProfile = (ImageView) holder.mView.findViewById(R.id.view_profile);
                viewVoiceCall = (ImageView) holder.mView.findViewById(R.id.view_voice_call);
                viewVideoCall = (ImageView) holder.mView.findViewById(R.id.view_video_call);
                viewChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(VoiceLogsActivity.this, ChatActivity.class));
                    }
                });
                viewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(VoiceLogsActivity.this, JyotishProfilesActivity.class));
                    }
                });
                viewVideoCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(VoiceLogsActivity.this, VidCallActivity.class));
                    }
                });
                viewVoiceCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(VoiceLogsActivity.this, VoiceCallActivity.class));
                    }
                });
             }

            @NonNull
            @Override
            public VoiceCallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_card, parent, false);
                return new VoiceCallHolder(itemView);
            }

        };
        FBRA.startListening();
        callLogs.setAdapter(FBRA);
    }

    public class VoiceCallHolder extends RecyclerView.ViewHolder{
        View mView;
        public VoiceCallHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        void setName(String name){
            TextView nameT = (TextView) mView.findViewById(R.id.name);
            nameT.setText(name);
        }

        void setContact(String contact){
            TextView contactT = (TextView) mView.findViewById(R.id.contact);
            contactT.setText(contact);
        }

        void setTimeDuration(long timestampp, long duration){
            long timeStamp = Math.abs(timestampp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M, h:mm a");
            Date date = new Date(timeStamp);
            StringBuilder format = new StringBuilder();
            format.append(sdf.format(date)+", ");
            TextView durationT = (TextView) mView.findViewById(R.id.duration);
            long mins = duration/60, secs = duration%60;
            if(mins>0)
                format.append(mins+"m "+secs+"s");
            else
                format.append(secs+"s");
            durationT.setText(format);
        }

        void setLogIcon(String type){
            ImageView typeT = (ImageView) mView.findViewById(R.id.type);
            if(type.equals("incom")){
                typeT.setImageResource(R.drawable.call_received);
                typeT.setColorFilter(Color.argb(255, 0, 255, 0));
            }
            else if(type.equals("outgo")){
                typeT.setImageResource(R.drawable.call_made);
                typeT.setColorFilter(Color.argb(255, 0, 255, 0));
            }
            else{
                typeT.setImageResource(R.drawable.call_missed);
                typeT.setColorFilter(Color.argb(255, 255, 0, 0));
            }
        }
    }
}
