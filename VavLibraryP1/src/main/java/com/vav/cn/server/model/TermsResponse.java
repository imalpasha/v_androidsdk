package com.vav.cn.server.model;

/**
 * Created by thunde91 on 3/5/16.
 */
public class TermsResponse {
    private Integer offerid;
    private String merchanturl;
    private String offerurl;
    private String terms;
    private String status;

    public Integer getOfferid() {
        return offerid;
    }

    public void setOfferid(Integer offerid) {
        this.offerid = offerid;
    }

    public String getMerchanturl() {
        return merchanturl;
    }

    public void setMerchanturl(String merchanturl) {
        this.merchanturl = merchanturl;
    }

    public String getOfferurl() {
        return offerurl;
    }

    public void setOfferurl(String offerurl) {
        this.offerurl = offerurl;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
