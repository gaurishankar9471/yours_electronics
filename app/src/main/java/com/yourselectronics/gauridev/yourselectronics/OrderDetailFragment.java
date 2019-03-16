package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
public class OrderDetailFragment extends Fragment {
    private View view;
    private String order_id;
    private TextView mOrderId, mOrderPD, mOrderTP, mOrderItemName, mOrderItemDesc, mOrderItemPrice, mOrderSipADD, mOrderMobileNo;
    private TextView mItemRP,mItemFP, mItemDP, mItemDisP;
    private DatabaseReference mRef;
    private ImageView mItemImage;
    private RecyclerView mODRelatedItemRCView;
    private FirebaseRecyclerAdapter<ProductModel,ProductViewHolder> recyclerAdapter;
    private String user_uid;


    public OrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Order Details");

        if(view!=null){
            if((ViewGroup)view.getParent()!=null)
                ((ViewGroup)view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        user_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
           order_id = getArguments().getString("order_id");

        }

        mRef = FirebaseDatabase.getInstance().getReference();

        mItemImage = view.findViewById(R.id.od_item_img);
        mOrderId = view.findViewById(R.id.od_order_id);
        mOrderPD = view.findViewById(R.id.od_purchase_date);
        mOrderTP = view.findViewById(R.id.od_total_amount);
        mOrderItemName = view.findViewById(R.id.od_item_name);
        mOrderItemDesc = view.findViewById(R.id.od_item_desc);
        mOrderItemPrice = view.findViewById(R.id.od_item_price);
        mOrderSipADD = view.findViewById(R.id.od_shipping_address);
        mOrderMobileNo = view.findViewById(R.id.od_mobile_no);
        mItemFP = view.findViewById(R.id.item_final_price);
        mItemRP = view.findViewById(R.id.item_real_price);
        mItemDP = view.findViewById(R.id.item_delivery_price);
        mItemDisP = view.findViewById(R.id.item_discount_price);

        mODRelatedItemRCView = view.findViewById(R.id.od_related_item_rc_view);
        mODRelatedItemRCView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        mRef.child("order_requests").child(user_uid).child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);



                    mOrderId.setText("#"+order_id);
                    mOrderPD.setText(orderModel.getOrder_date());
                    mOrderTP.setText("Rs. "+orderModel.getTotal_price());
                    mOrderItemName.setText(orderModel.getItem_name());
                    mOrderItemDesc.setText(orderModel.getItem_desc());
                    Picasso.get().load(orderModel.getItem_img()).fit().into(mItemImage);
                    mOrderItemPrice.setText("Rs. "+orderModel.getItem_single_price()+" "+ "(per piece)");
                    mOrderSipADD.setText(orderModel.getOrder_shipping_address());
                    mOrderMobileNo.setText(orderModel.getUser_mobile());
                    mItemRP.setText("Rs."+" "+ String.valueOf(Integer.valueOf(orderModel.getItem_real_price())));
                    mItemDP.setText("Rs."+" "+orderModel.getItem_delivery_price());
                    mItemDisP.setText("Rs."+" "+String.valueOf(Integer.valueOf(orderModel.getItem_real_price())-Integer.valueOf(orderModel.getItem_off_price())));
                    mItemFP.setText("Rs."+" "+orderModel.getTotal_price());

                    showRelatedItem(orderModel.getItem_category_id());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;
    }

    private void showRelatedItem(String item_category_id) {
        DatabaseReference ref = mRef.child("products");
        recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.recommended_card_view,
                ProductViewHolder.class,
                ref.orderByChild("product_category_id").equalTo(item_category_id)
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
                        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                        intent.putExtra("id", recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        mODRelatedItemRCView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Order Details");
    }

}
