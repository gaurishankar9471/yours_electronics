package com.yourselectronics.gauridev.yourselectronics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.AddressModel;
import com.yourselectronics.gauridev.yourselectronics.Model.CartModel;
import com.yourselectronics.gauridev.yourselectronics.Model.CatModel;
import com.yourselectronics.gauridev.yourselectronics.Model.CouponModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.AddressViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.CartViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class ShoppingCartActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private RecyclerView mRecycler, mRecRCView, mAddSelectRCView;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<CartModel,CartViewHolder> recyclerAdapter;
    private FirebaseRecyclerAdapter<ProductModel,ProductViewHolder> recyclerAdapterProduct;
    private RelativeLayout mRootLayout;
    private TextView mTotalPrice;
    private Button mPlaceOrder;
    private DatabaseReference mRootRef;
    private Boolean b = true;
    private String current_uid;
    private ProgressDialog mProgress;
    private CardView mAddSelectBtn, mGoToHomeBtn;
    private FirebaseRecyclerAdapter<AddressModel,AddressViewHolder> recyclerAdapterAdd;
    private TextView mPDTotalPrice, mPDItemPrice, mPDDiscount;


    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
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

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                mProgress.setMessage("Processing your order");
                mProgress.show();
                requestDataOrder(response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int code, String reason) {
               Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                       .show();
            }
        };
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
       // setupToolBar();

        mRootLayout = findViewById(R.id.cart_root_layout);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRef = FirebaseDatabase.getInstance().getReference().child("user_database");
        setupToolBar();

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);


        mAuth = FirebaseAuth.getInstance();
        final String user_uid = mAuth.getCurrentUser().getUid();


        DatabaseReference user_db = mRef.child(user_uid);
        final DatabaseReference ref = user_db.child("cart_item");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("order_requests");


        mTotalPrice = findViewById(R.id.total_item_price_cart);
        mPlaceOrder = findViewById(R.id.place_ord_cart_btn);
        mAddSelectBtn = findViewById(R.id.change_d_address);
        mPDTotalPrice=  findViewById(R.id.cart_pd_total_price);
        mPDItemPrice = findViewById(R.id.item_price);
        mPDDiscount = findViewById(R.id.item_discount);



        mAddSelectRCView = findViewById(R.id.add_select_rc_view);
        mAddSelectRCView.setLayoutManager(new LinearLayoutManager(this));
        mAddSelectRCView.setNestedScrollingEnabled(false);
        getLastSelectedAddress();


        mRecRCView = findViewById(R.id.cart_rec_rc_view);
        mRecRCView.setLayoutManager(new GridLayoutManager(this,2));
        mRecRCView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        mRecRCView.setNestedScrollingEnabled(false);
        showRecItem();






        mRecycler = findViewById(R.id.cart_rc_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setNestedScrollingEnabled(false);

        try {

            recyclerAdapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(
                    CartModel.class,
                    R.layout.cart_card_view,
                    CartViewHolder.class,
                    ref
            ) {

                @Override
                protected void populateViewHolder(final CartViewHolder viewHolder, final CartModel model, final int position) {
                    viewHolder.setProduct_img(model.getProduct_img());
                    viewHolder.setProduct_title(model.getProduct_title());
                    viewHolder.setProduct_desc(model.getProduct_desc());
                    viewHolder.setProduct_off_price("Rs."+" "+model.getProduct_off_price());
                    viewHolder.setProduct_real_price("Rs."+" "+model.getProduct_real_price());
                    viewHolder.setProduct_discount_percentage(model.getProduct_discount_percentage()+"% off");
                    viewHolder.setProduct_quantity(model.getProduct_quantity());


                   viewHolder.overflowIcon.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           //creating a popup menu
                           try {

                               //creating a popup menu
                               PopupMenu popup = new PopupMenu(view.getContext(), viewHolder.overflowIcon);
                               
                               //inflating menu from xml resource
                               popup.inflate(R.menu.cart_overflow_icon);
                               //adding click listener
                               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                   @Override
                                   public boolean onMenuItemClick(MenuItem item) {
                                       switch (item.getItemId()) {
                                           case R.id.change_quantity_menu:
                                               //handle menu1 click
                                               final String id = recyclerAdapter.getRef(viewHolder.getAdapterPosition()).getKey();
                                               LayoutInflater inflater = getLayoutInflater();
                                               View alertLayout = inflater.inflate(R.layout.change_item_quantitiy_dialog, null);

                                               final RadioButton one = alertLayout.findViewById(R.id.item_quantity_one);
                                               final RadioButton two = alertLayout.findViewById(R.id.item_quantity_two);
                                               final RadioButton three = alertLayout.findViewById(R.id.item_quantity_three);


                                               android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(ShoppingCartActivity.this);
                                               alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                       Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();

                                                   }
                                               });
                                               alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {

                                                       if (one.isChecked()) {
                                                           Toast.makeText(getApplicationContext(), "Quantity Changed to : 1", Toast.LENGTH_LONG).show();
                                                           ref.child(id).child("product_quantity").setValue(one.getText());


                                                       } else if (two.isChecked()) {
                                                           Toast.makeText(getApplicationContext(), "Quantity Changed to : 2", Toast.LENGTH_LONG).show();
                                                           ref.child(id).child("product_quantity").setValue(two.getText());


                                                       } else if (three.isChecked()) {
                                                           Toast.makeText(getApplicationContext(), "Quantity Changed to : 3", Toast.LENGTH_LONG).show();
                                                           ref.child(id).child("product_quantity").setValue(three.getText());

                                                       }

                                                   }
                                               });
                                               alert.setTitle("Select Quantity");

                                               // this is set the view from XML inside AlertDialog
                                               alert.setView(alertLayout);
                                               android.support.v7.app.AlertDialog dialog = alert.create();
                                               dialog.show();

                                               break;
                                       }
                                       return false;
                                   }
                               });
                               //displaying the popup
                               popup.show();


                           }
                           catch (Exception e){

                           }
                       }

                   });

                    ;

                    viewHolder.mRemoveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Toast.makeText(getApplicationContext(),"Remove Button is Clicked",Toast.LENGTH_LONG).show();

                            try {
                                recyclerAdapter.getRef(viewHolder.getAdapterPosition()).removeValue();
                                recyclerAdapter.notifyItemChanged(position);
                                recyclerAdapter.notifyDataSetChanged();
                                recyclerAdapter.notifyItemRemoved(position);


                            }

                           catch (Exception e){
                               Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                }


            };

        }
        catch (Exception e){
             Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

        }
        getTotalPrice();






        ref.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (dataSnapshot.getChildrenCount()==0){
                      //create a view to inflate the layout_item (the xml with the textView created before)
                      View view = getLayoutInflater().inflate(R.layout.empty_cart_layout, mRootLayout, false);


                      //add the view to the main layout
                      mRootLayout.addView(view);

                      CardView  mGoToHomeBtn = view.findViewById(R.id.cart_go_shop_btn);
                      mGoToHomeBtn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Intent intent = new Intent(ShoppingCartActivity.this,MainActivity.class);
                              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              startActivity(intent);
                          }
                      });

                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });



            mRecycler.setAdapter(recyclerAdapter);

        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentMethodDialog();
                /*

                */
            }

        });

        mAddSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddress();
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
        final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);


        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        final android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!radpayU.isChecked() && !radCOD.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please Select Payment Method...",Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }
                else
                if(radCOD.isChecked()){
                    placeOrderCart("COD");

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

    private void requestDataOrder(String response) {


        String response_array[] = response.split(":");

        final String payment_status = response_array[0].substring(response_array[0].indexOf("=")+1);
        final String payment_order_id = response_array[1].substring(response_array[1].indexOf("=")+1);
        final String payment_txnId = response_array[2].substring(response_array[2].indexOf("=")+1);
        final String paymentId = response_array[3].substring(response_array[3].indexOf("=")+1);

        final String user_uid = mAuth.getCurrentUser().getUid();
        final DatabaseReference user_db = mRef.child(user_uid);
        final DatabaseReference cart_ref = user_db.child("cart_item");

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        final String format = simpleDateFormat.format(date);

        user_db.child("address").orderByChild("last_selected_address").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final AddressModel addressModel = snapshot.getValue(AddressModel.class);

                    Toast.makeText(getBaseContext(), addressModel.getFirst_name(), Toast.LENGTH_LONG).show();
                    cart_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CartModel cartModel = snapshot.getValue(CartModel.class);
                                Map cart_data = new HashMap<>();

                                cart_data.put("item_name", cartModel.getProduct_title());
                                cart_data.put("item_desc", cartModel.getProduct_desc());
                                cart_data.put("item_img", cartModel.getProduct_img());
                                cart_data.put("item_quantity", cartModel.getProduct_quantity());
                                cart_data.put("order_status", "0");
                                cart_data.put("item_off_price", String.valueOf(Integer.valueOf(cartModel.getProduct_off_price())*Integer.valueOf(cartModel.getProduct_quantity())));
                                cart_data.put("item_real_price",String.valueOf(Integer.valueOf(cartModel.getProduct_real_price())*Integer.valueOf(cartModel.getProduct_quantity())));
                                cart_data.put("user_uid", user_uid);
                                cart_data.put("user_name", addressModel.getFirst_name() + addressModel.getLast_name());
                                cart_data.put("user_mobile", addressModel.getMobile_no());
                                cart_data.put("order_shipping_address", addressModel.getFull_address());
                                cart_data.put("pin_code", addressModel.getPin_code());
                                cart_data.put("state", addressModel.getState_name());
                                cart_data.put("order_date",format);
                                cart_data.put("total_price",String.valueOf((Integer.valueOf(cartModel.getProduct_off_price())*Integer.valueOf(cartModel.getProduct_quantity()))+20));
                                cart_data.put("item_delivery_price",cartModel.getProduct_delivery_price());
                                cart_data.put("item_single_price",cartModel.getProduct_off_price());
                                cart_data.put("item_category_id",cartModel.getProduct_category_id());
                                cart_data.put("payment_method","Online Payment");
                                cart_data.put("payment_status",payment_status);
                                cart_data.put("payment_order_id",payment_order_id);
                                cart_data.put("payment_txnId",payment_txnId);
                                cart_data.put("paymentId",paymentId);
                                int randomID = new Random().nextInt(9999 - 1) + 1;

                                DatabaseReference orderRef = mRootRef.child("order_requests").child(user_uid).child(System.currentTimeMillis() + String.valueOf(randomID));
                                orderRef.updateChildren(cart_data, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        mProgress.dismiss();
                                        Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ShoppingCartActivity.this,MainActivity.class);
                                        intent.putExtra("id","openOrder");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        cart_ref.setValue(null);

                                    }
                                });
                            }
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

    private void callOnlinePayment() {
        final String current_uid = mAuth.getCurrentUser().getUid();
        mRootRef.child("user_database").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String email = dataSnapshot.child("email").getValue(String.class);
                final String phone = dataSnapshot.child("mobile").getValue(String.class);
                final String name = dataSnapshot.child("name").getValue(String.class);

                DatabaseReference user_db = mRef.child(current_uid);
                DatabaseReference ref = user_db.child("cart_item");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int total=0;
                        int item_price =0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            // ProductModel productModel = snapshot.getValue(ProductModel.class);
                            CartModel cartModel = snapshot.getValue(CartModel.class);
                            //    Toast.makeText(getBaseContext(),cartModel.getProduct_quantity(),Toast.LENGTH_LONG).show();

                            try {

                                item_price = item_price + (Integer.valueOf(cartModel.getProduct_quantity()))*(Integer.valueOf(cartModel.getProduct_real_price()));
                                total = total + (Integer.valueOf(cartModel.getProduct_quantity()))*(Integer.valueOf(cartModel.getProduct_off_price()));


                            }
                            catch (Exception e){
                                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();

                            }
                        }

                        if (total>=200){
                            callInstamojoPay(email, phone, String.valueOf(total), "Yours Electronics Payments", name);

                        }
                        else {
                            callInstamojoPay(email, phone, String.valueOf(total)+20, "Yours Electronics Payments", name);

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

    private void getLastSelectedAddress() {
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRef.child(user_uid);
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

    private void selectAddress() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.add_select_layout, null);

        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
        mAddSelectRCView = alertLayout.findViewById(R.id.add_select_rc_view);
        mAddSelectRCView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();
        getAllAddress(mAddSelectRCView,dialog);


    }

    private void getAllAddress(RecyclerView mAddSelectRCView, final android.support.v7.app.AlertDialog dialog) {
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRef.child(user_uid);
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


    private void placeOrderCart(final String paymentMethod) {

        final String user_uid = mAuth.getCurrentUser().getUid();
        final DatabaseReference user_db = mRef.child(user_uid);
        final DatabaseReference cart_ref = user_db.child("cart_item");

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        final String format = simpleDateFormat.format(date);

        user_db.child("address").orderByChild("last_selected_address").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final AddressModel addressModel = snapshot.getValue(AddressModel.class);

                    Toast.makeText(getBaseContext(), addressModel.getFirst_name(), Toast.LENGTH_LONG).show();
                    cart_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CartModel cartModel = snapshot.getValue(CartModel.class);
                                Map cart_data = new HashMap<>();



                                cart_data.put("item_name", cartModel.getProduct_title());
                                cart_data.put("item_desc", cartModel.getProduct_desc());
                                cart_data.put("item_img", cartModel.getProduct_img());
                                cart_data.put("item_quantity", cartModel.getProduct_quantity());
                                cart_data.put("order_status", "0");
                                cart_data.put("item_off_price", String.valueOf(Integer.valueOf(cartModel.getProduct_off_price())*Integer.valueOf(cartModel.getProduct_quantity())));
                                cart_data.put("item_real_price",String.valueOf(Integer.valueOf(cartModel.getProduct_real_price())*Integer.valueOf(cartModel.getProduct_quantity())));
                                cart_data.put("user_uid", user_uid);
                                cart_data.put("user_name", addressModel.getFirst_name() + addressModel.getLast_name());
                                cart_data.put("user_mobile", addressModel.getMobile_no());
                                cart_data.put("order_shipping_address", addressModel.getFull_address());
                                cart_data.put("pin_code", addressModel.getPin_code());
                                cart_data.put("state", addressModel.getState_name());
                                cart_data.put("order_date",format);
                                cart_data.put("total_price",String.valueOf((Integer.valueOf(cartModel.getProduct_off_price())*Integer.valueOf(cartModel.getProduct_quantity()))+20));
                                cart_data.put("item_delivery_price",cartModel.getProduct_delivery_price());
                                cart_data.put("item_single_price",cartModel.getProduct_off_price());
                                cart_data.put("item_category_id",cartModel.getProduct_category_id());
                                cart_data.put("payment_method",paymentMethod);
                                int randomID = new Random().nextInt(9999 - 1) + 1;

                                DatabaseReference orderRef = mRootRef.child("order_requests").child(user_uid).child(System.currentTimeMillis() + String.valueOf(randomID));
                                orderRef.updateChildren(cart_data, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(getApplicationContext(), "Ordered", Toast.LENGTH_LONG).show();
                                       Intent intent = new Intent(ShoppingCartActivity.this,MainActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       startActivity(intent);
                                        cart_ref.setValue(null);

                                    }
                                });


                            }

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


    private void showRecItem() {
        DatabaseReference ref = mRootRef.child("products");
        recyclerAdapterProduct = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
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
                        Intent intent = new Intent(ShoppingCartActivity.this, ProductDetailActivity.class);
                        intent.putExtra("id",recyclerAdapterProduct.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };


        mRecRCView.setAdapter(recyclerAdapterProduct);


    }

    private void getTotalPrice() {

        final String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference user_db = mRef.child(user_uid);
        DatabaseReference ref = user_db.child("cart_item");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total=0;
                int item_price =0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                   // ProductModel productModel = snapshot.getValue(ProductModel.class);
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                //    Toast.makeText(getBaseContext(),cartModel.getProduct_quantity(),Toast.LENGTH_LONG).show();

                    try {

                        item_price = item_price + (Integer.valueOf(cartModel.getProduct_quantity()))*(Integer.valueOf(cartModel.getProduct_real_price()));
                        total = total + (Integer.valueOf(cartModel.getProduct_quantity()))*(Integer.valueOf(cartModel.getProduct_off_price()));
                        mTotalPrice.setText("Rs."+" "+String.valueOf(total));
                        mPDTotalPrice.setText("Rs."+" "+String.valueOf(total));
                        mPDItemPrice.setText("Rs."+" "+String.valueOf(item_price));
                        mPDDiscount.setText("-Rs. "+String.valueOf(item_price-total));




                    }
                    catch (Exception e){
                        Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupToolBar() {

        mToolbar =  findViewById(R.id.cart_tb);
        mToolbar.setTitle("Shopping Cart");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }







}
