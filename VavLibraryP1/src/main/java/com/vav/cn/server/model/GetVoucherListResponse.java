package com.vav.cn.server.model;

import java.util.List;

/**
 * Created by Handrata Samsul on 1/27/2016.
 */
public class GetVoucherListResponse {
    private int page;
    private int items_per_page;
    private int total_items;
    private String status;
    private String message;
    private List<Surprise> surprise;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItems_per_page() {
        return items_per_page;
    }

    public void setItems_per_page(int items_per_page) {
        this.items_per_page = items_per_page;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
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

    public List<Surprise> getSurprise() {
        return surprise;
    }

    public void setSurprise(List<Surprise> surprise) {
        this.surprise = surprise;
    }

    public class Surprise {
        private int offerid;
        private String offerurl;
        private String merchantname;
        private String deals;
        private String offerimg;
        private String thumbnail;
        private String expire;
        private String detailslink;
        private String termslink;
        private String content_source_type_name;
        private String content_source_url;
        private Integer stock_available;
        private String distance;

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

        public String getContent_source_type_name() {
            return content_source_type_name;
        }

        public void setContent_source_type_name(String content_source_type_name) {
            this.content_source_type_name = content_source_type_name;
        }

        public String getContent_source_url() {
            return content_source_url;
        }

        public void setContent_source_url(String content_source_url) {
            this.content_source_url = content_source_url;
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

        public int getOfferid() {
            return offerid;
        }

        public void setOfferid(int offerid) {
            this.offerid = offerid;
        }

        public String getOfferurl() {
            return offerurl;
        }

        public void setOfferurl(String offerurl) {
            this.offerurl = offerurl;
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

        public String getOfferimg() {
            return offerimg;
        }

        public void setOfferimg(String offerimg) {
            this.offerimg = offerimg;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }
    }
}
