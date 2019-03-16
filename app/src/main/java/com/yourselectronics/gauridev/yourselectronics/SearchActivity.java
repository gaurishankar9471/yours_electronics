package com.yourselectronics.gauridev.yourselectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourselectronics.gauridev.yourselectronics.Model.ProductModel;
import com.yourselectronics.gauridev.yourselectronics.ViewHolder.ProductViewHolder;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mRef;
    private Toolbar mToolbar;
    private ProgressDialog mProgress;
    FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> recyclerAdapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.product_rc_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,20,true));
        mRef = FirebaseDatabase.getInstance().getReference().child("products");



        setupToolBar();


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Please Wait...");
        mProgress.setMessage("Loading Products");

        if (getIntent()!=null) {
            id = getIntent().getStringExtra("cat_id");
            showProduct(id);
        }


    }



    private void showProduct(String id) {

        recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(
                ProductModel.class,
                R.layout.product_view_card,
                ProductViewHolder.class,
                mRef.orderByChild("product_category_id").equalTo(id)
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
                        Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
                        intent.putExtra("id",recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                mProgress.dismiss();
            }

        };
        mRecyclerView.setAdapter(recyclerAdapter);
    }


    private void setupToolBar() {

        mToolbar =  findViewById(R.id.toolbar_search);
        mToolbar.setTitle("Shopping Cart");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.search_icon);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

       // searchView.setIconified(false);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    // Toast.makeText(getApplicationContext(),"Changes",Toast.LENGTH_LONG).show();

                    callProduct(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //  Toast.makeText(getApplicationContext(),"Submitted",Toast.LENGTH_LONG).show();


                    return false;
                }
            });





        return true;
    }

    private void callProduct(String query) {

        recyclerAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>
                (
                        ProductModel.class,
                        R.layout.product_view_card,
                        ProductViewHolder.class,
                        mRef.orderByChild("product_title").startAt(query.toUpperCase()).endAt(query.toLowerCase()+"\uf8ff")

                ) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, ProductModel model, final int position) {
                viewHolder.setProduct_img(model.getProduct_img());
                viewHolder.setProduct_title(model.getProduct_title());
                viewHolder.setProduct_desc(model.getProduct_desc());
                viewHolder.setProduct_off_price(model.getProduct_off_price());
                viewHolder.setProduct_real_price(model.getProduct_real_price());
                viewHolder.setProduct_discount_percentage(model.getProduct_discount_percenatge());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
                        intent.putExtra("id",recyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                mProgress.dismiss();
            }
        };
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
