package com.vav.cn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vav.cn.R;
import com.vav.cn.adapter.VoucherAdapter;
import com.vav.cn.model.VoucherItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/22/2016.
 */
public class VoucherFragment extends Fragment {
    public static final String TAG_LOG = VoucherFragment.class.getSimpleName();
    public static final String TAG_PARAM_VOUCHER_LIST = "TAG_PARAM_VOUCHER_LIST";
    public static final String TAG_PARAM_SELECTED_VOUCHER = "TAG_PARAM_SELECTED_VOUCHER";

    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderLeft;
    private ImageView mBtnHeaderRight;

    private TextView mLblPageIndicator;
    private ViewPager mPager;
    private VoucherAdapter mFragmentPagerAdapter;
    private List<VoucherItem> mVoucherItemList = new ArrayList<>();
    private int mInitialVoucher = 1;

    private static String formatPagerIndicator(int position, int total) {
        return position + "/" + total;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mVoucherItemList = args.getParcelableArrayList(TAG_PARAM_VOUCHER_LIST);
        mInitialVoucher = args.getInt(TAG_PARAM_SELECTED_VOUCHER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.voucher_fragment, container, false);

        mLblHeaderTitle = (TextView) rootView.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.voucher_title);
        mBtnHeaderLeft = (ImageView) rootView.findViewById(R.id.btnHeaderLeft);
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
                }
            }
        });
        mBtnHeaderRight = (ImageView) rootView.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);

        mPager = (ViewPager) rootView.findViewById(R.id.voucherViewPager);
        mFragmentPagerAdapter = new VoucherAdapter(getActivity(), getChildFragmentManager(), mVoucherItemList);
        mPager.setAdapter(mFragmentPagerAdapter);
        mPager.setCurrentItem(mInitialVoucher);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mLblPageIndicator.setText(formatPagerIndicator((position + 1), mVoucherItemList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mLblPageIndicator = (TextView) rootView.findViewById(R.id.lblPagerIndicator);

        if (TextUtils.isEmpty(mLblPageIndicator.getText())) {
            mLblPageIndicator.setText(formatPagerIndicator(mInitialVoucher + 1, mVoucherItemList.size()));
        }

        return rootView;
    }

    public void removeItemByOfferId(int offerId) {
        for (int i = 0; i < mVoucherItemList.size(); i++) {
            if (mVoucherItemList.get(i).getVoucherId() == offerId) {
                mFragmentPagerAdapter.removeItem(i);
                mFragmentPagerAdapter.notifyDataSetChanged();

                FragmentManager fmSupport = getActivity().getSupportFragmentManager();
                fmSupport.beginTransaction().detach(this).attach(this).commit();

                if (mFragmentPagerAdapter.getCount() == 0) {
                    android.app.FragmentManager fm = getActivity().getFragmentManager();
                    android.app.Fragment fragment = fm.findFragmentByTag(VoucherListFragment.TAG_LOG);
                    fm.beginTransaction().detach(fragment).attach(fragment).commit();

                    mBtnHeaderLeft.performClick();
                }
                break;
            }
        }
    }
}
