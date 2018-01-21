package com.vav.cn.server.model;

import java.util.List;

/**
 * Created by Handrata Samsul on 2/1/2016.
 */
public class VavingResponse {
    private List<Offer> offers;
    private String status;
    private String message;

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Offer {
        private int offerid;
        private String merchantname;
        private String deals;
        private String offerurl;
        private String offerimg;
        private String content_source_type_name;
        private String content_source_url;
        private String detailslink;
        private String termslink;
        private Integer stock_available;

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

        public int getOfferid() {
            return offerid;
        }

        public void setOfferid(int offerid) {
            this.offerid = offerid;
        }

        public String getMerchantname() {
            return merchantname;
        }

        public void setMerchantname(String merchantname) {
            this.merchantname = merchantname;
        }

        public String getDeals() {
            return deals;
        }

        public void setDeals(String deals) {
            this.deals = deals;
        }

        public String getOfferurl() {
            return offerurl;
        }

        public void setOfferurl(String offerurl) {
            this.offerurl = offerurl;
        }

        public String getOfferimg() {
            return offerimg;
        }

        public void setOfferimg(String offerimg) {
            this.offerimg = offerimg;
        }

        public String getContent_source_type_name() {
            return content_source_type_name;
        }

        public void setContent_source_type_name(String content_source_type_name) {
            this.content_source_type_name = content_source_type_name;
        }
    }
}
