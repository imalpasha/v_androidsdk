package com.vav.cn.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.gcm.GcmConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Handrata Samsul on 1/12/2016.
 */
public class PreferenceManagerCustom {
    private static final String TAG_LOG = PreferenceManagerCustom.class.getSimpleName();
    private static final String APP_SHARED_PREFS = "VavPreferences";
    private static final String FILE_DIR_PATH = "FILE_DIR_PATH"; //internal / external
    private static final String USER_IS_LOGGED_IN = "USER_IS_LOGGED_IN";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_HASHED_PASSWORD = "USER_HASHED_PASSWORD";
    private static final String USER_PHONE = "USER_PHONE";
    private static final String USER_PHOTO = "USER_PHOTO";
    private static final String USER_VAV_COUNTER = "USER_VAV_COUNTER";
    private static final String USER_GUID = "USER_GUID";
    private static final String USER_AUTH_TOKEN = "USER_AUTH_TOKEN";
    private static final String USER_LAST_LOGIN_DATE = "USER_LAST_LOGIN_DATE";
    private static final String USER_PROFILE_PICT_PATH = "USER_PROFILE_PICT_PATH";
    private static final String IS_WENT_THROUGH_SPLASH = "IS_WENT_THROUGH_SPLASH";
    private static final String SEARCH_KEYWORDS = "SEARCH_KEYWORDS";
    private static final String THIRD_PARTY_APPS = "THIRD_PARTY_APPS";
    private static final String GCM_TOKEN = "GCM_TOKEN";


    private static final String VOUCHER_SORT_BY_TYPE = "VOUCHER_SORT_BY_TYPE";
    private static PreferenceManagerCustom instance;
    private final SharedPreferences appSharedPrefs;
    private final SharedPreferences.Editor prefsEditor;

