package com.yourselectronics.gauridev.yourselectronics;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopOffersFragment extends Fragment {
    private View view;

    private ViewPager viewPager, fragmentViewPager;
    private TabLayout indicator, tab_fragment;
    private ArrayList<String> banner_img;
    private ArrayList<String> product_id;

    private ArrayList<String> mKeysBanner = new ArrayList<>();
    private ArrayList<String> mKeysProduct = new ArrayList<>();

    private DatabaseReference mRootRef;


    public TopOffersFragment() {
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

        view = inflater.inflate(R.layout.fragment_top_offers, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        indicator = (TabLayout) view.findViewById(R.id.indicator);
        mRootRef = FirebaseDatabase.getInstance().getReference();


        banner_img = new ArrayList<>();
        product_id = new ArrayList<>() ;

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(),banner_img,product_id);
        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager,true);
        setUpSlider(sliderAdapter);



        fragmentViewPager = view.findViewById(R.id.top_offer_view_pager);
        setupViewPager(fragmentViewPager);

        tab_fragment = view.findViewById(R.id.top_offers_tabs);
        tab_fragment.setupWithViewPager(fragmentViewPager);



        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        if (getActivity()!=null) {
            TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager());
            adapter.addFragment(new CompView(), "Electronic Components");
            adapter.addFragment(new Robotics(), "Robotics");
            adapter.addFragment(new CircuitBoard(), "Circuit Board");
            viewPager.setAdapter(adapter);
        }
    }


    private void setUpSlider(final SliderAdapter sliderAdapter) {
        DatabaseReference ref = mRootRef.child("banners/top_offers/slider_top");
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
