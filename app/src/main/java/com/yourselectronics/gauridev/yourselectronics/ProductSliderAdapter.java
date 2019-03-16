package com.yourselectronics.gauridev.yourselectronics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductSliderAdapter extends PagerAdapter {
    private Context context;
    private List<String> img_url;
    private String id;


    public ProductSliderAdapter(Context context, List<String> img_url, String id) {
        this.context = context;
        this.img_url = img_url;
        this.id = id;
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

        TextView textView = (TextView) view.findViewById(R.id.textView);
        final ProgressBar mProgress = view.findViewById(R.id.progressBarSlider);
        // RelativeLayout linearLayout = (RelativeLayout) view.findViewById(R.id.linearLayout);
        ImageView img_slide = view.findViewById(R.id.slider_img);
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
        img_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ProductFullImageActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });

        // textView.setText(colorName.get(position));

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

    //Zoom Function




}
