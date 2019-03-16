package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.AddressModel;
import com.yourselectronics.gauridev.yourselectronics.Model.OrderModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.AddressViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OrderListViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OrderViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {
    private RecyclerView mOrderRCView;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<OrderModel,OrderListViewHolder> recyclerAdapter;
    private FirebaseAuth mAuth;
    private View view;
    private Fragment fragment;
    private DatabaseReference mRootRef;
    FrameLayout frameLayout;


    public OrdersFragment() {
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

        view = inflater.inflate(R.layout.fragment_orders, container, false);
        frameLayout = getActivity().findViewById(R.id.main_view);

        mRootRef = FirebaseDatabase.getInstance().getReference();


        mAuth = FirebaseAuth.getInstance();
        String user_uid = mAuth.getCurrentUser().getUid();


        mOrderRCView = view.findViewById(R.id.order_rc_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mOrderRCView.setLayoutManager(linearLayoutManager);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        mOrderRCView.setLayoutAnimation(animation);



        mRef = FirebaseDatabase.getInstance().getReference().child("order_requests").child(user_uid);


        recyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, OrderListViewHolder>(
                OrderModel.class,
                R.layout.order_list_card_card_view,
                OrderListViewHolder.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(final OrderListViewHolder viewHolder, OrderModel model, int position) {
                viewHolder.setItem_name(model.getItem_name());
                viewHolder.setItem_desc(model.getItem_desc());
                viewHolder.setItem_img(model.getItem_img());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (getActivity()!=null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", recyclerAdapter.getRef(viewHolder.getAdapterPosition()).getKey());
                            fragment = new OrderDetailFragment();
                            fragment.setArguments(bundle);

                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_view, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                });
            }
        };


        mOrderRCView.setAdapter(recyclerAdapter);

       showEmptyLayout(frameLayout);




        return view;
    }

    private void showEmptyLayout(final FrameLayout frameLayout) {
        String user_uid = mAuth.getCurrentUser().getUid();
        Query ref = mRootRef.child("order_requests").orderByChild("user_uid").equalTo(user_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0){
                    if (getActivity()!=null) {

                        LayoutInflater inflater = LayoutInflater.from(getActivity());

                        View viewRoot = inflater.inflate(R.layout.empty_order_layout, frameLayout, false);
                        frameLayout.addView(viewRoot);

                        CardView shopNow = viewRoot.findViewById(R.id.empty_order_shop_now_btn);
                        shopNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);
                            }
                        });

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Orders");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        frameLayout.removeAllViews();
    }
}
