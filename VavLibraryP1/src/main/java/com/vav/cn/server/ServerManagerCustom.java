package com.vav.cn.server;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vav.cn.config.Config;
import com.vav.cn.listener.BaseCallback;
import com.vav.cn.model.ErrorInfo;
import com.vav.cn.model.GeneralResponse;
import com.vav.cn.util.GsonHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

/**
 * Created by thunde91 on 8/11/16.
 */
public class ServerManagerCustom {
    private static final String TAG_LOG = ServerManagerCustom.class.getSimpleName();
    private static ServerManagerCustom instance = new ServerManagerCustom();

    public static ServerManagerCustom getInstance() {
        return instance;
    }

    private ServerManagerCustom() {
    }

    public void sendRequestPost(String requestUrl, Headers headers, RequestBody requestBody, final BaseCallback baseCallback) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .headers(headers)
                .post(requestBody)
                .build();

        callingConnection(baseCallback, client, request);
    }

    public void sendRequestGet(String requestUrl, Headers headers, final BaseCallback baseCallback) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .headers(headers)
                .build();

        callingConnection(baseCallback, client, request);
    }

    private void callingConnection(final BaseCallback baseCallback, OkHttpClient client, Request request) {
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (request.body() != null) {
                    Log.d(TAG_LOG, "Request:" + request.body().toString() + " Exception:" + e.getMessage());
                } else {
                    Log.d(TAG_LOG, "Request: null, Exception:" + e.getMessage());
                }
                baseCallback.onRequestFailure(new ErrorInfo("error", "BAD_REQUEST"));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    GeneralResponse generalResponse = null;
                    try{
                        generalResponse = GsonHelper.getInstance().parseJSONString(jsonData, GeneralResponse.class);
                    }catch (Exception e){
                        Log.e(TAG_LOG,"Error while converting" +e.getMessage());
                    }
                    Log.d(TAG_LOG, "Response:" + jsonData);

                    if (response.isSuccessful()) {
                        baseCallback.onRequestSuccess(generalResponse);
                    } else {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            baseCallback.onRequestFailure(new ErrorInfo("error", "UNAUTHORIZED"));
                        } else if (response.code() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                            baseCallback.onRequestFailure(new ErrorInfo("error", "BAD_REQUEST"));
                        } else {
                            baseCallback.onRequestFailure(new ErrorInfo("error", "BAD_REQUEST"));
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG_LOG, e.toString());
                }
            }
        });
    }

    public void sendRequestDelete(String requestUrl, Headers headers, RequestBody requestBody, BaseCallback baseCallback) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Config.CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(requestUrl)
                .delete(requestBody)
                .headers(headers)
                .build();

        callingConnection(baseCallback, client, request);
    }

    public boolean isError(String status) {
        return status.equals("error");
    }
}
