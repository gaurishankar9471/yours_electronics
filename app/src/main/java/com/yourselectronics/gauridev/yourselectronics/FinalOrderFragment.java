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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.server.converter.StringToIntConverterEntryCreator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.Model.OrderModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinalOrderFragment extends Fragment {

    private View view;
    private DatabaseReference mRootRef;
    private TextView final_order_id, d_title,d_desc,product_off_price,product_real_price,product_discount_percentage;
    private RecyclerView RecRCView, RelRCView;
    private FirebaseRecyclerAdapter<ProductModel,ProductViewHolder> recyclerAdapter;
    private CardView mShopMoreBtn;

    private String current_uid;
    private   String order_id;
    private String product_id;
    private ImageView d_img;

    public FinalOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_final_order, container, false);

        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final_order_id = view.findViewById(R.id.order_id);
        d_title = view.findViewById(R.id.d_title);
        d_desc = view.findViewById(R.id.d_desc);
        d_img = view.findViewById(R.id.d_img);
        product_off_price = view.findViewById(R.id.product_off_price);
        product_real_price = view.findViewById(R.id.product_real_price);
        product_discount_percentage = view.findViewById(R.id.product_discount_percentage);
        RecRCView = view.findViewById(R.id.final_rec_item);
        RelRCView = view.findViewById(R.id.final_rel_item);
        mShopMoreBtn = view.findViewById(R.id.shop_more_btn);




        mRootRef = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            order_id = getArguments().getString("id");
            product_id = getArguments().getString("product_id");
            showProductDetails(order_id);

        }

        RecRCView.setLayoutManager(new GridLayoutManager(getContext(),2));
        RecRCView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        RecRCView.setNestedScrollingEnabled(false);

        RelRCView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        showRecItem();

        mShopMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

            return view;
    }

    private void showRelItem(String product_category_id) {
        DatabaseReference ref = mRootRef.child("products");
        recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.recommended_card_view,
                ProductViewHolder.class,
                ref.orderByChild("product_category_id").equalTo(product_category_id)
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
        RelRCView.setAdapter(recyclerAdapter);

    }

    private void showRecItem() {
        DatabaseReference ref = mRootRef.child("products");
       recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.product_view_card,
                ProductViewHolder.class,
                ref.limitToLast(10)
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
       RecRCView.setAdapter(recyclerAdapter);
    }

    private void showProductDetails(final String order_id) {
        DatabaseReference ref = mRootRef.child("products").child(product_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                   ProductModel productModel = dataSnapshot.getValue(ProductModel.class);
                   d_title.setText(productModel.getProduct_title());
                   d_title.setText(productModel.getProduct_desc());
                   product_off_price.setText("Rs. "+productModel.getProduct_off_price());
                   product_real_price.setText("Rs. "+productModel.getProduct_real_price());
                   product_discount_percentage.setText(productModel.getProduct_discount_percenatge()+"% off");

                    Picasso.get().load(productModel.getProduct_img()).fit().into(d_img);
                    showRelItem(productModel.getProduct_category_id());




                }
                catch (Exception e){
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
