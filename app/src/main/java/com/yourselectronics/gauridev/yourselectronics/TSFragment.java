package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourselectronics.gauridev.yourselectronics.Model.OfferModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.Model.SmallCardModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OfferViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.SmallCardViewHolder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TSFragment extends Fragment {

    private View view;

    private RecyclerView mRC_View;
    private ViewPager viewPager;
    private TabLayout indicator;
    private ArrayList<String> banner_img;
    private ArrayList<String> product_id;

    private ArrayList<String> mKeysBanner = new ArrayList<>();
    private ArrayList<String> mKeysProduct = new ArrayList<>();

    private DatabaseReference mRootRef;


    public TSFragment() {
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
        view =inflater.inflate(R.layout.fragment_t, container, false);


        mRC_View = view.findViewById(R.id.frag_dod_rc_view);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        indicator = (TabLayout) view.findViewById(R.id.indicator);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        banner_img = new ArrayList<>();
        product_id = new ArrayList<>() ;

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(),banner_img,product_id);
        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager,true);
        setUpSlider(sliderAdapter);


        mRC_View.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRC_View.addItemDecoration(new GridSpacingItemDecoration(3,20,true));
        mRC_View.setNestedScrollingEnabled(false);

        DatabaseReference ref = mRootRef.child("offers_section/ts");
        FirebaseRecyclerAdapter<SmallCardModel,SmallCardViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<SmallCardModel, SmallCardViewHolder>(
                SmallCardModel.class,
                R.layout.small_card_view,
                SmallCardViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(SmallCardViewHolder viewHolder, final SmallCardModel model, int position) {
                viewHolder.setImg(model.getImg());
                viewHolder.setPrice("Rs. "+model.getPrice());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),ProductDetailActivity.class);
                        intent.putExtra("id",model.getProduct_id());
                        startActivity(intent);
                    }
                });
            }
        };
        mRC_View.setAdapter(recyclerAdapter);





        return view;
    }

    private void setUpSlider(final SliderAdapter sliderAdapter) {
        DatabaseReference ref = mRootRef.child("banners/top_selling/slider_top");
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Top Selling");
    }

}
