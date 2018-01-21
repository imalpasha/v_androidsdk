package com.vav.cn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vav.cn.R;

import java.util.List;


/**
 * Created by Handrata Samsul on 1/25/2016.
 */
public class DetailsImageAdapter extends PagerAdapter {
    List<String> mImageUrlList;
    private Context mContext;

    public DetailsImageAdapter(Context context, List<String> imageUrlList) {
        mContext = context;
        mImageUrlList = imageUrlList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.details_gallery_item, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgDetailImage);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        String imageUrl = mImageUrlList.get(position);
        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .placeholder(mContext.getResources().getColor(R.color.transparent_dark))
                .error(R.drawable.ic_image_placeholder)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (mImageUrlList != null) {
            return mImageUrlList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
