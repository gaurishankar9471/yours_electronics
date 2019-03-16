package com.yourselectronics.gauridev.yourselectronics;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ContactUsActivity extends AppCompatActivity {

    private CardView mCallBtn, mEmailBtn, mSendMessageBtn, mLiveChatBtn;
    private FirebaseUser user;

    private DatabaseReference mRootRef;
    private String current_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

         user = FirebaseAuth.getInstance().getCurrentUser();


         mRootRef = FirebaseDatabase.getInstance().getReference();

        mCallBtn = findViewById(R.id.call_us_btn);
        mEmailBtn = findViewById(R.id.email_us_btn);
        mSendMessageBtn = findViewById(R.id.send_message_btn);
        mLiveChatBtn = findViewById(R.id.live_chat_btn);

        mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialer();
            }
        });
        mEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user!=null) {
                    showMessageDialog();
                }
                else {
                    finish();
                    startActivity(getIntent());
                    Intent intent = new Intent(ContactUsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),"Please Log In to send message...",Toast.LENGTH_LONG).show();
                }
            }
        });
        mLiveChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLiveChat();
            }
        });

    }

    private void openLiveChat() {
        if (user!=null) {
            Intent intent = new Intent(ContactUsActivity.this, ChatHomeActivity.class);
            startActivity(intent);
        }
        else {
            finish();
            startActivity(getIntent());
            Intent intent = new Intent(ContactUsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }



    private void sendEmail() {
        String [] TO = {"contact@yourselectronics.com"};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL,TO);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Enter your Subject...");
        intent.putExtra(Intent.EXTRA_TEXT,"Enter Email...");

        try{
            startActivity(Intent.createChooser(intent,"Send Email Using..."));
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void openDialer() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + "+917001068589"));
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

        }
    }
    private void showMessageDialog(){
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.send_message_dialog, null);
        Button send = alertLayout.findViewById(R.id.send_msg_btn);
        Button cancel = alertLayout.findViewById(R.id.cancel_msg_btn);
        final EditText editMessage = alertLayout.findViewById(R.id.edit_personal_msg);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();


                DatabaseReference ref = mRootRef.child("user_message").child(current_uid).push();
                ref.setValue(message);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    

}
