package com.yourselectronics.gauridev.yourselectronics;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.Model.AddressModel;
import com.yourselectronics.gauridev.yourselectronics.Model.CartModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.AddressViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;

    private Button mOrderSbm;
    private Fragment fragment;
    private TextView mTitle, mDesc,product_off_price,product_real_price,product_discount_percentage, item_price,total_item_price;
    private TextView item_discount, item_total_quantity;
    private ImageView mImage;
    private DatabaseReference mRef;
    private Spinner spinner;
    private String id;
    private FirebaseAuth mAuth;
    private RecyclerView addRcView;
    private DatabaseReference ref;
    private RadioButton radCOD, radpayU;
    private ProgressDialog mProgress;
    private android.widget.RelativeLayout mRootLayout;
    private RelativeLayout applyCoupon;
    private TextView appliedCouponCode;
    private Toolbar mToolbar;
    private RecyclerView mAddSelectRCView;

    private TextView mPDTotalPrice;

    private CardView mAddSelectBtn;

    private DatabaseReference mRootRef;

    private FirebaseRecyclerAdapter<AddressModel,AddressViewHolder> recyclerAdapterAdd;





    public DeliveryFragment() {
        // Required empty public constructor
    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        if (getActivity()!=null) {
            final Activity activity = getActivity();
            InstamojoPay instamojoPay = new InstamojoPay();
            IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
            getActivity().registerReceiver(instamojoPay, filter);
            JSONObject pay = new JSONObject();
            try {
                pay.put("email", email);
                pay.put("phone", phone);
                pay.put("purpose", purpose);
                pay.put("amount", amount);
                pay.put("name", buyername);
                pay.put("send_sms", true);
                pay.put("send_email", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initListener();
            instamojoPay.start(activity, pay, listener);
        }
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                mProgress.setMessage("Processing your order");
                mProgress.show();
                requestDataOrder(response);
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String reason) {
               // Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                      //  .show();
            }
        };
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
        view = inflater.inflate(R.layout.fragment_delivery, container, false);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        setupToolBar();

        item_price = view.findViewById(R.id.item_price);
        item_discount = view.findViewById(R.id.item_discount);
        total_item_price = view.findViewById(R.id.total_item_price);
        item_total_quantity = view.findViewById(R.id.item_total_quantity);
        mPDTotalPrice = view.findViewById(R.id.payment_details_total_price);
        mRootLayout = view.findViewById(R.id.delivery_root);
        applyCoupon = view.findViewById(R.id.apply_coupon_layout);
        appliedCouponCode = view.findViewById(R.id.applied_coupon_code);

        mAddSelectBtn = view.findViewById(R.id.change_d_address);



        mProgress = new ProgressDialog(getActivity());


        ref = FirebaseDatabase.getInstance().getReference().child("user_database");



        mRef = FirebaseDatabase.getInstance().getReference().child("products");
        mAuth = FirebaseAuth.getInstance();

        spinner = view.findViewById(R.id.spinner);
        String[] items = new String[]{"1", "2", "3","4","5",};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        mImage = view.findViewById(R.id.d_img);
        mTitle = view.findViewById(R.id.d_title);
        mDesc = view.findViewById(R.id.d_desc);

        product_off_price = view.findViewById(R.id.product_off_price);
        product_real_price = view.findViewById(R.id.product_real_price);
        product_real_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        product_discount_percentage = view.findViewById(R.id.product_discount_percentage);

        mAddSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddress();
            }
        });


        mAddSelectRCView = view.findViewById(R.id.d_add_rc_view);
        mAddSelectRCView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAddSelectRCView.setNestedScrollingEnabled(false);
        getLastSelectedAddress();


        Bundle bundle = this.getArguments();
        if (bundle != null) {

            String code = getArguments().getString("id");

            String name = getColoredSpanned(code, "#800000");

            appliedCouponCode.setText(Html.fromHtml(name+" "+"Applied Successfully"));

        }


        if (getActivity()!=null) {
            if (getActivity().getIntent() != null) {
                id = getActivity().getIntent().getStringExtra("product_id");
                //  Toast.makeText(getContext(),id,)
                if(!id.isEmpty()){
                    showItemDetails(id);
                }

            }
        }


        mOrderSbm = view.findViewById(R.id.place_ord_btn);
        radCOD = view.findViewById(R.id.COD);
        radpayU = view.findViewById(R.id.payU);

        mOrderSbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showPaymentMethodDialog();

              /*  mProgress.setTitle("Please Wait...");
                mProgress.setMessage("Processing your order");
                mProgress.show();
                */


               // placeOrder();

            }
        });
        applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new CouponFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.orderFrame,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    private void requestDataOrder(String response) {

        final String user_uid = mAuth.getCurrentUser().getUid();


        final String i_quantity = spinner.getSelectedItem().toString();
        String response_array[] = response.split(":");

        final String payment_status = response_array[0].substring(response_array[0].indexOf("=")+1);
        final String payment_order_id = response_array[1].substring(response_array[1].indexOf("=")+1);
        final String payment_txnId = response_array[2].substring(response_array[2].indexOf("=")+1);
        final String paymentId = response_array[3].substring(response_array[3].indexOf("=")+1);


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        final String format = simpleDateFormat.format(date);

        mRootRef.child("user_database").child(user_uid).child("address").orderByChild("last_selected_address").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final AddressModel addressModel = snapshot.getValue(AddressModel.class);

                    mRootRef.child("products").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            ProductModel productModel = dataSnapshot.getValue(ProductModel.class);

                                Map cart_data = new HashMap<>();

                                final int randomID = new Random().nextInt(9999 - 1) + 1;

                                cart_data.put("item_name", productModel.getProduct_title());
                                cart_data.put("item_desc", productModel.getProduct_desc());
                                cart_data.put("item_img", productModel.getProduct_img());
                                cart_data.put("item_quantity", i_quantity);
                                cart_data.put("order_status", "0");
                                cart_data.put("item_off_price", String.valueOf(Integer.valueOf(productModel.getProduct_off_price())));
                                cart_data.put("item_real_price",String.valueOf(Integer.valueOf(productModel.getProduct_real_price())));
                                cart_data.put("user_uid", user_uid);
                                cart_data.put("user_name", addressModel.getFirst_name() + addressModel.getLast_name());
                                cart_data.put("user_mobile", addressModel.getMobile_no());
                                cart_data.put("order_shipping_address", addressModel.getFull_address());
                                cart_data.put("pin_code", addressModel.getPin_code());
                                cart_data.put("state", addressModel.getState_name());
                                cart_data.put("order_date",format);
                                cart_data.put("total_price",String.valueOf((Integer.valueOf(productModel.getProduct_off_price())*Integer.valueOf(i_quantity))));
                                cart_data.put("item_delivery_price",productModel.getProduct_delivery_price());
                                cart_data.put("item_single_price",productModel.getProduct_off_price());
                                cart_data.put("item_category_id",productModel.getProduct_category_id());
                                cart_data.put("payment_method","Online Payment");
                                cart_data.put("payment_status",payment_status);
                                cart_data.put("payment_order_id",payment_order_id);
                                cart_data.put("payment_txnId",payment_txnId);
                                cart_data.put("paymentId",paymentId);

                                final String order_id = System.currentTimeMillis() + String.valueOf(randomID);

                                DatabaseReference orderRef = mRootRef.child("order_requests").child(user_uid).child(order_id);
                                orderRef.updateChildren(cart_data, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        mProgress.dismiss();

                                        if (databaseError == null) {
                                            mProgress.dismiss();

                                            Bundle bundle = new Bundle();

                                            bundle.putString("id", order_id);
                                            bundle.putString("product_id", id);
                                            fragment = new FinalOrderFragment();
                                            fragment.setArguments(bundle);

                                            if (getActivity() != null) {
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.orderFrame, fragment);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                            }

                                        }
                                        else{
                                            Toast.makeText(getContext(),"Order Failed",Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void showPaymentMethodDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.payment_method_dialog, null);

        final RadioButton radCOD = alertLayout.findViewById(R.id.COD);
        final RadioButton radpayU = alertLayout.findViewById(R.id.payU);

        Button select = alertLayout.findViewById(R.id.select_btn);
        Button cancel = alertLayout.findViewById(R.id.cancel_btn);
        //   final EditText editMessage = alertLayout.findViewById(R.id.payment_method_dialog);
        final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());


        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        final android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!radpayU.isChecked() && !radCOD.isChecked()){
                    Toast.makeText(getActivity(),"Please Select Payment Method...",Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }
                else
                if(radCOD.isChecked()){
                    dialog.dismiss();
                    mProgress.setTitle("Please Wait...");
                    mProgress.setMessage("Processing your order");
                    mProgress.show();
                    placeOrder("COD");

                }
                else
                if (radpayU.isChecked()){
                    callOnlinePayment();
                    mProgress.dismiss();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void callOnlinePayment() {

        final String current_uid = mAuth.getCurrentUser().getUid();
        mRootRef.child("user_database").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String email = dataSnapshot.child("email").getValue(String.class);
                final String phone = dataSnapshot.child("mobile").getValue(String.class);
                final String name = dataSnapshot.child("name").getValue(String.class);

                mRootRef.child("products").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String item_quantity = spinner.getSelectedItem().toString();
                        Toast.makeText(getActivity(),item_quantity,Toast.LENGTH_LONG).show();
                        ProductModel productModel = dataSnapshot.getValue(ProductModel.class);

                        int total = Integer.valueOf(productModel.getProduct_off_price())*Integer.valueOf(item_quantity);
                        if (total>200){
                            callInstamojoPay(email, phone, String.valueOf(total), "Yours Electronics Payments", name);

                        }
                        else {
                            callInstamojoPay(email, phone, String.valueOf(total+20), "Yours Electronics Payments", name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void placeOrder(final String paymentMethod) {

        final String user_uid = mAuth.getCurrentUser().getUid();
        final String i_quantity = spinner.getSelectedItem().toString();



        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        final String format = simpleDateFormat.format(date);

        mRootRef.child("user_database").child(user_uid).child("address").orderByChild("last_selected_address").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final AddressModel addressModel = snapshot.getValue(AddressModel.class);

                    mRootRef.child("products").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                ProductModel productModel = dataSnapshot.getValue(ProductModel.class);
                                Map cart_data = new HashMap<>();

                                final int randomID = new Random().nextInt(9999 - 1) + 1;

                                cart_data.put("item_name", productModel.getProduct_title());
                                cart_data.put("item_desc", productModel.getProduct_desc());
                                cart_data.put("item_img", productModel.getProduct_img());
                                cart_data.put("item_quantity", i_quantity);
                                cart_data.put("order_status", "0");
                                cart_data.put("item_off_price", String.valueOf(Integer.valueOf(productModel.getProduct_off_price())*Integer.valueOf(i_quantity)));
                                cart_data.put("item_real_price",String.valueOf(Integer.valueOf(productModel.getProduct_real_price())*Integer.valueOf(i_quantity)));
                                cart_data.put("user_uid", user_uid);
                                cart_data.put("user_name", addressModel.getFirst_name() + addressModel.getLast_name());
                                cart_data.put("user_mobile", addressModel.getMobile_no());
                                cart_data.put("order_shipping_address", addressModel.getFull_address());
                                cart_data.put("pin_code", addressModel.getPin_code());
                                cart_data.put("state", addressModel.getState_name());
                                cart_data.put("order_date",format);
                                int total = Integer.valueOf(productModel.getProduct_off_price())*Integer.valueOf(i_quantity);
                                if (total>=200){
                                    cart_data.put("total_price",String.valueOf((Integer.valueOf(productModel.getProduct_off_price())*Integer.valueOf(i_quantity))));
                                }
                                else {
                                    cart_data.put("total_price",String.valueOf((Integer.valueOf(productModel.getProduct_off_price())*Integer.valueOf(i_quantity)+20)));

                                }
                                cart_data.put("item_delivery_price",productModel.getProduct_delivery_price());
                                cart_data.put("item_single_price",productModel.getProduct_off_price());
                                cart_data.put("item_category_id",productModel.getProduct_category_id());
                                cart_data.put("payment_method",paymentMethod);

                                final String order_id = System.currentTimeMillis() + String.valueOf(randomID);

                                DatabaseReference orderRef = mRootRef.child("order_requests").child(user_uid).child(order_id);
                                orderRef.updateChildren(cart_data, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        mProgress.dismiss();

                                        if (databaseError == null) {
                                            mProgress.dismiss();

                                            Bundle bundle = new Bundle();

                                            bundle.putString("id", order_id);
                                            bundle.putString("product_id", id);
                                            fragment = new FinalOrderFragment();
                                            fragment.setArguments(bundle);

                                            if (getActivity() != null) {
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.orderFrame, fragment);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                            }

                                        }
                                        else{
                                            Toast.makeText(getContext(),"Order Failed",Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setupToolBar() {
        if (getActivity() != null) {
            mToolbar = view.findViewById(R.id.delivery_toolbar);
            mToolbar.setTitle("Delivery");
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void selectAddress() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.add_select_layout, null);

        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getContext());
        mAddSelectRCView = alertLayout.findViewById(R.id.add_select_rc_view);
        mAddSelectRCView.setLayoutManager(new LinearLayoutManager(getContext()));

        // this is set the view from XML inside AlertDialog

        alert.setView(alertLayout);
        android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();
        getAllAddress(mAddSelectRCView,dialog);


    }



    private void getAllAddress(RecyclerView mAddSelectRCView, final android.support.v7.app.AlertDialog dialog) {
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRootRef.child("user_database").child(user_uid);
        final DatabaseReference add_ref = user_db.child("address");

        recyclerAdapterAdd = new FirebaseRecyclerAdapter<AddressModel, AddressViewHolder>(
                AddressModel.class,
                R.layout.card_address,
                AddressViewHolder.class,
                add_ref
        ) {
            @Override
            protected void populateViewHolder(final AddressViewHolder viewHolder, final AddressModel model, int position) {
                viewHolder.setFirst_name(model.getFirst_name());
                viewHolder.setFull_address(model.getFull_address());
                viewHolder.mSelectBtn.setChecked(model.isLast_selected_address());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    snapshot.child("last_selected_address").getRef().setValue(false);
                                }
                                add_ref.child(recyclerAdapterAdd.getRef(viewHolder.getAdapterPosition()).getKey()).child("last_selected_address").setValue(true);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };
        mAddSelectRCView.setAdapter(recyclerAdapterAdd);
    }

    private void getLastSelectedAddress() {
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRootRef.child("user_database").child(user_uid);
        final DatabaseReference add_ref = user_db.child("address");

        recyclerAdapterAdd = new FirebaseRecyclerAdapter<AddressModel, AddressViewHolder>(
                AddressModel.class,
                R.layout.cart_add_card,
                AddressViewHolder.class,
                add_ref.orderByChild("last_selected_address").equalTo(true)

        ) {
            @Override
            protected void populateViewHolder(final AddressViewHolder viewHolder, final AddressModel model, int position) {
                viewHolder.setFirst_name(model.getFirst_name());
                viewHolder.setFull_address(model.getFull_address());

            }
        };
        mAddSelectRCView.setAdapter(recyclerAdapterAdd);

    }



    private void showItemDetails(String id) {
        Query query = mRef.child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            ProductModel productModel = dataSnapshot.getValue(ProductModel.class);
                            item_price.setText("Rs. " + productModel.getProduct_real_price());
                            total_item_price.setText("Rs. " + productModel.getProduct_off_price());
                            mTitle.setText(productModel.getProduct_title());
                            mDesc.setText(productModel.getProduct_desc());
                            product_off_price.setText("Rs. " + productModel.getProduct_off_price());
                            product_real_price.setText("Rs. " + productModel.getProduct_real_price());
                            product_discount_percentage.setText(productModel.getProduct_discount_percenatge()+"% off");
                            Picasso.get().load(productModel.getProduct_img()).fit().into(mImage);

                            mPDTotalPrice.setText(productModel.getProduct_off_price());

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final String s = (String) adapterView.getItemAtPosition(i);
        mRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String off_price = dataSnapshot.child("product_off_price").getValue(String.class);
                String real_price = dataSnapshot.child("product_real_price").getValue(String.class);

                int total = Integer.valueOf(off_price)*Integer.valueOf(s);
                int total_real_price = Integer.valueOf(real_price)*Integer.valueOf(s);
                int discount = (Integer.valueOf(real_price)-Integer.valueOf(off_price))*(Integer.valueOf(s));
                total_item_price.setText("Rs. "+String.valueOf(total));
                item_price.setText("Rs. "+ String.valueOf(total_real_price));
                item_discount.setText("- Rs. "+String.valueOf(discount));
                mPDTotalPrice.setText("Rs. "+String.valueOf(total));
                item_total_quantity.setText(s);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}
