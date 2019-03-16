package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodaysDealsFragment extends Fragment {
    private View view;

    private ViewPager viewPager;
    private TabLayout indicator;
    private ArrayList<String> banner_img;
    private ArrayList<String> product_id;

    private ArrayList<String> mKeysBanner = new ArrayList<>();
    private ArrayList<String> mKeysProduct = new ArrayList<>();
    private RecyclerView mTodaysRCView;

    private DatabaseReference mRootRef;
    private FirebaseRecyclerAdapter<ProductModel,ProductViewHolder> recyclerAdapter;

    public TodaysDealsFragment() {
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

       view = inflater.inflate(R.layout.fragment_todays_deals, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        indicator = (TabLayout) view.findViewById(R.id.indicator);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mTodaysRCView = view.findViewById(R.id.todays_deal_rc_view);
        mTodaysRCView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mTodaysRCView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        mTodaysRCView.setNestedScrollingEnabled(false);
        showTodaysRCView();



        banner_img = new ArrayList<>();
        product_id = new ArrayList<>() ;

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(),banner_img,product_id);
        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager,true);
        setUpSlider(sliderAdapter);


        return view;
    }

    private void showTodaysRCView() {
        DatabaseReference ref = mRootRef.child("products");
        recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.product_view_card,
                ProductViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, ProductModel model, final int position) {
                viewHolder.setProduct_img(model.getProduct_img());
                viewHolder.setProduct_title(model.getProduct_title());
                viewHolder.setProduct_desc(model.getProduct_desc());
                viewHolder.setProduct_off_price(model.getProduct_off_price());
                viewHolder.setProduct_real_price(model.getProduct_real_price());
                viewHolder.setProduct_discount_percentage(model.getProduct_discount_percenatge());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("id",recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        mTodaysRCView.setAdapter(recyclerAdapter);
    }

    private void setUpSlider(final SliderAdapter sliderAdapter) {
        DatabaseReference ref = mRootRef.child("banners/todays_deals/slider_top");
        ref.child("img_url").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                banner_img.add(dataSnapshot.getValue(String.class));
                mKeysBanner.add(dataSnapshot.getKey());
                sliderAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                int index = mKeysBanner.indexOf(dataSnapshot.getKey());
                banner_img.set(index,dataSnapshot.getValue(String.class));
                sliderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                banner_img.remove(dataSnapshot.getValue(String.class));
                sliderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Empty",Toast.LENGTH_LONG).show();

            }
        });
        ref.child("product_id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                product_id.add(dataSnapshot.getValue(String.class));
                mKeysProduct.add(dataSnapshot.getKey());
                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int index = mKeysProduct.indexOf(dataSnapshot.getKey());
                product_id.set(index,dataSnapshot.getValue(String.class));
                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                product_id.remove(dataSnapshot.getValue(String.class));

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
