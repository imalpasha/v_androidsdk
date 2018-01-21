package com.vav.cn.util;

import com.vav.cn.model.VoucherItem;

import java.util.Comparator;

/**
 * Created by Handrata Samsul on 2/11/2016.
 */
public class VoucherListComparatorAlphabet implements Comparator<VoucherItem> {
    @Override
    public int compare(VoucherItem lhs, VoucherItem rhs) {
        return lhs.getVoucherName().compareTo(rhs.getVoucherName());
    }
}
