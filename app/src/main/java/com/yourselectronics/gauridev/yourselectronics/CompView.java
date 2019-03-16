package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourselectronics.gauridev.yourselectronics.Model.OfferModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OfferViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompView extends Fragment {
    private View view;
    private RecyclerView mCompRCView;
    private DatabaseReference mRootRef;
    private FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> recyclerAdapter;


    public CompView() {
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
        view = inflater.inflate(R.layout.fragment_comp_view, container, false);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mCompRCView = view.findViewById(R.id.comp_view_rc_view);
        mCompRCView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mCompRCView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        showCompRcView();


        return view;
    }

    private void showCompRcView() {
        DatabaseReference ref = mRootRef.child("offers_section/dod");
        FirebaseRecyclerAdapter<OfferModel, OfferViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<OfferModel, OfferViewHolder>(
                OfferModel.class,
                R.layout.offer_card_view,
                OfferViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(final OfferViewHolder viewHolder, final OfferModel model, int position) {
                viewHolder.setProduct_img(model.getProduct_img());
                viewHolder.setProduct_title(model.getProduct_title());
                viewHolder.setProduct_desc(model.getProduct_desc());
                viewHolder.setProduct_off_price("Rs."+" "+model.getProduct_off_price());
                viewHolder.setProduct_real_price("Rs."+" "+model.getProduct_real_price());
                viewHolder.setProduct_off_percentage(model.getProduct_off_percentage());
                viewHolder.setProduct_deal_timeout(model.getProduct_deal_timeout());



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
        mCompRCView.setAdapter(recyclerAdapter);
    }

}
