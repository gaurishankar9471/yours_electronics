package com.yourselectronics.gauridev.yourselectronics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yourselectronics.gauridev.yourselectronics.Service.ListenOrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private android.support.v4.app.Fragment fragmentHome, fragmentOrder;
    private FirebaseAuth mAuth;
    private DatabaseReference mCartRef;
    private DatabaseReference mRootRef;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mAuth= FirebaseAuth.getInstance();
        mCartRef = FirebaseDatabase.getInstance().getReference().child("user_database");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //MenuText//

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menu_log = menu.findItem(R.id.log_in_nav_icon);

        if(user!=null){
            menu_log.setTitle("Logout");
            menu_log.setIcon(R.drawable.ic_action_log_out);
        }
        else{
            menu_log.setTitle("Log IN");
            menu_log.setIcon(R.drawable.ic_action_log_in);
        }

        setupToolBarMenu();
        setupNavigationDrawerMenu();

        checkUser();
        updateToken();

        subscribeToNewsAlert();



        fragmentHome = new HomeFragment();

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_view,fragmentHome);
            fragmentTransaction.commit();




        String type = getIntent().getStringExtra("id");
        if (type != null) {
            switch (type) {
                case "openOrder":
                    Toast.makeText(this,type,Toast.LENGTH_LONG).show();
                    fragmentOrder = new OrdersFragment();
                    android.support.v4.app.FragmentManager fragmentManagerOrder = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransactionOrder = fragmentManagerOrder.beginTransaction();
                    fragmentTransactionOrder.replace(R.id.main_view,fragmentOrder);
                    fragmentTransactionOrder.commit();
                    break;
            }
        }

       /* if (getIntent()!=null){
            String openOrder = getIntent().getStringExtra("id");
            if (openOrder.equals("openOrder")){


            }
        }
        */
        //Slider_Operation//
    }

    private void updateToken() {
        if(user!=null){
            String user_id = mAuth.getCurrentUser().getUid();
            String token_id = FirebaseInstanceId.getInstance().getToken();
            mRootRef.child("user_database").child(user_id).child("token_id").setValue(token_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

        }
    }

    private void subscribeToNewsAlert() {

        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfully Subscribed";
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"News Alert not Subcribed",Toast.LENGTH_LONG).show();

                        }
                    }
                });


    }

    private void checkUser() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Sign in logic here.


                }
            }
        };

    }

    private void setupToolBarMenu() {

        mToolbar.setTitle("Yours Electronics");
        setSupportActionBar(mToolbar);

    }

    private void setupNavigationDrawerMenu() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_Layout);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        android.support.v4.app.Fragment fragment = null;

       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        switch (item.getItemId()) {
            case R.id.home_nav_icon:
                fragment = new HomeFragment();
                mToolbar.setTitle("Yours Electronics");

                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.category_nav_icon:

                fragment = new CatFragment();
                mToolbar.setTitle("Categories");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.top_offers_nav_icon:

                fragment = new TopOffersFragment();
                mToolbar.setTitle("Top Offers");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.todays_deals_nav_icon:

                fragment = new TodaysDealsFragment();
                mToolbar.setTitle("Today's Deals");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.my_account:

                if (user != null) {

                    fragment = new AccountFragment();
                    mToolbar.setTitle("My Account");



                } else {
                    Toast.makeText(getApplicationContext(),"Please Login...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }


                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_orders_nav_icon:


                if (user != null) {
                    // User is signed in
                    fragment = new OrdersFragment();
                    mToolbar.setTitle("My Orders");

                } else {
                    // No user is signed in
                    Toast.makeText(getApplicationContext(),"Please Login...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }


                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.log_in_nav_icon:
                if (user!=null){
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(),"Successfully Log Out",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                    }
                else {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        //Toast.makeText(getApplicationContext(),"Please Login...", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                }


                break;

            case R.id.contact_nav_icon:
                Intent intent = new Intent(MainActivity.this,ContactUsActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.about_us_nav_icon:
                Intent intentAbout = new Intent(MainActivity.this,AboutUsActivity.class);
                startActivity(intentAbout);
                mDrawerLayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.live_help_nav_icon:
                if (user!=null){
                    Intent intentLiveHelp = new Intent(MainActivity.this,ChatHomeActivity.class);
                    startActivity(intentLiveHelp);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Login...", Toast.LENGTH_LONG).show();
                    Intent intentLiveChat = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intentLiveChat);
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.privacy_policy_nav_icon:

                Intent intentPrivacy = new Intent(MainActivity.this,PrivacyActivity.class);
                startActivity(intentPrivacy);


                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.share_nav_icon:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Yours Electronics");
                    i.putExtra(Intent.EXTRA_TEXT,"Get All Electronics Components at cheap prices at Yours Electronics\n For more details Download Our App \n Link :https://play.google.com/store/apps/details?id=com.yourselectronics.gauridev.yourselectronics " );
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.rate_us_nav_icon:

                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                startActivity(rateIntent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;



        }





                if (fragment!=null){
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_view,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


        return true;

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_Layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_overflow_menu, menu);

        if (user!=null) {
           showCount(menu);
       }


        return true;
    }

    private void showCount(Menu menu) {
        final MenuItem menuItem2 = menu.findItem(R.id.cart_icon);
        final MenuItem notification = menu.findItem(R.id.notification_icon);
        String user_uid = mAuth.getCurrentUser().getUid();
        DatabaseReference mUser_db= mCartRef.child(user_uid);
        DatabaseReference mUserCartItem = mUser_db.child("cart_item");
        mUserCartItem.addValueEventListener(new ValueEventListener() {

            private int cart_count;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cart_count =  (int) dataSnapshot.getChildrenCount();
                menuItem2.setIcon(Converter.convertLayoutToImage(MainActivity.this,cart_count,R.drawable.ic_shopping_cart_black_24dp
                ));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRootRef.child("notification").child(user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               int notification_count = (int) dataSnapshot.getChildrenCount();
               notification.setIcon(Converter.convertLayoutToImage(MainActivity.this,notification_count,R.drawable.ic_notifications_black_24dp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_icon) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // User is signed in
                Intent intent = new Intent(MainActivity.this,ShoppingCartActivity.class);
                startActivity(intent);
            } else {
                // No user is signed in
                finish();
                startActivity(getIntent());
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        }
        if(id==R.id.notification_icon){
            if (user != null) {
                // User is signed in
                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
            } else {
                // No user is signed in
                finish();
                startActivity(getIntent());
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user!=null) {
            Intent intent = new Intent(MainActivity.this, ListenOrderStatus.class);
           // startService(intent);
        }


    }


}
