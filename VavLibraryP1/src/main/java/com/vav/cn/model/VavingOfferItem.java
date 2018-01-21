package com.vav.cn.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Handrata Samsul on 2/1/2016.
 */
public class VavingOfferItem implements Parcelable {
    public static final Creator<VavingOfferItem> CREATOR = new Creator<VavingOfferItem>() {
        @Override
        public VavingOfferItem createFromParcel(Parcel source) {
            return new VavingOfferItem(source);
        }

        @Override
        public VavingOfferItem[] newArray(int size) {
            return new VavingOfferItem[size];
        }
    };
    private int offerId;
    private String offerMerchantName;
    private String offerDesc;
    private String offerUrl;
    private String offerImgUrl;
    private String content_source_type_name;
    private String content_source_url;
    private String detailslink;
    private String termslink;
    private Integer stock_available;

    public VavingOfferItem() {
    }

    // This method 'unpacks' the parcel with data
    private VavingOfferItem(Parcel in) {
        // Urutan penulisan variable harus sama dengan function writeToParcel(Parcel dest, int flags)!
        offerId = in.readInt();
        offerMerchantName = in.readString();
        offerDesc = in.readString();
        offerUrl = in.readString();
        offerImgUrl = in.readString();
        content_source_type_name = in.readString();
    }

    public Integer getStock_available() {
        return stock_available;
    }

    public void setStock_available(Integer stock_available) {
        this.stock_available = stock_available;
    }

    public String getTermslink() {
        return termslink;
    }

    public void setTermslink(String termslink) {
        this.termslink = termslink;
    }

    public String getDetailslink() {
        return detailslink;
    }

    public void setDetailslink(String detailslink) {
        this.detailslink = detailslink;
    }

    public String getContent_source_url() {
        return content_source_url;
    }

    public void setContent_source_url(String content_source_url) {
        this.content_source_url = content_source_url;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getOfferMerchantName() {
        return offerMerchantName;
    }

    public void setOfferMerchantName(String offerMerchantName) {
        this.offerMerchantName = offerMerchantName;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getOfferUrl() {
        return offerUrl;
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public String getOfferImgUrl() {
        return offerImgUrl;
    }

    public void setOfferImgUrl(String offerImgUrl) {
        this.offerImgUrl = offerImgUrl;
    }

    public String getContent_source_type_name() {
        return content_source_type_name;
    }

    public void setContent_source_type_name(String content_source_type_name) {
        this.content_source_type_name = content_source_type_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(offerId);
        dest.writeString(offerMerchantName);
        dest.writeString(offerDesc);
        dest.writeString(offerUrl);
        dest.writeString(offerImgUrl);
        dest.writeString(content_source_type_name);
    }
}
