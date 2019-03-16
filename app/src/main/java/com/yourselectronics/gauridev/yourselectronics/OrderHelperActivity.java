package com.yourselectronics.gauridev.yourselectronics;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourselectronics.gauridev.yourselectronics.Model.AddressModel;
import com.yourselectronics.gauridev.yourselectronics.Model.OrderModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OrderViewHolder;

public class OrderHelperActivity extends AppCompatActivity {
    private Fragment fragment;
    private RecyclerView mOrderRCView;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<OrderModel,OrderViewHolder> recyclerAdapter;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_helper);

        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.order_helper_toolbar);
        mToolbar.setTitle("My Orders");
        setSupportActionBar(mToolbar);

        mOrderRCView = findViewById(R.id.order_rc_view);
        mOrderRCView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRef = FirebaseDatabase.getInstance().getReference().child("order_requests");

        mAuth = FirebaseAuth.getInstance();
        String user_uid = mAuth.getCurrentUser().getUid();

        recyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, OrderViewHolder>(
                OrderModel.class,
                R.layout.order_card,
                OrderViewHolder.class,
                mRef.orderByChild("user_id").equalTo(user_uid)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, OrderModel model, int position) {
                AddressModel addressModel = new AddressModel();
                viewHolder.order_id.setText("#" +recyclerAdapter.getRef(position).getKey());
                viewHolder.setItem_img(model.getItem_img());
                viewHolder.setOrder_status(convertCodeToStatus(model.getOrder_status()));
                viewHolder.setItem_name(model.getItem_name());
                viewHolder.setItem_desc(model.getItem_desc());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setPayment_method(model.getPayment_method());


            }
        };
        mOrderRCView.setAdapter(recyclerAdapter);



    }
    private String convertCodeToStatus(String order_status) {
        switch (order_status) {
            case "0":
                return "Placed";
            case "1":
                return "Dispatched";
            default:
                return "Delivered";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
