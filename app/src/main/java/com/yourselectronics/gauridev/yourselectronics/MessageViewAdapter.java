package com.yourselectronics.gauridev.yourselectronics;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yourselectronics.gauridev.yourselectronics.Model.MessageModel;

import java.util.List;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.MessageViewHolder> {
    private List<MessageModel> mMessageList;
    private FirebaseAuth mAuth;
    private View mView;

    public MessageViewAdapter(List<MessageModel> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_layout,parent,false);
        return new MessageViewHolder(view);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public  TextView message_text, message_timestamp;

       public MessageViewHolder(View itemView) {
           super(itemView);
           mView = itemView;
           message_text = itemView.findViewById(R.id.single_message_text);
           message_timestamp = itemView.findViewById(R.id.single_message_timestamp);

       }
   }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();

        String current_uid = mAuth.getCurrentUser().getUid();
        MessageModel messageModel = mMessageList.get(position);

        String from_uid = messageModel.getFrom();





        try {
            if (from_uid.equals(current_uid)) {
                holder.message_text.setTextColor(Color.BLACK);
                holder.message_text.setBackgroundResource(R.drawable.single_message_background_second);

            } else {
                holder.message_text.setTextColor(Color.WHITE);
                holder.message_text.setBackgroundResource(R.drawable.single_message_background);

            }
        }
        catch (Exception e){
            Toast.makeText(mView.getContext(),e.toString(),Toast.LENGTH_LONG).show();

        }
            holder.message_text.setText(messageModel.getMessage());


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
