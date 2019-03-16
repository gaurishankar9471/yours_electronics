package com.yourselectronics.gauridev.yourselectronics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductFullImageActivity extends AppCompatActivity {
    private ViewPager mPager;
    private ArrayList<String> img = new ArrayList<>() ;
    private DatabaseReference mRootRef;
    private String product_id;
    private TabLayout tab;
    private CardView closeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_full_image);

        if (getIntent() != null) {
            product_id = getIntent().getStringExtra("id");
        }

        closeActivity=findViewById(R.id.single_image_card_close_icon);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPager= findViewById(R.id.single_image_viewpager);
        tab= findViewById(R.id.full_img_tab_layout);



        final ProductFullImageSlider sliderAdapter = new ProductFullImageSlider(this,img);
        mPager.setAdapter(sliderAdapter);
        tab.setupWithViewPager(mPager);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child("products").child(product_id).child("img_slider").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                img.add(dataSnapshot.getValue(String.class));
                sliderAdapter.notifyDataSetChanged();

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



}
