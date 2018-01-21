package com.vav.cn.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Handrata Samsul on 1/20/2016.
 */
public class VoucherItem implements Parcelable {
    public static final Creator<VoucherItem> CREATOR = new Creator<VoucherItem>() {
        @Override
        public VoucherItem createFromParcel(Parcel source) {
            return new VoucherItem(source);
        }

        @Override
        public VoucherItem[] newArray(int size) {
            return new VoucherItem[size];
        }
    };
    private int voucherId;
    private String voucherUrl;
    private String voucherName;
    private String voucherDesc;
    private String voucherImgUrl;
    private String voucherImgThumbUrl;
    private String voucherExpiredDate;
    private String content_source_type_name;
    private String content_source_url;
    private String detailslink;
    private String termslink;
    private Integer stock_available;
    private String distance;

    public VoucherItem() {
    }

    // This method 'unpacks' the parcel with data
    private VoucherItem(Parcel in) {
        // Urutan penulisan variable harus sama dengan function writeToParcel(Parcel dest, int flags)!
        voucherId = in.readInt();
        voucherName = in.readString();
        voucherDesc = in.readString();
        voucherImgUrl = in.readString();
        voucherImgThumbUrl = in.readString();
        voucherExpiredDate = in.readString();
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getStock_available() {
        return stock_available;
    }

    public void setStock_available(Integer stock_available) {
        this.stock_available = stock_available;
    }

    public String getDetailslink() {
        return detailslink;
    }

    public void setDetailslink(String detailslink) {
        this.detailslink = detailslink;
    }

    public String getTermslink() {
        return termslink;
    }

    public void setTermslink(String termslink) {
        this.termslink = termslink;
    }

    public String getContent_source_url() {
        return content_source_url;
    }

    public void setContent_source_url(String content_source_url) {
        this.content_source_url = content_source_url;
    }

    public String getContent_source_type_name() {
        return content_source_type_name;
    }

    public void setContent_source_type_name(String content_source_type_name) {
        this.content_source_type_name = content_source_type_name;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherUrl() {
        return voucherUrl;
    }

    public void setVoucherUrl(String voucherUrl) {
        this.voucherUrl = voucherUrl;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherDesc() {
        return voucherDesc;
    }

    public void setVoucherDesc(String voucherDesc) {
        this.voucherDesc = voucherDesc;
    }

    public String getVoucherImgUrl() {
        return voucherImgUrl;
    }

    public void setVoucherImgUrl(String voucherImgUrl) {
        this.voucherImgUrl = voucherImgUrl;
    }

    public String getVoucherImgThumbUrl() {
        return voucherImgThumbUrl;
    }

    public void setVoucherImgThumbUrl(String voucherImgThumbUrl) {
        this.voucherImgThumbUrl = voucherImgThumbUrl;
    }

    public String getVoucherExpiredDate() {
        return voucherExpiredDate;
    }

    public void setVoucherExpiredDate(String voucherExpiredDate) {
        this.voucherExpiredDate = voucherExpiredDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(voucherId);
        dest.writeString(voucherUrl);
        dest.writeString(voucherName);
        dest.writeString(voucherDesc);
        dest.writeString(voucherImgUrl);
        dest.writeString(voucherImgThumbUrl);
        dest.writeString(voucherExpiredDate);
    }
}
