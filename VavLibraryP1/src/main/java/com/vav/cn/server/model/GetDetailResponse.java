package com.vav.cn.server.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/26/2016.
 */
public class GetDetailResponse implements Parcelable {
    public static final Creator<GetDetailResponse> CREATOR = new Creator<GetDetailResponse>() {
        @Override
        public GetDetailResponse createFromParcel(Parcel source) {
            return new GetDetailResponse(source);
        }

        @Override
        public GetDetailResponse[] newArray(int size) {
            return new GetDetailResponse[size];
        }
    };
    private int id;
    private String address;
    private String location;
    private String website;
    private String facebook;
    private String twitter;
    private String instagram;
    private String merchantname;
    private double latitude;
    private double longitude;
    private String tel;
    private String coverimg;
    private String caticon_url;
    private String caticon;
    private String distance;
    private String promo_text;
    private List<Offer> offers;
    private List<Gallery> gallery;
    private String status;
    private String operating_days;
    private String opening_time;
    private String closing_time;

    public GetDetailResponse() {
    }

    // This method 'unpacks' the parcel with data
    private GetDetailResponse(Parcel in) {
        // Urutan penulisan variable harus sama dengan function writeToParcel(Parcel dest, int flags)!
        id = in.readInt();
        address = in.readString();
        location = in.readString();
        website = in.readString();
        facebook = in.readString();
        twitter = in.readString();
        instagram = in.readString();
        merchantname = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        tel = in.readString();
        coverimg = in.readString();
        caticon_url = in.readString();
        caticon = in.readString();
        distance = in.readString();
        offers = new ArrayList<Offer>();
        in.readList(offers, Offer.class.getClassLoader());
        gallery = new ArrayList<Gallery>();
        in.readList(gallery, Gallery.class.getClassLoader());
        status = in.readString();
    }

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getOperating_days() {
        return operating_days;
    }

    public void setOperating_days(String operating_days) {
        this.operating_days = operating_days;
    }

    public String getPromo_text() {
        return promo_text;
    }

    public void setPromo_text(String promo_text) {
        this.promo_text = promo_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(List<Gallery> gallery) {
        this.gallery = gallery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(address);
        dest.writeString(location);
        dest.writeString(website);
        dest.writeString(facebook);
        dest.writeString(twitter);
        dest.writeString(instagram);
        dest.writeString(merchantname);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(tel);
        dest.writeString(coverimg);
        dest.writeString(caticon_url);
        dest.writeString(caticon);
        dest.writeString(distance);
        dest.writeList(offers);
        dest.writeList(gallery);
        dest.writeString(status);
    }

    public static class Offer implements Parcelable {
        public static final Creator<Offer> CREATOR = new Creator<Offer>() {
            @Override
            public Offer createFromParcel(Parcel source) {
                return new Offer(source);
            }

            @Override
            public Offer[] newArray(int size) {
                return new Offer[size];
            }
        };
        private int offerid;
        private String offerurl;
        private String desc;
        private String expire;
        private String offerimg;
        private String thumbnail;
        private String content_source_type_name;

        public Offer() {
        }

        // This method 'unpacks' the parcel with data
        private Offer(Parcel in) {
            // Urutan penulisan variable harus sama dengan function writeToParcel(Parcel dest, int flags)!
            offerid = in.readInt();
            offerurl = in.readString();
            desc = in.readString();
            expire = in.readString();
            offerimg = in.readString();
            thumbnail = in.readString();
            content_source_type_name = in.readString();
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
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
            dest.writeInt(offerid);
            dest.writeString(offerurl);
            dest.writeString(desc);
            dest.writeString(expire);
            dest.writeString(offerimg);
            dest.writeString(thumbnail);
            dest.writeString(content_source_type_name);
        }


    }

    public static class Gallery implements Parcelable {
        public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
            @Override
            public Gallery createFromParcel(Parcel source) {
                return new Gallery(source);
            }

            @Override
            public Gallery[] newArray(int size) {
                return new Gallery[size];
            }
        };
        private String imageurl;

        public Gallery() {
        }

        // This method 'unpacks' the parcel with data
        private Gallery(Parcel in) {
            // Urutan penulisan variable harus sama dengan function writeToParcel(Parcel dest, int flags)!
            imageurl = in.readString();
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imageurl);
        }
    }
}
