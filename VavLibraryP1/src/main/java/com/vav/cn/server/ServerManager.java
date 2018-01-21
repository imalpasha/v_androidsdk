package com.vav.cn.server;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vav.cn.R;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.server.listener.ServerManagerListener;
import com.vav.cn.server.model.DeleteVoucherResponse;
import com.vav.cn.server.model.GeneralResponse;
import com.vav.cn.server.model.GetDetailResponse;
import com.vav.cn.server.model.GetHomeListResponse;
import com.vav.cn.server.model.GetProfileResponse;
import com.vav.cn.server.model.GetVoucherListResponse;
import com.vav.cn.server.model.LoginResponse;
import com.vav.cn.server.model.LogoutResponse;
import com.vav.cn.server.model.SaveVoucherResponse;
import com.vav.cn.server.model.SignUpResponse;
import com.vav.cn.server.model.TermsResponse;
import com.vav.cn.server.model.UpdateProfileResponse;
import com.vav.cn.server.model.UseVoucherResponse;
import com.vav.cn.server.model.VavingResponse;
import com.vav.cn.util.DialogFragmentListenerHandler;
import com.vav.cn.util.Logger;
import com.vav.cn.util.PreferenceManagerCustom;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Handrata Samsul on 1/14/2016.
 */
public class ServerManager {
    private static final String TAG_LOG = ServerManager.class.getSimpleName();
    private static ServerManager instance = new ServerManager();
    private String mServerUrl = Config.SERVER_API_URL_DEVELOPMENT;

    public void setmServerUrl(String mServerUrl) {
        this.mServerUrl = mServerUrl;
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public void doNormalSignUp(String name, String email, String password, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doNormalSignUp Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER;

        Headers headers = new Headers.Builder()
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_APP_USER_NAME, name)
                .add(Config.SERVER_API_APP_USER_EMAIL, email)
                .add(Config.SERVER_API_APP_USER_PASSWORD, password)
                .build();

        sendRequestPost(REQUEST_TYPE.SIGNUP, requestUrl, headers, requestBody, listener);
    }

    public void doNormalLogIn(String email, String password, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doNormalLogIn Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_LOGIN;

        Headers headers = new Headers.Builder()
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_APP_USER_EMAIL, email)
                .add(Config.SERVER_API_APP_USER_PASSWORD, password)
                .build();

