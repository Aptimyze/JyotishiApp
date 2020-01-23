package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    LinearLayout help_butt, back_button;
    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView messages;
    FirebaseAuth mAuth;
    LinearLayout messageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();

        help_butt = (LinearLayout) findViewById(R.id.help_butt);
        back_button = (LinearLayout) findViewById(R.id.back_button);
        messages = (RecyclerView) findViewById(R.id.messages);
        messageContainer = (LinearLayout) findViewById(R.id.layoutContainer);

        messages.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Name");

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("Chat")
                .child("Messages");

        inflateMessages(query);

    }

    public void inflateMessages(Query query){
        FirebaseRecyclerOptions<Messages> options = new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(query, Messages.class)
                .build();

        FirebaseRecyclerAdapter FBRA = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Messages model) {
//                    final String message = getRef(position).getKey();
                    if(!model.getSender().equals("Jyotish")){
                        holder.setSender("You");
                        holder.setTextMessage(model.getTextMessage());
                        holder.setTime(model.getTime());
                    }
                    else {
                        messageContainer.setGravity(Gravity.LEFT);
                        messageContainer.setBackgroundResource(R.drawable.senderchatmessage);
                        holder.setSender("Jyotish Ji");
                        holder.setTextMessage(model.getTextMessage());
                        holder.setTime(model.getTime());
                    }
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);
                return new MessageViewHolder(view);
            }
        };
        FBRA.startListening();
        messages.setAdapter(FBRA);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MessageViewHolder(@NonNull View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setTextMessage(String message){
            TextView textMessage = (TextView) mView.findViewById(R.id.textMessage);
            textMessage.setText(message);
        }

        public void setSender(String sender){
            TextView senderView = (TextView) mView.findViewById(R.id.sender);
            senderView.setText(sender);
        }

        public void setTime(String time){
            TextView timeDate = (TextView) mView.findViewById(R.id.time);
            timeDate.setText(time);
        }
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
