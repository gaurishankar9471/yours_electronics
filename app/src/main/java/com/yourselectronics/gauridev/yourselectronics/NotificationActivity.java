package com.yourselectronics.gauridev.yourselectronics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourselectronics.gauridev.yourselectronics.Model.NotificationModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.NotificationViewHolder;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView mNotificationRCView;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private String current_uid;
    private FirebaseRecyclerAdapter<NotificationModel, NotificationViewHolder> recyclerAdapter;
    private Toolbar mToolbar;
    private ImageView mCloseTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.notification_toolbar);
        mToolbar.setTitle("Notification");

        mCloseTB = findViewById(R.id.close_toolbar_icon);
        mCloseTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("notification");
        current_uid = mAuth.getCurrentUser().getUid();

        DatabaseReference reference = ref.child(current_uid);

        mNotificationRCView = findViewById(R.id.notification_rc_view);
        mNotificationRCView.setLayoutManager(new LinearLayoutManager(this));


         recyclerAdapter = new FirebaseRecyclerAdapter<NotificationModel, NotificationViewHolder>(
                NotificationModel.class,
                R.layout.notification_card_view,
                NotificationViewHolder.class,
                reference

        ) {
            @Override
            protected void populateViewHolder(final NotificationViewHolder viewHolder, NotificationModel model, int position) {
                viewHolder.setNotification_title(model.getNotification_title());
                viewHolder.setNotification_desc(model.getNotification_desc());
                viewHolder.setNotification_img(model.getNotification_img());
                viewHolder.setNotification_time(model.getNotification_time());
                viewHolder.close_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();

                    }
                });
            }
        };
         mNotificationRCView.setAdapter(recyclerAdapter);

    }
}
