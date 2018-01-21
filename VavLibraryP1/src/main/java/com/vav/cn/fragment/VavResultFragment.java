package com.vav.cn.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vav.cn.R;
import com.vav.cn.adapter.VavResultAdapter;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.model.VavingOfferItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 2/1/2016.
 */
public class VavResultFragment extends Fragment {
    public static final String TAG_LOG = VavResultFragment.class.getSimpleName();
    public static final String TAG_PARAM_OFFER_LIST = "TAG_PARAM_OFFER_LIST";

    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderLeft;
    private ImageView mBtnHeaderRight;

    private ViewPager mPager;
    private VavResultAdapter mVavResultAdapter;
    private List<VavingOfferItem> mVavingOfferItemList = new ArrayList<>();
    private LinearLayout mPagerIndicator;
    private List<ImageView> mImgDotsList;
    private RelativeLayout relativeLayoutContent;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mVavingOfferItemList = args.getParcelableArrayList(TAG_PARAM_OFFER_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.vav_result_fragment, container, false);

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.vav_result_title);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderLeft.setVisibility(View.INVISIBLE);
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setImageResource(R.drawable.header_btn_close);
        mBtnHeaderRight.setVisibility(View.VISIBLE);
        mBtnHeaderRight.setFocusable(true);
        mBtnHeaderRight.setClickable(true);
        mBtnHeaderRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentByTag(TAG_LOG);
                if (fragment != null) {
                    ft.remove(fragment);
                    ft.commit();
                    fm.popBackStack();
                }
                VavDirectGenerator.getInstance().closeFragment();
            }
        });

        relativeLayoutContent = (RelativeLayout) view.findViewById(R.id.contentLayout);
        mPager = (ViewPager) view.findViewById(R.id.vavResultViewPager);
        mPagerIndicator = (LinearLayout) view.findViewById(R.id.vavResultIndicator);
        mImgDotsList = new ArrayList<>();
        mVavResultAdapter = new VavResultAdapter(getActivity(), getChildFragmentManager(), mVavingOfferItemList);
        mPager.setAdapter(mVavResultAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mImgDotsList.size(); i++) {
                    if (i == position) mImgDotsList.get(i).setSelected(true);
                    else mImgDotsList.get(i).setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setPagerIndicator(mVavingOfferItemList);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayoutContent.post(new Runnable() {
                    public void run() {
                        if (getActivity() != null && VavResultFragment.this.isAdded()) {
                            try {
                                if (relativeLayoutContent.getWidth() > 0 && relativeLayoutContent.getHeight() > 0)
                                    Glide.with(VavResultFragment.this).load(R.drawable.vav_result_bg).into(new SimpleTarget<GlideDrawable>(relativeLayoutContent.getMeasuredWidth(), relativeLayoutContent.getMeasuredHeight()) {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                relativeLayoutContent.setBackground(resource);
                                            } else {
                                                relativeLayoutContent.setBackgroundDrawable(resource);
                                            }
                                        }
                                    });
                            } catch (Exception e) {
                                Log.d(TAG_LOG,"Error Occur while setting background");
                            }
                        }
                    }
                });
            }
        });
        return view;
    }

    public void removeItemByOfferId(final int offerId) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < mVavingOfferItemList.size(); i++) {
                        if (mVavingOfferItemList.get(i).getOfferId() == offerId) {

                            mVavResultAdapter.removeItem(i);
                            mVavResultAdapter.notifyDataSetChanged();

                            FragmentManager fmSupport = getActivity().getSupportFragmentManager();
                            fmSupport.beginTransaction().detach(VavResultFragment.this).attach(VavResultFragment.this).commit();

                            if (mVavResultAdapter.getCount() == 0) {
                                mBtnHeaderRight.performClick();
                            }
                            break;
                        }
                    }
                }
            });
        }
    }

    public void setPagerIndicator(List<VavingOfferItem> offerList) {
        for (int i = 0; i < offerList.size(); i++) {
            ImageView dot = new ImageView(getActivity());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dot.setImageDrawable(getResources().getDrawable(R.drawable.gallery_dot_indicator_selector, getActivity().getTheme()));
            } else {
                dot.setImageDrawable(getResources().getDrawable(R.drawable.gallery_dot_indicator_selector));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 5, 5, 5);
            mPagerIndicator.addView(dot, params);

            mImgDotsList.add(dot);
        }
        if (offerList.size() > 0) mImgDotsList.get(0).setSelected(true);
    }
}
