package com.vav.cn.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vav.cn.R;
import com.vav.cn.adapter.DetailsImageAdapter;
import com.vav.cn.server.model.GetDetailResponse;
import com.vav.cn.util.GeneralUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/25/2016.
 */
public class DetailsFragment extends Fragment {
    public static final String TAG_LOG = VoucherFragment.class.getSimpleName();
    public static final String TAG_PARAM_DETAIL_ITEM = "TAG_PARAM_DETAIL_ITEM";

    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderLeft;
    private ImageView mBtnHeaderRight;

    private TextView mLblMerchantName;
    private ViewPager mGalleryPager;
    private LinearLayout mGalleryIndicator;
    private List<ImageView> mImgDotsList;

    private TextView mLblDesc;
    private TextView mLblExpiredDate;
    private TextView mLblAddress;
    private TextView mLblTelephone;
    private TextView textViewOperatingDays;
    private TextView textViewOperatingTimes;

    private ImageView mBtnMerchantWeb;
    private ImageView mBtnMerchantFacebook;
    private ImageView mBtnMerchantTwitter;
    private ImageView mBtnMerchantInstagram;

    private String mMerchantWebUrl;
    private String mMerchantFacebookUrl;
    private String mMerchantTwitterUrl;
    private String mMerchantInstagramUrl;

