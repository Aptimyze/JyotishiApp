package com.jyotishapp.jyotishi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    LinearLayout help_butt, back_button;
    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView messages;
    FirebaseAuth mAuth;
    LinearLayout messageContainer, parentContainer;
    EditText typedMessage;
    int messageNo =-1;
    String name, nid;
    LinearLayoutManager rvllm = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();
        rvllm.setStackFromEnd(true);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                        .child("Name").getValue().toString();
                nid = dataSnapshot.child("Admin").child("NotificationKey").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        help_butt = (LinearLayout) findViewById(R.id.help_butt);
        back_button = (LinearLayout) findViewById(R.id.back_button);
        messages = (RecyclerView) findViewById(R.id.messages);
        typedMessage = (EditText) findViewById(R.id.typedMessage);

        messages.setLayoutManager(rvllm);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        getMessageNo();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("Chat")
                .child("Messages");

        inflateMessages(query);

    }

    public void getMessageNo(){
        mRef.child("Chat").child("TotalMessages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageNo = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void inflateMessages(Query query){
        FirebaseRecyclerOptions<Messages> options = new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(query, Messages.class)
                .build();

        final FirebaseRecyclerAdapter FBRA = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Messages model) {
//                    final String message = getRef(position).getKey();
                    if(!model.getSender().equals("Jyotish")){
                        holder.setOriginContainer();
                        holder.setSender("You");
                        holder.setTextMessage(model.getTextMessage());
                        holder.setTime(model.getTime());
                    }
                    else {
                        holder.setContainer();
                        holder.setSender("Jyotish");
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
//        FBRA.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//
////            @Override
////            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
////                rvllm.smoothScrollToPosition(messages, null, FBRA.getItemCount());
////            }
////
////            @Override
////            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
////                rvllm.smoothScrollToPosition(messages, null, FBRA.getItemCount());
////            }
//
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                rvllm.smoothScrollToPosition(messages, null, FBRA.getItemCount());
//            }
//        });
        messages.setAdapter(FBRA);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MessageViewHolder(@NonNull View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setOriginContainer(){
            parentContainer = (LinearLayout) mView.findViewById(R.id.parentContainer);
            parentContainer.setGravity(Gravity.LEFT);
            messageContainer = (LinearLayout) mView.findViewById(R.id.layoutContainer);
            messageContainer.setGravity(Gravity.LEFT);
            messageContainer.setBackgroundResource(R.drawable.chatmessage);
        }

        public void setContainer(){
            parentContainer = (LinearLayout) mView.findViewById(R.id.parentContainer);
            parentContainer.setGravity(Gravity.LEFT);
            messageContainer = (LinearLayout) mView.findViewById(R.id.layoutContainer);
            messageContainer.setGravity(Gravity.LEFT);
            messageContainer.setBackgroundResource(R.drawable.senderchatmessage);
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

    public void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void sendButtonClicked(View view){
        closeKeyboard();
        String messageTyped = typedMessage.getText().toString().trim();
        if(!TextUtils.isEmpty(messageTyped)){
            if(messageNo!=-1){
                messageNo++;
                DatabaseReference chatRef = mRef.child("Chat").child("Messages");
                chatRef.child(messageNo+"").child("messageId").setValue(messageNo+"");
                chatRef.child(messageNo+"").child("sender").setValue("User");
                chatRef.child(messageNo+"").child("textMessage").setValue(messageTyped);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM, hh:mm a");
                Date date = new Date();
                chatRef.child(messageNo+"").child("time").setValue(sdf.format(date));
                mRef.child("Chat").child("TotalMessages").setValue(messageNo+"");
                typedMessage.setText("");
                long timestamp = System.currentTimeMillis();
                mRef.child("timestamp").setValue(-timestamp);
                mRef.child("lastMessage").setValue(messageTyped);
                mRef.child("newMessage").setValue("1");
                new SendNotificationForCall(messageTyped, "New message from " + name, nid);
                messageNo=-1;
                getMessageNo();
            }
            else
                Toast.makeText(ChatActivity.this, getString(R.string.unable_send), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ChatActivity.this, getString(R.string.empty_message), Toast.LENGTH_SHORT).show();
        }
    }

    public void help_butt_click(View view){
        PopupMenu popupMenu = new PopupMenu(ChatActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.help_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.clear_chat:
                        Toast.makeText(ChatActivity.this, getString(R.string.chat_cleared), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }


}
