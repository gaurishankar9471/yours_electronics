package com.yourselectronics.gauridev.yourselectronics;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.Model.ListViewModel;
import com.yourselectronics.gauridev.yourselectronics.Model.MessageModel;
import com.yourselectronics.gauridev.yourselectronics.Model.PinCodeModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ListViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private ViewPager product_detail_img;
    private TabLayout indicator;
    private TextView product_detail_title,product_detail_desc, product_off_price, product_real_price, product_discount_percentage;
    private DatabaseReference mRef, mCartRef;
    private String id;
    private Button mAddCartBtn, mSubmitBtn,mWatchVideo, mRatingBtn;
    private FirebaseAuth mAuth;
    private NonScrollListView mDescList, mDescList2;
    private ArrayList<String> img = new ArrayList<>() ;
    private RecyclerView mRecRCView, mDescListRCView ,mDetailsListRCView;
    private FirebaseRecyclerAdapter<ProductModel,ProductViewHolder> recyclerAdapter;
    private String r = "Rs. ";
    private android.support.v7.widget.Toolbar mToolbar;
    private FirebaseUser user;
    private  String product_id;
    private DatabaseReference mRootRef;
    private ProgressDialog mProgress;
    private LinearLayout mTimerLayout;
    private CardView mPinCodeSbmBtn;

    private TextView mPinCodeVal;

    private FrameLayout mPinFrame;

    private TextView mPD_Location, mPDEDDate, mPDCODStatus;


    ArrayList<String> list;
    ArrayList<String> mKey;
    private ArrayAdapter<String> adapter, adapter2;

    private List<MessageModel> messageList = new ArrayList<>();
    private TextView mDealTimeout;

    private LinearLayout LLPinCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);



        //list=new ArrayList<>();
      //  mKey = new ArrayList<>();

        if (getIntent() != null) {
            product_id = getIntent().getStringExtra("id");
        }
        if (!product_id.isEmpty()) {

        }
        mToolbar = findViewById(R.id.pd_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();


        mRef = FirebaseDatabase.getInstance().getReference().child("products");
        mCartRef = FirebaseDatabase.getInstance().getReference().child("user_database");
        mAuth = FirebaseAuth.getInstance();
       // mDescList = findViewById(R.id.desc_list_view);
      //  mDescList2 = findViewById(R.id.desc_list_view2);


       // adapter = new ArrayAdapter<String>(this, R.layout.list_view_item, list);
       // adapter2 = new ArrayAdapter<String>(this, R.layout.list_view_item, list);

      //  mDescList.setAdapter(adapter);
       // mDescList2.setAdapter(adapter2);

        mPinCodeVal = findViewById(R.id.pd_pin_code_text);
        mPinFrame = findViewById(R.id.pd_pin_frame);

        LLPinCode = findViewById(R.id.LLPinCode);
        mPD_Location = findViewById(R.id.pd_location);
        mPDEDDate = findViewById(R.id.pd_estimated_delivery_date);
        mPDCODStatus = findViewById(R.id.pd_cod_status);




        mPinCodeSbmBtn = findViewById(R.id.pd_pin_code_sbm_btn);

        mTimerLayout = findViewById(R.id.pd_timer_layout);
        mDealTimeout = findViewById(R.id.p_deal_timeout);
        setDealTimeOut();

        mDescListRCView = findViewById(R.id.p_desc_list);
        mDescListRCView.setLayoutManager(new LinearLayoutManager(this));
        mDescListRCView.setNestedScrollingEnabled(false);

        mDetailsListRCView = findViewById(R.id.p_details_rc_view);
        mDetailsListRCView.setLayoutManager(new LinearLayoutManager(this));
        mDetailsListRCView.setNestedScrollingEnabled(false);




        mRecRCView = findViewById(R.id.pd_rec_rc_view);
        mRecRCView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));



            mAddCartBtn = findViewById(R.id.add_to_cart_btn);
        mAddCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    mProgress.setTitle("Please Wait...");
                    mProgress.setMessage("Adding Product...");
                    mProgress.show();
                    addProduct();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "First Login Please...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        mRatingBtn = findViewById(R.id.rating_btn);
        mRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });


        if (getIntent() != null) {


            product_detail_img = findViewById(R.id.product_detail_img_viewPager);
            indicator = findViewById(R.id.indicator_Product);

            ProductSliderAdapter sliderAdapter = new ProductSliderAdapter(this, img,product_id);

            product_detail_img.setAdapter(sliderAdapter);
            indicator.setupWithViewPager(product_detail_img, true);


            product_detail_title = findViewById(R.id.product_detail_title);
            product_detail_desc = findViewById(R.id.product_detail_desc);
            product_off_price = findViewById(R.id.product_off_price);
            product_real_price = findViewById(R.id.product_real_price);
            product_real_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            product_discount_percentage = findViewById(R.id.product_discount_percentage);

            mSubmitBtn = findViewById(R.id.sbm_ord_btn);


            if (getIntent() != null) {
                id = getIntent().getStringExtra("id");
            }
            if (!id.isEmpty()) {
                showDetails();
                setUpSlider(sliderAdapter);
                setFullDesc();
                setFullDetails();

            }



            mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user != null) {
                        Intent intent = new Intent(ProductDetailActivity.this, OrderActivity.class);
                        intent.putExtra("product_id", id);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }

                }
            });

           mWatchVideo = findViewById(R.id.watch_video_btn);
            mWatchVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        mPinFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPinCodeDialog();

            }
        });


        mPinCodeSbmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Button TODO",Toast.LENGTH_LONG).show();

                String pin_code = mPinCodeVal.getText().toString();
                showPinCodeDialog();

            }
        });



    }

    private void showPinCodeDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pin_code_pop_up, null);

        CardView checkBtn = alertLayout.findViewById(R.id.pd_pin_code_sbm_btn);
        final EditText mCouponCode = alertLayout.findViewById(R.id.pd_pin_code_text);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin_code = mCouponCode.getText().toString();
                if(pin_code.isEmpty()){
                    mCouponCode.setError("Pin Code is required");
                    mCouponCode.requestFocus();
                    return;
                }
                if (pin_code.length()>6){
                    mCouponCode.setError("Please enter 6 digit Pin Code");
                    mCouponCode.requestFocus();
                    return;
                }
                if (pin_code.length()<6){
                    mCouponCode.setError("Please enter 6 digit Pin Code");
                    mCouponCode.requestFocus();
                    return;
                }
                mProgress.setMessage("Checking Pin Code");
                mProgress.show();
                checkPinCodeValidity(pin_code, dialog);
            }
        });
    }

    private void checkPinCodeValidity(final String pin_code, final AlertDialog dialog) {
        Query query = mRootRef.child("supported_pin_code").orderByChild("pin_code").equalTo(pin_code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        PinCodeModel pinCodeModel = snapshot.getValue(PinCodeModel.class);
                        mPD_Location.setText(pinCodeModel.getLocation()+", "+pinCodeModel.getState());
                        mPDEDDate.setText(pinCodeModel.getEstimated_delivery_time()+" working days");
                        mPDCODStatus.setText(getStatusToText(pinCodeModel.isCod_status()));
                        mPinCodeVal.setText(pinCodeModel.getPin_code());
                        dialog.dismiss();
                        LLPinCode.setVisibility(View.VISIBLE);
                        mProgress.dismiss();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"Sorry... We don't deliver at this location",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getStatusToText(boolean cod_status) {
        String s ="Not Available";
       if (cod_status==true){
           s="Available";
       }
       if (cod_status==false){
           s="Not Available";
       }
       return s;
    }

    private void setDealTimeOut() {
    }

    private void setFullDetails() {

        DatabaseReference ref = mRootRef.child("products").child(id).child("product_full_details");
        FirebaseRecyclerAdapter<ListViewModel,ListViewHolder>  recyclerAdapter = new FirebaseRecyclerAdapter<ListViewModel, ListViewHolder>(
                ListViewModel.class,
                R.layout.list_rc_view,
                ListViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ListViewModel model, int position) {
                viewHolder.setList_text(model.getList_text());

            }
        };
        mDetailsListRCView.setAdapter(recyclerAdapter);
    }

    private void setFullDesc() {
        DatabaseReference ref = mRootRef.child("products").child(id).child("product_full_desc");
        FirebaseRecyclerAdapter<ListViewModel,ListViewHolder>  recyclerAdapter = new FirebaseRecyclerAdapter<ListViewModel, ListViewHolder>(
                ListViewModel.class,
                R.layout.list_rc_view,
                ListViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ListViewModel model, int position) {
                viewHolder.setList_text(model.getList_text());

            }
        };
        mDescListRCView.setAdapter(recyclerAdapter);

    }

    private void showRatingDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rating_layout, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_LONG).show();

            }
        });
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Rated",Toast.LENGTH_LONG).show();

            }
        });

        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void showRecPro(String product_category_id) {

         recyclerAdapter =new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.recommended_card_view,
                ProductViewHolder.class,
                mRef.orderByChild("product_category_id").equalTo(product_category_id)
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
                        Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                        intent.putExtra("id",recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        mRecRCView.setAdapter(recyclerAdapter);


    }

    private void setUpSlider(final ProductSliderAdapter sliderAdapter) {
        mRef.child(id).child("img_slider").addChildEventListener(new ChildEventListener() {
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


    private void setDesc() {
        mRef.child(id).child("product_full_desc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                list.add(dataSnapshot.getValue(String.class));
                mKey.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                int index = mKey.indexOf(dataSnapshot.getKey());
                list.set(index,dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addProduct() {
        DatabaseReference ref = mRootRef.child("products").child(product_id);
        String user_uid = mAuth.getCurrentUser().getUid();
        final DatabaseReference mUser_db= mCartRef.child(user_uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ProductModel productModel = dataSnapshot.getValue(ProductModel.class);

                    Map cart_data = new HashMap<>();
                    cart_data.put("product_img",productModel.getProduct_img());
                    cart_data.put("product_title",productModel.getProduct_title());
                    cart_data.put("product_desc",productModel.getProduct_desc());
                    cart_data.put("product_real_price",productModel.getProduct_real_price());
                    cart_data.put("product_off_price",productModel.getProduct_off_price());
                    cart_data.put("product_discount_percentage",productModel.getProduct_discount_percenatge());
                    cart_data.put("product_quantity","1");
                    cart_data.put("product_delivery_price",productModel.getProduct_delivery_price());
                    cart_data.put("product_category_id",productModel.getProduct_category_id());


                    DatabaseReference mUserCartItem = mUser_db.child("cart_item").push();
                    mUserCartItem.updateChildren(cart_data, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Toast.makeText(getBaseContext(),"Added to Cart",Toast.LENGTH_LONG).show();
                            mProgress.dismiss();

                        }
                    });


                }
                catch (Exception e){
                    Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void showDetails() {
        mRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ProductModel productModel = dataSnapshot.getValue(ProductModel.class);

                final String video_id =dataSnapshot.child("product_video_id").getValue(String.class);


                mToolbar.setTitle(productModel.getProduct_title());
                product_detail_title.setText(productModel.getProduct_title());
                product_detail_desc.setText(productModel.getProduct_desc());
                product_off_price.setText("Rs. "+productModel.getProduct_off_price());
                product_real_price.setText("Rs. "+productModel.getProduct_real_price());
                product_discount_percentage.setText(productModel.getProduct_discount_percenatge()+"% off");

                if (dataSnapshot.child("product_deal_timeout").exists()){
                    String time = dataSnapshot.child("product_deal_timeout").getValue(String.class);
                    if (time!=null){
                        mTimerLayout.setVisibility(View.VISIBLE);
                        CountDownTimer countDownTimer = new CountDownTimer(Long.valueOf(time)-System.currentTimeMillis(),1000){
                            @Override
                            public void onTick(long l) {
                                mDealTimeout.setText(covertMilliSecondToHMS(l/1000));
                            }

                            @Override
                            public void onFinish() {
                                mDealTimeout.setText("Expired");

                            }
                        };
                        countDownTimer.start();
                    }
                }

                showRecPro(productModel.getProduct_category_id());


                mWatchVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ProductDetailActivity.this,VideoTutorialActivity.class);
                        intent.putExtra("video_id",video_id);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pd_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon_pd);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductDetailActivity.this,0,R.drawable.ic_search_black_24dp));



        if (user!=null) {
            showCartCount(menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_icon_pd) {
            Intent intent = new Intent(ProductDetailActivity.this,SearchActivity.class);
            startActivity(intent);

        }
        if(id==R.id.cart_icon_pd){

            if (user!=null) {
                Intent intent = new Intent(ProductDetailActivity.this,ShoppingCartActivity.class);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void showCartCount(Menu menu) {
        final MenuItem menuItem2 = menu.findItem(R.id.cart_icon_pd);
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference mUser_db= mCartRef.child(user_uid);
        DatabaseReference mUserCartItem = mUser_db.child("cart_item");
        mUserCartItem.addValueEventListener(new ValueEventListener() {

            private int cart_count;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cart_count =  (int) dataSnapshot.getChildrenCount();
                menuItem2.setIcon(Converter.convertLayoutToImage(ProductDetailActivity.this,cart_count,R.drawable.ic_shopping_cart_black_24dp
                ));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String covertMilliSecondToHMS(long time) {
        long s = time % 60;
        long m = (time / 60) % 60;
        long h = (time / (60 * 60)) % 24;
        int days = (int) time / (60 * 60 * 24);


        if (time > 86399 && time < 172799) {
            return String.valueOf(days) + " day";
        } else if (time > 172799) {
            return String.valueOf(days) + " days";

        } else {
            return String.format("%d:%02d:%02d", h, m, s);

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
