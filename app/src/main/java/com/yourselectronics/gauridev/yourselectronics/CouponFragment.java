package com.yourselectronics.gauridev.yourselectronics;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.CouponModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.CouponViewHolder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CouponFragment extends Fragment{

    private View view;
    private RecyclerView mRecyclerView;
    private DatabaseReference mRef;
    private EditText mEditCouponCode;
    private CardView mSbmBtn;
    private FirebaseRecyclerAdapter <CouponModel,CouponViewHolder> recyclerAdapter;
    private ProgressBar mProgress;
    private ProgressDialog mProgressBar;
    private Fragment fragment;
    private Toolbar mToolbar;

    public CouponFragment() {
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
        view = inflater.inflate(R.layout.fragment_coupon, container, false);

        mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setMessage("Applying Coupon");
        setupToolBar();



        mEditCouponCode = view.findViewById(R.id.edit_coupon_code);
        mSbmBtn = view.findViewById(R.id.sbm_coupon_code);


        mRef = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = view.findViewById(R.id.coupon_rc_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        recyclerAdapter = new FirebaseRecyclerAdapter<CouponModel, CouponViewHolder>(
                CouponModel.class,
                R.layout.coupon_card_view,
                CouponViewHolder.class,
                mRef.child("coupon_list")

        ) {
            @Override
            protected void populateViewHolder(final CouponViewHolder viewHolder, final CouponModel model, final int position) {
                viewHolder.setCoupon_title(model.getCoupon_title());
                viewHolder.setCoupon_code(model.getCoupon_code());
                viewHolder.setCoupon_desc(model.getCoupon_desc());
                viewHolder.setCoupon_validity(model.getCoupon_validity());



            }
        };



        mRecyclerView.setAdapter(recyclerAdapter);

        mSbmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mEditCouponCode.getText().toString();
                checkCoupon(code);

            }
        });








        return view;
    }

    private void checkCoupon(String code) {
        Query query = FirebaseDatabase.getInstance().getReference().child("coupon_list").orderByChild("coupon_code").equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                         CouponModel couponModel =snapshot.getValue(CouponModel.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("id", couponModel.getCoupon_code());
                        fragment = new DeliveryFragment();
                        fragment.setArguments(bundle);

                       if (getActivity() != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.orderFrame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    }





                }
                else {
                    Toast.makeText(getContext(),"Coupon Not Found",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setupToolBar() {

        if (getActivity() != null) {
            mToolbar = view.findViewById(R.id.coupon_toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

            //getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }


}