    private PreferenceManagerCustom() {
        this.appSharedPrefs = ApplicationHelper.getInstance().getApplication()
                .getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public static PreferenceManagerCustom getInstance() {
        if (instance == null) {
            instance = new PreferenceManagerCustom();
        }
        return instance;
    }

    public void clearPreferences() {
        prefsEditor.clear();
        prefsEditor.commit();
        // setDefaultPreferences();
    }

    public String getFileDirPath() {
        return appSharedPrefs.getString(FILE_DIR_PATH, "");
    }

    public void setFileDirPath(String fileDirPath) {
        prefsEditor.putString(FILE_DIR_PATH, fileDirPath);
        prefsEditor.commit();
    }

    public boolean getUserIsLoggedIn() {
        return appSharedPrefs.getBoolean(USER_IS_LOGGED_IN, false);
    }

    public void setUserIsLoggedIn(boolean isLoggedIn) {
        prefsEditor.putBoolean(USER_IS_LOGGED_IN, isLoggedIn);
        prefsEditor.commit();
    }

    public String getUserName() {
        return appSharedPrefs.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        prefsEditor.putString(USER_NAME, userName);
        prefsEditor.commit();
    }

    public String getUserEmail() {
        return appSharedPrefs.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        prefsEditor.putString(USER_EMAIL, userEmail);
        prefsEditor.commit();
    }

    public String getUserPhoto() {
        return appSharedPrefs.getString(USER_PHOTO, "");
    }

    public void setUserPhoto(String userPhoto) {
        prefsEditor.putString(USER_PHOTO, userPhoto);
        prefsEditor.commit();
    }

    public String getUserHashedPassword() {
        return appSharedPrefs.getString(USER_HASHED_PASSWORD, "");
    }

    public void setUserHashedPassword(String userHashedPassword) {
        prefsEditor.putString(USER_HASHED_PASSWORD, userHashedPassword);
        prefsEditor.commit();
    }

    public String getUserPhone() {
        return appSharedPrefs.getString(USER_PHONE, "");
    }

    public void setUserPhone(String userPhone) {
        prefsEditor.putString(USER_PHONE, userPhone);
        prefsEditor.commit();
    }

    public int getUserVavCounter() {
        return appSharedPrefs.getInt(USER_VAV_COUNTER, 0);
    }

    public void setUserVavCounter(int userVavCounter) {
        prefsEditor.putInt(USER_VAV_COUNTER, userVavCounter);
        prefsEditor.commit();
    }

    public Set<String> getSearchKeywords() {
        return appSharedPrefs.getStringSet(SEARCH_KEYWORDS, new HashSet<String>());
    }

    public void setSearchKeywords(Set<String> searchKeywords) {
        prefsEditor.putStringSet(SEARCH_KEYWORDS, searchKeywords);
        prefsEditor.commit();
    }

    public String getUserGuid() {
        return appSharedPrefs.getString(USER_GUID, "");
    }

    public void setUserGuid(String userGuid) {
        prefsEditor.putString(USER_GUID, userGuid);
        prefsEditor.commit();
    }

    public String getUserAuthToken() {
        return appSharedPrefs.getString(USER_AUTH_TOKEN, "");
    }

    public void setUserAuthToken(String userAuthToken) {
        prefsEditor.putString(USER_AUTH_TOKEN, userAuthToken);
        prefsEditor.commit();
    }

    public Long getUserLastLoginDateTimeMillis() {
        return appSharedPrefs.getLong(USER_LAST_LOGIN_DATE, 0);
    }

    public void setUserLastLoginDateTimeMillis(Long userLastLoginDate) {
        prefsEditor.putLong(USER_LAST_LOGIN_DATE, userLastLoginDate);
        prefsEditor.commit();
    }

    public String getUserProfilePictPath() {
        return appSharedPrefs.getString(USER_PROFILE_PICT_PATH, "");
    }

    public void setUserProfilePictPath(String userProfilePictPath) {
        prefsEditor.putString(USER_PROFILE_PICT_PATH, userProfilePictPath);
        prefsEditor.commit();
        Logger.getInstance().d(TAG_LOG, "save profile pict :" + userProfilePictPath);
    }

    public boolean getSentTokenToServer() {
        return appSharedPrefs.getBoolean(GcmConfig.SENT_TOKEN_TO_SERVER, false);
    }

    public void setSentTokenToServer(boolean sentTokenToServer) {
        prefsEditor.putBoolean(GcmConfig.SENT_TOKEN_TO_SERVER, sentTokenToServer);
        prefsEditor.apply();
    }

    public boolean getIsWentThroughSplash() {
        return appSharedPrefs.getBoolean(IS_WENT_THROUGH_SPLASH, false);
    }

    public void setIsWentThroughSplash(boolean isWentThroughSplash) {
        prefsEditor.putBoolean(IS_WENT_THROUGH_SPLASH, isWentThroughSplash);
        prefsEditor.commit();
    }

    public String getGcmToken() {
        return appSharedPrefs.getString(GCM_TOKEN, null);
    }

    public void setGcmToken(String gcmToken) {
        prefsEditor.putString(GCM_TOKEN, gcmToken);
        prefsEditor.commit();
    }

    public Boolean getThirdPartyApps() {
        return appSharedPrefs.getBoolean(THIRD_PARTY_APPS, false);
    }

    public void setThirdPartyApps(Boolean thirdPartyApps) {
        prefsEditor.putBoolean(THIRD_PARTY_APPS, thirdPartyApps);
        prefsEditor.commit();
    }

    public VOUCHER_SORT_TYPE getVoucherSortByType() {
        int savedPref = appSharedPrefs.getInt(VOUCHER_SORT_BY_TYPE, 0);
        return VOUCHER_SORT_TYPE.values()[savedPref];

    }

    public void setVoucherSortByType(VOUCHER_SORT_TYPE voucherSortByType) {
        prefsEditor.putInt(VOUCHER_SORT_BY_TYPE, voucherSortByType.ordinal());
        prefsEditor.commit();
    }


    public enum VOUCHER_SORT_TYPE {
        ALPHABET, EXPIRE_DATE
    }
}
