package com.yourselectronics.gauridev.yourselectronics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.MessageModel;
import com.yourselectronics.gauridev.yourselectronics.Model.SupportMemberModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.SupportMemberViewHolder;

public class ChatHomeActivity extends AppCompatActivity {

    private RecyclerView mRcView;
    private DatabaseReference mRootRef;
    private FirebaseRecyclerAdapter<SupportMemberModel, SupportMemberViewHolder>  recyclerAdapter;
    private String current_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);

        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRcView = findViewById(R.id.agent_list_rc_view);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRcView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        recyclerAdapter = new FirebaseRecyclerAdapter<SupportMemberModel, SupportMemberViewHolder>(
                SupportMemberModel.class,
                R.layout.agent_list_card_view,
                SupportMemberViewHolder.class,
                mRootRef.child("agent_list")
        ) {
            @Override
            protected void populateViewHolder(final SupportMemberViewHolder viewHolder, final SupportMemberModel model, final int position) {
                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.isUser_status());
                viewHolder.setUser_profile_img(model.getUser_profile_img());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ChatHomeActivity.this,ChatActivity.class);
                        intent.putExtra("agent_user_uid", recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

                Query query =  mRootRef.child("chat_messages").child(current_uid).child(recyclerAdapter.getRef(position).getKey()).limitToLast(1);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MessageModel messageModel = snapshot.getValue(MessageModel.class);
                            viewHolder.recent_message.setText(messageModel.getMessage());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };

        mRcView.setAdapter(recyclerAdapter);

    }
}
