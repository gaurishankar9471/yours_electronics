package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.Model.CatModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.CatViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DatabaseReference categoryRef;
    private FirebaseRecyclerAdapter<CatModel,CatViewHolder> recyclerAdapter;
    private View view;
    private ImageView mBanner200dp, mBanner150dp;
    private DatabaseReference mRootRef;


    public CatFragment() {
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
        view = inflater.inflate(R.layout.fragment_cat, container, false);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mBanner200dp = view.findViewById(R.id.cat_banner_200dp);
        mBanner150dp = view.findViewById(R.id.cat_banner_150dp);
        loadBanners();



        mRecyclerView = view.findViewById(R.id.cat_rc_view);
        categoryRef = FirebaseDatabase.getInstance().getReference().child("category");
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
        mRecyclerView.setNestedScrollingEnabled(false);

        recyclerAdapter = new FirebaseRecyclerAdapter<CatModel, CatViewHolder>(
                CatModel.class,
                R.layout.cat_card,
                CatViewHolder.class,
                categoryRef
        ) {
            @Override
            protected void populateViewHolder(CatViewHolder viewHolder, CatModel model, final int position) {
                viewHolder.setCat_img(model.getCat_img());
                viewHolder.setCat_name(model.getCat_name());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("cat_id",recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        mRecyclerView.setAdapter(recyclerAdapter);



        return view;
    }

    private void loadBanners() {
        DatabaseReference ref = mRootRef.child("banners/category");
        ref.child("banner_01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img = dataSnapshot.child("img_url").getValue(String.class);
                final String p_id = dataSnapshot.child("product_id").getValue(String.class);
                Picasso.get().load(img).fit().into(mBanner200dp);
                mBanner200dp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("id", p_id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.child("banner_02").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img = dataSnapshot.child("img_url").getValue(String.class);
                final String p_id = dataSnapshot.child("product_id").getValue(String.class);
                Picasso.get().load(img).fit().into(mBanner150dp);
                mBanner200dp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("id", p_id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Categories");


    }
}
