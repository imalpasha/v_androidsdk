package com.vav.cn.server.model;

import java.util.List;

/**
 * Created by Handrata Samsul on 1/25/2016.
 */
public class GetHomeListResponse {
    private int page;
    private int items_per_page;
    private int total_items;
    private List<Surprise> surprise;
    private String status;
    private String message;

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

    public List<Surprise> getSurprise() {
        return surprise;
    }

    public void setSurprise(List<Surprise> surprise) {
        this.surprise = surprise;
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

    public class Surprise {
        private String merchantname;
        private String coverimg;
        private String caticon_url;
        private String caticon;
        private String detailslink;
        private String promo_text;
        private String deals;
        private String content_source_type_name;
        private String distance;
        private String location;

        public String getPromo_text() {
            return promo_text;
        }

        public void setPromo_text(String promo_text) {
            this.promo_text = promo_text;
        }

        public String getMerchantname() {
            return merchantname;
        }

        public void setMerchantname(String merchantname) {
            this.merchantname = merchantname;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getCaticon_url() {
            return caticon_url;
        }

        public void setCaticon_url(String caticon_url) {
            this.caticon_url = caticon_url;
        }

        public String getCaticon() {
            return caticon;
        }

        public void setCaticon(String caticon) {
            this.caticon = caticon;
        }

        public String getDetailslink() {
            return detailslink;
        }

        public void setDetailslink(String detailslink) {
            this.detailslink = detailslink;
        }

        public String getDeals() {
            return deals;
        }

        public void setDeals(String deals) {
            this.deals = deals;
        }

        public String getContent_source_type_name() {
            return content_source_type_name;
        }

        public void setContent_source_type_name(String content_source_type_name) {
            this.content_source_type_name = content_source_type_name;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
