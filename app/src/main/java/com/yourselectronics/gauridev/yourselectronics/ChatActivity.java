package com.yourselectronics.gauridev.yourselectronics;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.MessageModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private ImageButton mSendBtn, mAddBtn;
    private RecyclerView mMessageRCView;

    private List<MessageModel> messageList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageViewAdapter messageViewAdapter;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private String current_uid;
    private EditText mEditTextMessage;
    private Toolbar mToolbar;
    private Parcelable state;
    private String agent_user_id ;
    private TextView mTBName;
    private TextView mTBStatus;
    private ImageView mTBBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mToolbar = findViewById(R.id.chat_toolbar);

        mTBBackBtn = findViewById(R.id.chat_tb_back_btn);
        mTBBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

      //  setUpToolbar();

        if (getIntent() != null) {
            agent_user_id = getIntent().getStringExtra("agent_user_uid");
        }


        mTBName = findViewById(R.id.chat_tb_name);
        mTBStatus = findViewById(R.id.chat_tb_status);


        mEditTextMessage = findViewById(R.id.message_send_text);
        messageViewAdapter = new MessageViewAdapter(messageList);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        current_uid = mAuth.getCurrentUser().getUid();

        mSendBtn = findViewById(R.id.send_live_message_btn);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!agent_user_id.isEmpty()){
                    sendMessage(agent_user_id);
                }
            }
        });

        mMessageRCView = findViewById(R.id.message_list_rc_view);

        mMessageRCView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mMessageRCView.setLayoutManager(linearLayoutManager);
        mMessageRCView.setAdapter(messageViewAdapter);
        if (!agent_user_id.isEmpty()){
            loadAllMessage(agent_user_id);
        }
        loadTBContent();

    }

    private void loadTBContent() {
        mRootRef.child("agent_list").child(agent_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String agent_name = dataSnapshot.child("user_name").getValue(String.class);
                mTBName.setText(agent_name);

                if (dataSnapshot.child("user_status").getValue(Boolean.class)==true){
                    mTBStatus.setText("Online");
                }
                else {
                    mTBStatus.setText("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpToolbar() {
        mToolbar.setTitle("Chat");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendMessage(final String agent_user_id) {

        String message = mEditTextMessage.getText().toString();
        if (!message.isEmpty()) {

            String current_user_ref = "chat_messages/"+current_uid +"/"+ agent_user_id;
            String agent_user_ref = "chat_messages/" +agent_user_id +"/" + current_uid;

            DatabaseReference message_push = mRootRef.child("chat_messages").child(current_uid).child(agent_user_id).push();

            String push_id = message_push.getKey();

            Map message_data = new HashMap<>();
            message_data.put("message",message);
            message_data.put("seen",false);
            message_data.put("from",current_uid);
            message_data.put("time",System.currentTimeMillis());



            Map both_message_data = new HashMap<>();
            both_message_data.put(current_user_ref+"/"+push_id, message_data);
            both_message_data.put(agent_user_ref+"/"+push_id, message_data);





            mRootRef.updateChildren(both_message_data, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                   if (databaseError==null) {
                       mEditTextMessage.setText("");
                       Toast.makeText(getApplicationContext(), "Message Sent...", Toast.LENGTH_LONG).show();
                   }
                   else {
                       Toast.makeText(getApplicationContext(), "Sending Failed...", Toast.LENGTH_LONG).show();

                   }

                }
            });

            mRootRef.child("user_database").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String user_name = dataSnapshot.child("name").getValue(String.class);

                    Map user_data = new HashMap<>();
                    user_data.put("user_name",user_name);

                    DatabaseReference new_ref = mRootRef.child("chat_page").child(agent_user_id).child(current_uid);
                    new_ref.updateChildren(user_data, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError!=null){

                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else {
            Toast.makeText(getApplicationContext(),"Please enter message",Toast.LENGTH_LONG).show();
        }
    }

    private void loadAllMessage(String agent_user_id) {
        mRootRef.child("chat_messages").child(current_uid).child(agent_user_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);

                messageList.add(messageModel);
                messageViewAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
