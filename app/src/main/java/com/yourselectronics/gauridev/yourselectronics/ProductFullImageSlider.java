package com.yourselectronics.gauridev.yourselectronics;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductFullImageSlider extends PagerAdapter {
    private Context context;
    private List<String> img_url;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private  ImageView img_slide;




    public ProductFullImageSlider(Context context, List<String> img_url) {
        this.context = context;
        this.img_url = img_url;
    }

    @Override
    public int getCount() {
        return img_url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        img_slide = view.findViewById(R.id.slider_img);
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());

        final ProgressBar mProgress = view.findViewById(R.id.progressBarSlider);
        if (!img_url.get(position).isEmpty()) {
            Picasso.get().load(img_url.get(position)).fit().placeholder(R.drawable.ic_image_black_24dp).into(img_slide, new Callback() {
                @Override
                public void onSuccess() {
                    mProgress.setVisibility(View.GONE);

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            img_slide.setScaleX(mScaleFactor);
            img_slide.setScaleY(mScaleFactor);
            return true;
        }
    }



}