    private GetDetailResponse mGetDetailResponse;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mGetDetailResponse = args.getParcelable(TAG_PARAM_DETAIL_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.details_fragment, container, false);

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.details_title);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderLeft.setVisibility(View.VISIBLE);
        mBtnHeaderLeft.setFocusable(true);
        mBtnHeaderLeft.setClickable(true);
        mBtnHeaderLeft.setOnClickListener(new View.OnClickListener() {
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
            }
        });
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);
        textViewOperatingDays = (TextView) view.findViewById(R.id.textviewOperatingDays);
        textViewOperatingTimes = (TextView) view.findViewById(R.id.textviewOperatingTimes);
        mLblMerchantName = (TextView) view.findViewById(R.id.lblMerchantName);

        mGalleryPager = (ViewPager) view.findViewById(R.id.detailGalleryViewPager);
        mGalleryIndicator = (LinearLayout) view.findViewById(R.id.detailGalleryIndicator);
        mImgDotsList = new ArrayList<>();

        mLblDesc = (TextView) view.findViewById(R.id.lblDesc);
        mLblExpiredDate = (TextView) view.findViewById(R.id.lblExpiredDate);
        mLblAddress = (TextView) view.findViewById(R.id.lblAddress);
        mLblTelephone = (TextView) view.findViewById(R.id.lblTelephone);

        mBtnMerchantWeb = (ImageView) view.findViewById(R.id.btnMerchantWeb);
        mBtnMerchantWeb.setVisibility(View.GONE);
        mBtnMerchantWeb.setFocusable(true);
        mBtnMerchantWeb.setClickable(true);
        mBtnMerchantWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directToNewActivity(mMerchantWebUrl);
            }
        });
        mBtnMerchantFacebook = (ImageView) view.findViewById(R.id.btnMerchantFacebook);
        mBtnMerchantFacebook.setVisibility(View.GONE);
        mBtnMerchantFacebook.setFocusable(true);
        mBtnMerchantFacebook.setClickable(true);
        mBtnMerchantFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directToNewActivity(mMerchantFacebookUrl);
            }
        });
        mBtnMerchantTwitter = (ImageView) view.findViewById(R.id.btnMerchantTwitter);
        mBtnMerchantTwitter.setVisibility(View.GONE);
        mBtnMerchantTwitter.setFocusable(true);
        mBtnMerchantTwitter.setClickable(true);
        mBtnMerchantTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directToNewActivity(mMerchantTwitterUrl);
            }
        });
        mBtnMerchantInstagram = (ImageView) view.findViewById(R.id.btnMerchantInstagram);
        mBtnMerchantInstagram.setVisibility(View.GONE);
        mBtnMerchantInstagram.setFocusable(true);
        mBtnMerchantInstagram.setClickable(true);
        mBtnMerchantInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directToNewActivity(mMerchantInstagramUrl);
            }
        });


        //ASIGN VALUE
        mLblMerchantName.setText(mGetDetailResponse.getMerchantname());
        final List<GetDetailResponse.Gallery> galleryList = mGetDetailResponse.getGallery();
        if (galleryList.size() == 0) {
            GetDetailResponse.Gallery blankItem = new GetDetailResponse.Gallery();
            blankItem.setImageurl("");
            galleryList.add(blankItem);
        }

        //set Gallery Pager
        setGalleryPager(galleryList);

        //set Indicator
        setGalleryIndicator(galleryList);
        String operationTimes;

        operationTimes = mGetDetailResponse.getOpening_time() != null ? mGetDetailResponse.getOpening_time() : getString(R.string.no_data);
        operationTimes = MessageFormat.format("{0} - {1}", operationTimes, (mGetDetailResponse.getClosing_time()) != null ? mGetDetailResponse.getClosing_time() : getString(R.string.no_data));

        //set Address
        mLblAddress.setText(mGetDetailResponse.getAddress());
        mLblTelephone.setText(mGetDetailResponse.getTel());
        mLblDesc.setText(mGetDetailResponse.getPromo_text() != null ? mGetDetailResponse.getPromo_text() : "");
        textViewOperatingDays.setText(mGetDetailResponse.getOperating_days() != null ? mGetDetailResponse.getOperating_days() : getString(R.string.no_data));
        textViewOperatingTimes.setText(operationTimes);
        if (mGetDetailResponse.getWebsite() != null) {
            if (!mGetDetailResponse.getWebsite().equals("")) {
                mBtnMerchantWeb.setVisibility(View.VISIBLE);
                mMerchantWebUrl = mGetDetailResponse.getWebsite();
                mMerchantWebUrl = GeneralUtil.getInstance().formatUrl(mMerchantWebUrl);
            }
        }
        if (mGetDetailResponse.getFacebook() != null) {
            if (!mGetDetailResponse.getFacebook().equals("")) {
                mBtnMerchantFacebook.setVisibility(View.VISIBLE);
                mMerchantFacebookUrl = mGetDetailResponse.getFacebook();
                mMerchantFacebookUrl = GeneralUtil.getInstance().formatUrl(mMerchantFacebookUrl);
            }
        }
        if (mGetDetailResponse.getTwitter() != null) {
            if (!mGetDetailResponse.getTwitter().equals("")) {
                mBtnMerchantTwitter.setVisibility(View.VISIBLE);
                mMerchantTwitterUrl = mGetDetailResponse.getTwitter();
                mMerchantTwitterUrl = GeneralUtil.getInstance().formatUrl(mMerchantTwitterUrl);
            }
        }
        if (mGetDetailResponse.getInstagram() != null) {
            if (!mGetDetailResponse.getInstagram().equals("")) {
                mBtnMerchantInstagram.setVisibility(View.VISIBLE);
                mMerchantInstagramUrl = mGetDetailResponse.getInstagram();
                mMerchantInstagramUrl = GeneralUtil.getInstance().formatUrl(mMerchantInstagramUrl);
            }
        }

        return view;
    }

    public void setGalleryPager(List<GetDetailResponse.Gallery> galleryList) {
        List<String> galleryUrlList = new ArrayList<>();
        for (int i = 0; i < galleryList.size(); i++) {
            galleryUrlList.add(galleryList.get(i).getImageurl());
        }
        mGalleryPager.setAdapter(new DetailsImageAdapter(getActivity(), galleryUrlList));
        mGalleryPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    public void setGalleryIndicator(List<GetDetailResponse.Gallery> galleryList) {
        for (int i = 0; i < galleryList.size(); i++) {
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
            mGalleryIndicator.addView(dot, params);

            mImgDotsList.add(dot);
        }
        if (galleryList.size() > 0) mImgDotsList.get(0).setSelected(true);
    }

    private void directToNewActivity(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
