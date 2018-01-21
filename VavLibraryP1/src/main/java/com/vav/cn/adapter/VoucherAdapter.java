package com.vav.cn.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.vav.cn.fragment.VoucherPagerItemFragment;
import com.vav.cn.model.VoucherItem;

import java.util.List;

/**
 * Created by Handrata Samsul on 1/22/2016.
 */
public class VoucherAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<VoucherItem> mVoucherItemList;

    public VoucherAdapter(Context context, FragmentManager fm, List<VoucherItem> voucherItemList) {
        super(fm);
        mContext = context;
        mVoucherItemList = voucherItemList;
    }

    @Override
    public Fragment getItem(int position) {
        if (getCount() > 0) {
            VoucherItem currItem = mVoucherItemList.get(position);

            VoucherPagerItemFragment voucherPagerItemFragment = new VoucherPagerItemFragment();
            Bundle args1 = new Bundle();
            args1.putParcelable(VoucherPagerItemFragment.TAG_PARAM_VOUCHER_ITEM, currItem);
            args1.putInt(VoucherPagerItemFragment.TAG_PARAM_VOUCHER_PAGER_POSITION, position + 1);
            voucherPagerItemFragment.setArguments(args1);
            return voucherPagerItemFragment;
        }

        return new VoucherPagerItemFragment();
    }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    public void removeItem(int position) {
        mVoucherItemList.remove(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public int getCount() {
        if (mVoucherItemList != null) {
            return mVoucherItemList.size();
        }
        return 0;
    }


    public List<VoucherItem> getVoucherItemList() {
        return mVoucherItemList;
    }

    public void setVoucherItemList(List<VoucherItem> voucherItemList) {
        mVoucherItemList = voucherItemList;
    }
}
