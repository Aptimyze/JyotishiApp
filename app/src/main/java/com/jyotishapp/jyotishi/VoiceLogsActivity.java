package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VoiceLogsActivity extends AppCompatActivity {

    private RecyclerView callLogs;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

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
            protected void onBindViewHolder(@NonNull VoiceCallHolder holder, int position, @NonNull VoiceCall model) {
                holder.setName(model.getContactUserName());
                holder.setContact(model.getContactInfo());
                holder.setLogIcon(model.getType());
                holder.setTimeDuration(model.getTimestamp(), model.getDuration());
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
