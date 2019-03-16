package com.yourselectronics.gauridev.yourselectronics;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.Model.HozModel;
import com.yourselectronics.gauridev.yourselectronics.Model.OfferModel;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.Model.SmallCardModel;
import com.yourselectronics.gauridev.yourselectronics.Service.ListenOrderStatus;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.HozViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OfferHorizontalViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.OfferViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.SmallCardViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout indicator;

    private List<String> img_url;
    private List<String> colorName;

    private DatabaseReference mRef, mRootRef;
    private ProgressBar mProgress;
    private CardView mSearch;
    private RecyclerView mDODRcView, mFTView, mBDRcView, mTSView, mBMRcView,mNLRcView;
    FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> recyclerAdapter;

    private ImageView mBanner1, mBanner2;
    private ArrayList<String> banner_img;
    private ArrayList<String> product_id;
    private ArrayList<String> mKeysBanner = new ArrayList<>();
    private ArrayList<String> mKeysProduct = new ArrayList<>();




    private LinearLayout mLayout;
    private FirebaseAuth mAuth;
    private CardView mDODBtn, mTSBtn, mBDBtn, mFDBtn, mNLBtn;


    View view;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view!=null){
            if((ViewGroup)view.getParent()!=null)
                ((ViewGroup)view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_home, container, false);

        

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        indicator = (TabLayout) view.findViewById(R.id.indicator);
        mRef = FirebaseDatabase.getInstance().getReference().child("banner_slider").child("home_slider");
        mRootRef = FirebaseDatabase.getInstance().getReference();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDODBtn = view.findViewById(R.id.openDOD);
        mFDBtn = view.findViewById(R.id.openFD);
        mBDBtn = view.findViewById(R.id.openBD);
        mTSBtn = view.findViewById(R.id.openTS);
        mNLBtn = view.findViewById(R.id.openNL);




        setUpAllButton();

        mLayout = view.findViewById(R.id.LR1);

        banner_img = new ArrayList<>();
        product_id = new ArrayList<>() ;

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(),banner_img,product_id);
        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager,true);
        setUpSlider(sliderAdapter);




        mBanner1 = view.findViewById(R.id.banner1);
        mBanner2 = view.findViewById(R.id.banner2);

        mDODRcView = view.findViewById(R.id.dod_rc_view);
        mFTView = view.findViewById(R.id.ft_rc_view);
        mBDRcView = view.findViewById(R.id.bd_rc_view);
        mTSView =view.findViewById(R.id.ts_rc_view);
        mBMRcView = view.findViewById(R.id.bm_rc_view);
        mNLRcView = view.findViewById(R.id.nl_rc_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setReverseLayout(true);

        mDODRcView.setLayoutManager(gridLayoutManager);
        mDODRcView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        mDODRcView.setNestedScrollingEnabled(false);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        mDODRcView.setLayoutAnimation(animation);

        mFTView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mFTView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        mFTView.setNestedScrollingEnabled(false);
        mFTView.setLayoutAnimation(animation);

        LinearLayoutManager bdLayout = new LinearLayoutManager(getContext());
        bdLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        bdLayout.setReverseLayout(true);
        bdLayout.setStackFromEnd(true);

        mBDRcView.setLayoutManager(bdLayout);

        mTSView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mTSView.addItemDecoration(new GridSpacingItemDecoration(3,20,true));
        mTSView.setNestedScrollingEnabled(false);
        mTSView.setLayoutAnimation(animation);

        LinearLayoutManager nlLayout = new LinearLayoutManager(getContext());
        nlLayout.setReverseLayout(true);
        nlLayout.setStackFromEnd(true);
        mNLRcView.setLayoutManager(nlLayout);
        mNLRcView.setNestedScrollingEnabled(false);
        mNLRcView.setLayoutAnimation(animation);



        mBMRcView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        mSearch = view.findViewById(R.id.search_card);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });




        showDODView();
        showFDView();
        showBDView();
        showTSView();
        loadAllBanner();
        showNewLaunched();
        showBMView();


       Timer timer = new Timer();
       timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        return view;

    }

    private void showBMView() {
        DatabaseReference ref = mRootRef.child("products");



       recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.recommended_card_view,
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
                viewHolder.setProduct_discount_percentage(model.getProduct_discount_percenatge()+"% off");
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
        mBMRcView.setAdapter(recyclerAdapter);

    }

    private void showFDView() {
        DatabaseReference ref = mRootRef.child("offers_section/fd");
        FirebaseRecyclerAdapter<OfferModel, OfferViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<OfferModel, OfferViewHolder>(
                OfferModel.class,
                R.layout.offer_card_view,
                OfferViewHolder.class,
                ref.limitToLast(2)
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
        mFTView.setAdapter(recyclerAdapter);

    }

    private void showNewLaunched() {
        DatabaseReference ref = mRootRef.child("offers_section/nl");
        FirebaseRecyclerAdapter<OfferModel, OfferHorizontalViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<OfferModel, OfferHorizontalViewHolder>(
                OfferModel.class,
                R.layout.offer_horizontal_card,
                OfferHorizontalViewHolder.class,
                ref.limitToLast(5)
        ) {
            @Override
            protected void populateViewHolder(OfferHorizontalViewHolder viewHolder, final OfferModel model, int position) {
                viewHolder.setProduct_img(model.getProduct_img());
                viewHolder.setProduct_title(model.getProduct_title());
                viewHolder.setProduct_desc(model.getProduct_desc());
                viewHolder.setProduct_real_price("Rs."+" "+model.getProduct_real_price());
                viewHolder.setProduct_off_price("Rs. "+model.getProduct_off_price());
                viewHolder.setProduct_off_percentage(model.getProduct_off_percentage()+"% off");

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
        mNLRcView.setAdapter(recyclerAdapter);



    }

    private void setUpAllButton() {

        mDODBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Deals of the Day");

                Toast.makeText(getActivity(),"DOD Clicked ",Toast.LENGTH_LONG).show();
                Fragment fragment =new DODFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mFDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Featured Deals");
                Fragment fragment =new FDFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mBDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Best Discounts");
                Fragment fragment =new BDFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mTSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Top Selling");
                Fragment fragment =new TSFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mNLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("New Launched");
                Fragment fragment =new NLFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });








    }

    private void loadAllBanner() {
        DatabaseReference ref = mRootRef.child("banners/home");
        ref.child("sponsor_img").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img = dataSnapshot.child("img_url").getValue(String.class);
                final String p_id = dataSnapshot.child("product_id").getValue(String.class);


                if (!img.isEmpty() && !p_id.isEmpty()) {

                    Picasso.get().load(img).fit().into(mBanner2);


                }
                mBanner2.setOnClickListener(new View.OnClickListener() {
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
        ref.child("banner_01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img = dataSnapshot.child("img_url").getValue(String.class);
                final String p_id = dataSnapshot.child("product_id").getValue(String.class);
                if (!img.isEmpty() && !p_id.isEmpty()) {

                    Picasso.get().load(img).fit().into(mBanner1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUpSlider(final SliderAdapter sliderAdapter) {
        DatabaseReference ref = mRootRef.child("banners/home/slider_top");
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


    private void showTSView() {
        DatabaseReference ref = mRootRef.child("offers_section/ts");
        FirebaseRecyclerAdapter<SmallCardModel,SmallCardViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<SmallCardModel, SmallCardViewHolder>(
                SmallCardModel.class,
                R.layout.small_card_view,
                SmallCardViewHolder.class,
                ref.limitToFirst(9)
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
        mTSView.setAdapter(recyclerAdapter);


    }

    private void showBDView() {
        DatabaseReference ref = mRootRef.child("offers_section/bd");
        FirebaseRecyclerAdapter<HozModel, HozViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<HozModel, HozViewHolder>(
                HozModel.class,
                R.layout.hoz_card_view,
                HozViewHolder.class,
                ref.limitToLast(5)
        ) {
            @Override
            protected void populateViewHolder(HozViewHolder viewHolder, final HozModel model, int position) {
                viewHolder.setImg(model.getImg());
                viewHolder.setName(model.getName());
                viewHolder.setOff(model.getOff());
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
        mBDRcView.setAdapter(recyclerAdapter);

    }

    private void showDODView() {

        DatabaseReference ref = mRootRef.child("offers_section/dod");
        FirebaseRecyclerAdapter<OfferModel, OfferViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<OfferModel, OfferViewHolder>(
                OfferModel.class,
                R.layout.offer_card_view,
                OfferViewHolder.class,
                ref.limitToLast(2)
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
        mDODRcView.setAdapter(recyclerAdapter);
    }





    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < banner_img.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }


        // Inflate the layout for this fragment

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Yours Electronics");
    }

}




