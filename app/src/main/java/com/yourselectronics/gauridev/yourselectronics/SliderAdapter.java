package com.yourselectronics.gauridev.yourselectronics;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<String> img_url;
    private List<String> colorName;

    public SliderAdapter(Context context, List<String> img_url, List<String> colorName) {
        this.context = context;
        this.img_url = img_url;
        this.colorName = colorName;
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
      //  if (!colorName.get(position).isEmpty()) {
            img_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("id", colorName.get(position));
                    context.startActivity(intent);
                }
            });
        //}


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
}