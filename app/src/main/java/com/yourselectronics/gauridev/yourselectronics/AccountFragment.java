package com.yourselectronics.gauridev.yourselectronics;


import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.AddressModel;
import com.yourselectronics.gauridev.yourselectronics.Model.OrderModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.AddressViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OrderViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private TextView mProfile_Name,mProfile_Mobile,mProfile_Email, mtext_view_all_add;
    private RecyclerView mRecyclerView, mOrderRCView;
    private Fragment addFragment;
    private CardView mAddNewAdd, view_all_add, mViewAllBtn;
    private View view;
    private LinearLayout RootRL;
    private ProgressBar mProgress;
    private String current_user;
    private DatabaseReference mRootRef;
    private  FirebaseRecyclerAdapter<OrderModel, OrderViewHolder> recyclerAdapter;
    private Fragment fragment;




    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view!=null){
            if((ViewGroup)view.getParent()!=null)
                ((ViewGroup)view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_account, container, false);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();

        RootRL = view.findViewById(R.id.account_rel_layout);
        mProgress = view.findViewById(R.id.account_p_bar);
        mProgress.setVisibility(View.VISIBLE);

        mRef = FirebaseDatabase.getInstance().getReference().child("user_database");
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = view.findViewById(R.id.account_add_rc_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);

        mOrderRCView = view.findViewById(R.id.account_order_rc_view);
        mOrderRCView.setLayoutManager(new LinearLayoutManager(getContext()));
        mOrderRCView.setNestedScrollingEnabled(false);
        showAllOrder();

        mViewAllBtn = view.findViewById(R.id.account_view_all_order_btn);
        mViewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity()!=null) {
                    fragment = new OrdersFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_view, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        mtext_view_all_add = view.findViewById(R.id.text_view_all_add);








        mAddNewAdd = view.findViewById(R.id.add_new_add);
        view_all_add = view.findViewById(R.id.view_all_add);
        view_all_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = mtext_view_all_add.getText().toString();
                if (string.equals("View All")){
                    callFullAdd();
                    mtext_view_all_add.setText("View Less");
                }
                else if(string.equals(("View Less"))) {
                    callLessAddress();
                    mtext_view_all_add.setText("View All");

                }

            }
        });

        mAddNewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((getActivity() != null)) {
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("Add New Address");
                    addFragment = new AddressFillUpFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_view, addFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            }
        });


        mProfile_Name = view.findViewById(R.id.profile_name);
        mProfile_Mobile = view.findViewById(R.id.profile_mobile);
        mProfile_Email = view.findViewById(R.id.profile_email);



        mRootRef.child("user_database").child(current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("name").getValue(String.class);
                String user_email = dataSnapshot.child("email").getValue(String.class);
                String user_mobile = dataSnapshot.child("mobile").getValue(String.class);

                mProfile_Name.setText(user_name);
                mProfile_Email.setText(user_email);
                mProfile_Mobile.setText(user_mobile);

                RootRL.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in Loading...", Toast.LENGTH_LONG).show();;

            }
        });
        callLessAddress();


        return view;
    }

    private void showAllOrder() {
         recyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, OrderViewHolder>(
                OrderModel.class,
                R.layout.order_card,
                OrderViewHolder.class,
                mRootRef.child("order_requests").orderByChild("user_uid").equalTo(current_user).limitToLast(2)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, OrderModel model, int position) {
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

    private void callLessAddress() {
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRef.child(user_uid);

        DatabaseReference rc_data = user_db.child("address");

        FirebaseRecyclerAdapter<AddressModel, AddressViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<AddressModel, AddressViewHolder> (
                AddressModel.class,
                R.layout.cart_add_card,
                AddressViewHolder.class,
                rc_data.limitToFirst(2)

        ){
            @Override
            protected void populateViewHolder(AddressViewHolder viewHolder, AddressModel model, int position) {
                viewHolder.setFull_address(model.getFull_address());
                viewHolder.setFirst_name(model.getFirst_name()+" "+model.getLast_name());
                // viewHolder.setLast_name(model.getLast_name());

            }
        };
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    private void callFullAdd() {
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRef.child(user_uid);

        DatabaseReference rc_data = user_db.child("address");

        FirebaseRecyclerAdapter<AddressModel, AddressViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<AddressModel, AddressViewHolder> (
                AddressModel.class,
                R.layout.cart_add_card,
                AddressViewHolder.class,
                rc_data

        ){
            @Override
            protected void populateViewHolder(AddressViewHolder viewHolder, AddressModel model, int position) {
                viewHolder.setFull_address(model.getFull_address());
                viewHolder.setFirst_name(model.getFirst_name()+" "+model.getLast_name());
                // viewHolder.setLast_name(model.getLast_name());

            }
        };
        mRecyclerView.setAdapter(recyclerAdapter);
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
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");
    }
}