        sendRequestPost(REQUEST_TYPE.LOGIN, requestUrl, headers, requestBody, listener);
    }

    public void doSendToken(String authToken, String token, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doSendToken Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_GCM_REGIS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_GCM_TOKEN, token)
                .add(Config.SERVER_API_PLATFORM, Config.PLATFORM)
                .build();

        sendRequestPost(REQUEST_TYPE.SEND_TOKEN, requestUrl, headers, requestBody, listener);
    }

    public void doLogOut(String guId, String authToken, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doLogOut Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_LOGIN + "/" + guId;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .build();

        sendRequestDelete(REQUEST_TYPE.LOGOUT, requestUrl, headers, requestBody, listener);
    }

    public void doGetProfile(String authToken, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doGetProfile Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER + "/" + PreferenceManagerCustom.getInstance().getUserVavCounter();

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_CONTENT_TYPE, Config.SERVER_API_CONTENT_JSON)
                .build();
        sendRequestGet(REQUEST_TYPE.GET_PROFILE, requestUrl, headers, listener);
    }

    public void doUpdateProfileName(String authToken, String name, ServerManagerListener listener) {
        Log.d(TAG_LOG, "updateProfileName Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER + "/";

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_CONTENT_TYPE, Config.SERVER_API_CONTENT_JSON)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_APP_USER_NAME, name)
                .build();

        sendRequestPatch(REQUEST_TYPE.UPDATE_PROFILE, requestUrl, headers, requestBody, listener);
    }

    public void doUpdatePhoneNumber(String authToken, String phoneNumber, ServerManagerListener listener) {
        Log.d(TAG_LOG, "updateProfileName Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER + "/";

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_APP_PHONE_NUMBER, phoneNumber)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_CONTENT_TYPE, Config.SERVER_API_CONTENT_JSON)
                .build();

        sendRequestPatch(REQUEST_TYPE.UPDATE_PROFILE, requestUrl, headers, requestBody, listener);
    }

    public void doUpdateProfilePhoto(String authToken, File file, ServerManagerListener listener) {
        Log.d(TAG_LOG, "updateProfileName Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER + "/";

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_CONTENT_TYPE, Config.SERVER_API_CONTENT_JSON)
                .build();

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart(Config.SERVER_API_APP_USER_PHOTO, file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();

        sendRequestPatch(REQUEST_TYPE.UPDATE_PROFILE, requestUrl, headers, requestBody, listener);
    }

    public void doUpdateProfileEmail(String authToken, String email, ServerManagerListener listener) {
        Log.d(TAG_LOG, "updateProfileEmail Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER + "/";

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_CONTENT_TYPE, Config.SERVER_API_CONTENT_JSON)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_APP_USER_EMAIL, email)
                .build();

        sendRequestPatch(REQUEST_TYPE.UPDATE_PROFILE, requestUrl, headers, requestBody, listener);
    }

    public void doUpdateProfilePassword(String authToken, String password, ServerManagerListener listener) {
        Log.d(TAG_LOG, "updateProfilePassword Method running");
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_USER + "/";

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_CONTENT_TYPE, Config.SERVER_API_CONTENT_JSON)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_APP_USER_PASSWORD, password)
                .build();

        sendRequestPatch(REQUEST_TYPE.UPDATE_PROFILE, requestUrl, headers, requestBody, listener);
    }

    public void doGetHomeList(String authToken, double latitude, double longitude, int distance, int companyId, int pageNumber, ServerManagerListener listener) {
        Log.d(TAG_LOG, "getHomeListing Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_HOME;

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Config.SERVER_API_LATITUDE, latitude + "")
                .appendQueryParameter(Config.SERVER_API_LONGITUDE, longitude + "")
                .appendQueryParameter(Config.SERVER_API_DISTANCE, distance + "")
                .appendQueryParameter(Config.SERVER_API_COMPANY_ID, companyId + "")
                .appendQueryParameter(Config.SERVER_API_PAGE_NUMBER, pageNumber + "");

        requestUrl += "?" + builder.build().getEncodedQuery();

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();

        sendRequestGet(REQUEST_TYPE.GET_HOME_LIST, requestUrl, headers, listener);
    }

    public void doGetMerchantSearch(String authToken, double latitude, double longitude, int distance, int companyId, int pageNumber, String searchKeyword, ServerManagerListener listener) {
        Log.d(TAG_LOG, "getHomeListing Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_HOME;

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Config.SERVER_API_SEARCH_BY_NAME, searchKeyword + "")
                .appendQueryParameter(Config.SERVER_API_LATITUDE, latitude + "")
                .appendQueryParameter(Config.SERVER_API_LONGITUDE, longitude + "")
                .appendQueryParameter(Config.SERVER_API_DISTANCE, distance + "")
                .appendQueryParameter(Config.SERVER_API_COMPANY_ID, companyId + "")
                .appendQueryParameter(Config.SERVER_API_PAGE_NUMBER, pageNumber + "");

        requestUrl += "?" + builder.build().getEncodedQuery();

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();

        sendRequestGet(REQUEST_TYPE.SEARCH_MERCHANT, requestUrl, headers, listener);
    }

    public void doGetMerchantDetails(String authToken, String detailsLink, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doGetMerchantDetails Method running");

        String requestUrl = detailsLink;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();

        sendRequestGet(REQUEST_TYPE.GET_DETAIL, requestUrl, headers, listener);
    }

    public void doGetVoucherList(String authToken, ServerManagerListener listener, double latitude, double longitude, int distance, int pageNumber) {
        Log.d(TAG_LOG, "doGetVoucherList Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VOUCHERS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .build();
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Config.SERVER_API_LATITUDE, latitude + "")
                .appendQueryParameter(Config.SERVER_API_LONGITUDE, longitude + "")
                .appendQueryParameter(Config.SERVER_API_DISTANCE, distance + "")
                .appendQueryParameter(Config.SERVER_API_PAGE_NUMBER, pageNumber + "");

        requestUrl += "?" + builder.build().getEncodedQuery();
        sendRequestGet(REQUEST_TYPE.GET_VOUCHERS, requestUrl, headers, listener);
    }

    public void doRequestVoucher(String authToken, int offerId, double latitude, double longitude, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doUseVoucher Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VOUCHERS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_OFFERID, offerId + "")
                .add(Config.SERVER_API_LATITUDE, latitude + "")
                .add(Config.SERVER_API_LONGITUDE, longitude + "")
                .build();
        DialogFragmentListenerHandler.getInstance().setOfferID(offerId);
        sendRequestPatch(REQUEST_TYPE.REQUEST_VOUCHER, requestUrl, headers, requestBody, listener);
    }

    public void doUseVoucher(String authToken, int offerId, double latitude, double longitude, String merchantPIN, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doUseVoucher Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VOUCHERS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_OFFERID, offerId + "")
                .add(Config.SERVER_API_LATITUDE, latitude + "")
                .add(Config.SERVER_API_LONGITUDE, longitude + "")
                .add(Config.SERVER_API_MERCHANT_PIN, merchantPIN + "")
                .build();
        DialogFragmentListenerHandler.getInstance().setOfferID(offerId);
        sendRequestPatch(REQUEST_TYPE.USE_VOUCHER, requestUrl, headers, requestBody, listener);
    }

    public void doSaveVoucher(String authToken, int offerId, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doSaveVoucher Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VOUCHERS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_OFFERID, offerId + "")
                .build();

        sendRequestPost(REQUEST_TYPE.SAVE_VOUCHER, requestUrl, headers, requestBody, listener);

    }

    public void doDeleteSavedVoucher(String authToken, int offerId, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doGetVoucherList Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VOUCHERS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_OFFERID, offerId + "")
                .build();

        sendRequestDelete(REQUEST_TYPE.DELETE_VOUCHER, requestUrl, headers, requestBody, listener);
    }

    public void doRequestTermsAndCondition(String authToken, String requestUrl, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doGetVoucherList Method running");

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        sendRequestGet(REQUEST_TYPE.TERMS_CONDITION, requestUrl, headers, listener);
    }

    public void doVaving(String authToken, String ultraSoundId, ServerManagerListener listener, int companyId, double latitude, double longitude, int distance) {
        Log.d(TAG_LOG, "doVaving Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VAVING + "/" + ultraSoundId;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Config.SERVER_API_LATITUDE, latitude + "")
                .appendQueryParameter(Config.SERVER_API_LONGITUDE, longitude + "")
                .appendQueryParameter(Config.SERVER_API_DISTANCE, distance + "")
                .appendQueryParameter(Config.SERVER_API_COMPANY_ID, companyId + "");

        requestUrl += "?" + builder.build().getEncodedQuery();


        sendRequestGet(REQUEST_TYPE.VAVING, requestUrl, headers, listener);
    }

    public void doVavingLocation(String authToken, double latitude, double longitude, int distance, int companyId, ServerManagerListener listener) {
        Log.d(TAG_LOG, "doVavingLocation Method running");

        String requestUrl = mServerUrl + "/" + Config.SERVER_API_VAVING + "/" + Config.SERVER_API_NO_HFS;

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + authToken)
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .build();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(Config.SERVER_API_LATITUDE, latitude + "")
                .appendQueryParameter(Config.SERVER_API_LONGITUDE, longitude + "")
                .appendQueryParameter(Config.SERVER_API_DISTANCE, distance + "")
                .appendQueryParameter(Config.SERVER_API_COMPANY_ID, companyId + "");

        requestUrl += "?" + builder.build().getEncodedQuery();

        sendRequestGet(REQUEST_TYPE.VAVING, requestUrl, headers, listener);
    }

    private synchronized void sendRequestGet(final REQUEST_TYPE requestType, String requestUrl, Headers headers, final ServerManagerListener mServerManagerListener) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .headers(headers)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (request.body() != null) {
                    Logger.getInstance().d(TAG_LOG, "Request:" + request.body().toString() + " Exception:" + e.getMessage());
                } else {
                    Logger.getInstance().d(TAG_LOG, "Request: null, Exception:" + e.getMessage());
                }
                mServerManagerListener.onFailCallback(
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_title),
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_message), false);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    Gson gson = new Gson();
                    String jsonData = response.body().string();
                    Log.d(TAG_LOG, "Response:" + jsonData);

                    if (response.isSuccessful()) {
                        switch (requestType) {
                            case GET_PROFILE:
                                System.out.println("GET PROFILE HERE");
                                GetProfileResponse getProfileResponse = gson.fromJson(jsonData, GetProfileResponse.class);
                                if (getProfileResponse.getStatus().equals("success")) {
                                    mServerManagerListener.onGetProfileCallback(getProfileResponse, true);
                                } else {
                                    mServerManagerListener.onGetProfileCallback(getProfileResponse, false);
                                }
                                break;
                            case GET_HOME_LIST:
                                GetHomeListResponse getHomeListResponse = gson.fromJson(jsonData, GetHomeListResponse.class);
                                mServerManagerListener.onGetHomeListCallback(getHomeListResponse, true);
                                break;
                            case GET_DETAIL:
                                GetDetailResponse getDetailResponse = gson.fromJson(jsonData, GetDetailResponse.class);
                                mServerManagerListener.onGetDetailCallback(getDetailResponse, true);
                                break;
                            case GET_VOUCHERS:
                                GetVoucherListResponse getVoucherListResponse = gson.fromJson(jsonData, GetVoucherListResponse.class);
                                mServerManagerListener.onGetVoucherListCallback(getVoucherListResponse, true);
                                break;
                            case VAVING:
                                VavingResponse vavingResponse = gson.fromJson(jsonData, VavingResponse.class);

                                if (vavingResponse.getStatus().equals("success")) {
                                    mServerManagerListener.onVavingCallback(vavingResponse, true);
                                } else {
                                    if (vavingResponse.getStatus().equals("warning")) {
                                        mServerManagerListener.onVavingCallback(vavingResponse, false);
                                    }
                                }
                                break;
                            case SEARCH_MERCHANT:
                                GetHomeListResponse getSearchMerchantResponse = gson.fromJson(jsonData, GetHomeListResponse.class);
                                mServerManagerListener.onGetHomeListCallback(getSearchMerchantResponse, true);
                                break;
                            case TERMS_CONDITION:
                                TermsResponse termsResponse = gson.fromJson(jsonData, TermsResponse.class);
                                mServerManagerListener.onTermsConditionCallback(termsResponse, true);
                                break;
                        }
                    } else {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), true);
                        } else if (response.code() > HttpURLConnection.HTTP_BAD_REQUEST) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), false);
                        } else {
                            GeneralResponse generalResponse = gson.fromJson(jsonData, GeneralResponse.class);
                            mServerManagerListener.onFailCallback(generalResponse.getStatus(), generalResponse.getMessage(), false);
                        }
                    }
                } catch (IOException e) {
                    Logger.getInstance().e(TAG_LOG, e.toString());
                }
            }
        });
    }

    private void sendRequestPost(final REQUEST_TYPE requestType, String requestUrl, Headers headers, RequestBody requestBody, final ServerManagerListener mServerManagerListener) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .headers(headers)
                .post(requestBody)
                .build();

        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (request.body() != null) {
                    Logger.getInstance().d(TAG_LOG, "Request:" + request.body().toString() + " Exception:" + e.getMessage());
                } else {
                    Logger.getInstance().d(TAG_LOG, "Request: null, Exception:" + e.getMessage());
                }
                mServerManagerListener.onFailCallback(
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_title),
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_message), false);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    Gson gson = new Gson();
                    String jsonData = response.body().string();
                    Log.d(TAG_LOG, "Response:" + jsonData);

                    if (response.isSuccessful()) {
                        switch (requestType) {
                            case SIGNUP:
                                SignUpResponse signUpResponse = gson.fromJson(jsonData, SignUpResponse.class);
                                mServerManagerListener.onSignUpCallback(signUpResponse, true);
                                break;
                            case LOGIN:
                                LoginResponse loginResponse = gson.fromJson(jsonData, LoginResponse.class);
                                mServerManagerListener.onLoginCallback(loginResponse, true);
                                break;
                            case SAVE_VOUCHER:
                                SaveVoucherResponse saveVoucherResponse = gson.fromJson(jsonData, SaveVoucherResponse.class);
                                mServerManagerListener.onSaveVoucherCallback(saveVoucherResponse, true);
                                break;
                            case SEND_TOKEN:
                                GeneralResponse generalResponse = gson.fromJson(jsonData, GeneralResponse.class);
                                mServerManagerListener.onSendTokenCallback(generalResponse != null && generalResponse.getStatus().equals("success"));
                                break;
                        }
                    } else {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), true);
                        } else if (response.code() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), false);
                        } else {
                            GeneralResponse generalResponse = gson.fromJson(jsonData, GeneralResponse.class);
                            mServerManagerListener.onFailCallback(generalResponse.getStatus(), generalResponse.getMessage(), false);
                        }
                    }
                } catch (IOException e) {
                    Logger.getInstance().e(TAG_LOG, e.toString());
                }
            }
        });
    }

    private void sendRequestPatch(final REQUEST_TYPE requestType, String requestUrl, Headers headers, RequestBody requestBody, final ServerManagerListener mServerManagerListener) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .headers(headers)
                .patch(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (request.body() != null) {
                    Logger.getInstance().d(TAG_LOG, "Request:" + request.body().toString() + " Exception:" + e.getMessage());
                } else {
                    Logger.getInstance().d(TAG_LOG, "Request: null, Exception:" + e.getMessage());
                }
                mServerManagerListener.onFailCallback(
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_title),
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_message), false);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    Gson gson = new Gson();
                    String jsonData = response.body().string();
                    Log.d(TAG_LOG, "Response:" + jsonData);

                    if (response.isSuccessful()) {
                        switch (requestType) {
                            case UPDATE_PROFILE:
                                UpdateProfileResponse updateProfileResponse = gson.fromJson(jsonData, UpdateProfileResponse.class);
                                mServerManagerListener.onUpdateProfileCallback(updateProfileResponse, true);
                                break;
                            case USE_VOUCHER:
                                UseVoucherResponse useVoucherResponse = gson.fromJson(jsonData, UseVoucherResponse.class);
                                useVoucherResponse.setIsRequestVoucher(false);
                                mServerManagerListener.onUseVoucherCallback(useVoucherResponse, true);
                                break;
                            case REQUEST_VOUCHER:
                                UseVoucherResponse useVoucherResponse1 = gson.fromJson(jsonData, UseVoucherResponse.class);
                                useVoucherResponse1.setIsRequestVoucher(true);
                                mServerManagerListener.onUseVoucherCallback(useVoucherResponse1, true);
                                break;
                        }
                    } else {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), true);
                        } else if (response.code() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), false);
                        } else {
                            GeneralResponse generalResponse = gson.fromJson(jsonData, GeneralResponse.class);
                            mServerManagerListener.onFailCallback(generalResponse.getStatus(), generalResponse.getMessage(), false);
                        }
                    }
                } catch (IOException e) {
                    Logger.getInstance().e(TAG_LOG, e.toString());
                }
            }
        });
    }

    private void sendRequestDelete(final REQUEST_TYPE requestType, String requestUrl, Headers headers, RequestBody requestBody, final ServerManagerListener mServerManagerListener) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .delete(requestBody)
                .headers(headers)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (request.body() != null) {
                    Logger.getInstance().d(TAG_LOG, "Request:" + request.body().toString() + " Exception:" + e.getMessage());
                } else {
                    Logger.getInstance().d(TAG_LOG, "Request: null, Exception:" + e.getMessage());
                }
                mServerManagerListener.onFailCallback(
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_title),
                        ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_send_request_failed_message), false);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    Gson gson = new Gson();
                    String jsonData = response.body().string();
                    Log.d(TAG_LOG, "Response:" + jsonData);

                    if (response.isSuccessful()) {
                        switch (requestType) {
                            case LOGOUT:
                                LogoutResponse logoutResponse = gson.fromJson(jsonData, LogoutResponse.class);
                                mServerManagerListener.onLogoutCallback(logoutResponse, true);
                                break;
                            case DELETE_VOUCHER:
                                DeleteVoucherResponse deleteVoucherResponse = gson.fromJson(jsonData, DeleteVoucherResponse.class);
                                mServerManagerListener.onDeleteVoucherCallback(deleteVoucherResponse, true);
                                break;
                        }
                    } else {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), true);
                        } else if (response.code() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                            mServerManagerListener.onFailCallback(
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_title) + " " + response.code(),
                                    ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message), false);
                        } else {
                            GeneralResponse generalResponse = gson.fromJson(jsonData, GeneralResponse.class);
                            mServerManagerListener.onFailCallback(generalResponse.getStatus(), generalResponse.getMessage(), false);
                        }
                    }
                } catch (IOException e) {
                    Logger.getInstance().e(TAG_LOG, e.toString());
                }
            }
        });
    }

    public enum REQUEST_TYPE {
        SIGNUP, LOGIN, LOGOUT, GET_PROFILE, UPDATE_PROFILE, GET_HOME_LIST, GET_DETAIL, GET_VOUCHERS, SAVE_VOUCHER, USE_VOUCHER, DELETE_VOUCHER, VAVING, SEARCH_MERCHANT, TERMS_CONDITION, REQUEST_VOUCHER, SEND_TOKEN
    }
}
