package com.vav.cn.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vav.cn.fragment.VavResultPagerItemFragment;
import com.vav.cn.model.VavingOfferItem;

import java.util.List;

/**
 * Created by Handrata Samsul on 1/22/2016.
 */
public class VavResultAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<VavingOfferItem> mVavingOfferItemList;

    public VavResultAdapter(Context context, FragmentManager fm, List<VavingOfferItem> vavingOfferItemList) {
        super(fm);
        mContext = context;
        mVavingOfferItemList = vavingOfferItemList;
    }

    public void removeItem(int position) {
        mVavingOfferItemList.remove(position);
    }


    public List<VavingOfferItem> getVavingOfferItemList() {
        return mVavingOfferItemList;
    }

    public void setVavingOfferItemList(List<VavingOfferItem> vavingOfferItemList) {
        mVavingOfferItemList = vavingOfferItemList;
    }

    @Override
    public Fragment getItem(int position) {
        if (getCount() > 0) {
            VavingOfferItem currItem = mVavingOfferItemList.get(position);

            VavResultPagerItemFragment vavResultPagerItemFragment = new VavResultPagerItemFragment();
            Bundle args1 = new Bundle();
            args1.putParcelable(VavResultPagerItemFragment.TAG_PARAM_OFFER_ITEM, currItem);
            vavResultPagerItemFragment.setArguments(args1);
            return vavResultPagerItemFragment;
        }

        return new VavResultPagerItemFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (mVavingOfferItemList != null) {
            return mVavingOfferItemList.size();
        }
        return 0;
    }
}
