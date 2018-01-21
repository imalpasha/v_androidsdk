package com.vav.cn.util;

import com.vav.cn.model.VoucherItem;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Handrata Samsul on 2/11/2016.
 */
public class VoucherListComparatorExpireDate implements Comparator<VoucherItem> {
    @Override
    public int compare(VoucherItem lhs, VoucherItem rhs) {
        Date dateLhs = GeneralUtil.getInstance().parseExpireDateFromServer(lhs.getVoucherExpiredDate());
        Date dateRhs = GeneralUtil.getInstance().parseExpireDateFromServer(rhs.getVoucherExpiredDate());

        return dateLhs.compareTo(dateRhs);
    }
}
