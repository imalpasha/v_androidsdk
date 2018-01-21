package com.vav.cn.config;

/**
 * Created by Handrata Samsul on 1/11/2016.
 */
public class Config {
    public static String SERVER_API_URL_DEVELOPMENT = "http://dev.vavapps.com/api";
    public final static String SERVER_API_URL_STAGING = "http://dev.vavapps.com/api";
    public final static String SERVER_API_APP_USER = "app_users";
    public final static String SERVER_API_APP_LOGIN = "login";
    public final static String SERVER_API_APP_USER_NAME = "app_user[name]";
    public final static String SERVER_API_APP_PHONE_NUMBER = "app_user[phone]";
    public final static String SERVER_API_APP_USER_EMAIL = "app_user[email]";
    public final static String SERVER_API_APP_USER_PASSWORD = "app_user[password]";
    public final static String SERVER_API_APP_USER_PHOTO = "app_user[photo]";
    public final static String SERVER_API_APP_AUTHORIZATION = "Authorization";
    public final static String SERVER_API_ACCEPT = "accept";
    public final static String SERVER_API_ACCEPT_VALUE = "application/vnd.audaxy.com.v1";
    public final static String SERVER_API_HOME = "home";
    public final static String SERVER_API_CONTENT_TYPE = "Content-Type";
    public final static String SERVER_API_CONTENT_JSON = "application/json";
    public final static String SERVER_API_LATITUDE = "latitude";
    public final static String SERVER_API_LONGITUDE = "longitude";
    public final static String SERVER_API_DISTANCE = "distance";
    public final static String SERVER_API_COMPANY_ID = "company_id";
    public final static String SERVER_API_MERCHANT_PIN = "merchant_pin";
    public final static String SERVER_API_OFFERID = "offerid";
    public final static String SERVER_API_PAGE_NUMBER = "page";
    public final static String SERVER_API_VAVING = "vaving";
    public final static String SERVER_API_VOUCHERS = "vouchers";
    public static final String SERVER_API_SEARCH_BY_NAME = "search_by_name";
    public static final String SERVER_API_NO_HFS = "nohfs";
    public final static String SERVER_API_GCM_TOKEN = "app_user[gcm_token]";
    public final static String SERVER_API_PLATFORM = "app_user[platform]";
    public static final String PLATFORM = "android";
    public static final String SERVER_API_GCM_REGIS = "gcm_registrations";
    public final static int HOME_LISTING_DISTANCE = 1000;

    /* VAVING */
    public final static long VAVING_TIMER_IN_MILLISECONDS = 3000;
    public final static long VAVING_EMPTY_ID_TIMER_IN_MILLISECONDS = 5000; // *empty timer must bigger than regular timer

    /* USE VOUCHER */
    public final static long USE_VOUCHER_TIMER_IN_MILLISECONDS = 60000;

    public final static String SERVER_ERROR_CODE = "500";
    public final static int COMPANY_ID = 5;
    public final static int CONNECTION_TIMEOUT_IN_SECONDS = 10;

    public final static String PROFILE_LAST_LOGIN_DATE_FORMAT = "dd MMM yyyy";
    public final static String SEARCH_EXPIRE_DATE_FORMAT = "dd MMM yyyy";

    public final static String FONT_PRIMARY = "fzy3jw";
    public final static String FONT_STYLE_PRIMARY = "normal";
    public final static String FONT_STYLE_BOLD = "bold";
    public static final int MAX_LOG_FILE_CHAR = 2000;

    public static final int SELECT_PHOTO_GALLERY_CODE = 100;
    public static final int SELECT_PHOTO_CAMERA_CODE = 101;
    public static final int REQUEST_PERMISSION_GALLERY = 252;
    public static final int REQUEST_PERMISSION_CAMERA = 253;
    public static final int REQUEST_PERMISSION_RECORD_AUDIO = 254;
    public static final int REQUEST_PERMISSION_LOCATION = 255;

    public static final int CROP_IMAGE_CODE = 200;
    public static final String IMAGE_DIR = ".image";
    public static final String LOG_FILE = "log";
    public static final String USER_PROFILE_PICT_FILE_NAME = "user_pp";
    public static final String TEMP_FILE_NAME = "temp";

    public static final String COMPANY_NAME = "MyDigi";

    public final static String SERVER_API_EXT_AUTH_SOURCE = "app_user[ext_auth_source]";
    public final static String SERVER_API_EXT_AUTH_TOKEN = "app_user[ext_auth_token]";
    public final static String SERVER_API_EXT_AUTH_ID = "app_user[ext_auth_id]";
    public final static String SERVER_API_REFERRAL_FROM = "app_user[referral_from]";
    public final static String SERVER_API_SOURCE_GROUP_CODE = "app_user[source_group_code]";
    public final static String SERVER_API_APP_COMPANY_ID = "app_user[company_id]";


}
