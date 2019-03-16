package com.yourselectronics.gauridev.yourselectronics;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private  int[] layouts = {R.layout.welcome_first_slide,R.layout.welcome_second_slide,R.layout.welcome_third_slide,R.layout.welcome_fourth_slide};

    private MpagerAdapter mpagerAdapter;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button Btn_skip,Btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        if (Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        setContentView(R.layout.activity_welcome);
        mPager=(ViewPager)findViewById(R.id.welcome_viewpager);
        mpagerAdapter = new MpagerAdapter(layouts,this);
        mPager.setAdapter(mpagerAdapter);

        Dots_Layout = (LinearLayout)findViewById(R.id.dots_layout);
        createDots(0);
        Btn_next = (Button)findViewById(R.id.btn_next);
        Btn_skip = (Button)findViewById(R.id.btn_skip);
        Btn_skip.setOnClickListener(this);
        Btn_next.setOnClickListener((this));




        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                createDots(position);

                if (position ==layouts.length-1)
                {
                    Btn_next.setText("Start");
                    Btn_skip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Btn_skip.setVisibility(View.VISIBLE);
                    Btn_next.setText("Next");
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
    private void createDots(int current_position)
    {
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();
        dots = new ImageView[layouts.length];

        for (int i=0; i<layouts.length;i++) {

            dots[i] = new ImageView(this);
            if (i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bullet_dot));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_indicator));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_next:
                loadNextSlide();

                break;
            case R.id.btn_skip:
                loadHome();
                new PreferenceManager(WelcomeActivity.this).writePreference();
                break;

        }

    }
    private void loadHome(){
        startActivity(new Intent(this,SplashActivity.class));
        finish();
    }
    public void loadNextSlide()
    {
        int next_slide =mPager.getCurrentItem()+1;

        if (next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }

}
