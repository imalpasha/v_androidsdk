package com.vav.cn.directhelper;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.RequestBody;
import com.vav.cn.callback.UnInitializedExeception;
import com.vav.cn.config.Config;
import com.vav.cn.fragment.VavFragment;
import com.vav.cn.fragment.VoucherListFragment;
import com.vav.cn.listener.BaseCallback;
import com.vav.cn.listener.LoginCallback;
import com.vav.cn.listener.LogoutCallback;
import com.vav.cn.listener.OnCloseFragmentListener;
import com.vav.cn.listener.OnLoginCallbackSuccess;
import com.vav.cn.model.DeploymentType;
import com.vav.cn.model.ErrorInfo;
import com.vav.cn.model.GeneralResponse;
import com.vav.cn.server.ServerManager;
import com.vav.cn.server.ServerManagerCustom;
import com.vav.cn.server.model.AuthData;
import com.vav.cn.util.PreferenceManagerCustom;

/**
 * Created by thunde91 on 4/14/16.
 */
public class VavDirectGenerator {
    private static VavDirectGenerator instance = new VavDirectGenerator();
    private OnLoginCallbackSuccess currentLoginCallbackSuccess;
    private OnCloseFragmentListener onCloseFragmentListener;
    private String mServerUrl = Config.SERVER_API_URL_DEVELOPMENT;
    private volatile boolean initialized;
    private DeploymentType deploymentType;

    private VavDirectGenerator() {
    }

    public static VavDirectGenerator getInstance() {
        if (instance == null || !instance.initialized) {
            throw new UnInitializedExeception();
        }
        return instance;
    }
//
//    private void directToLoginPage(FragmentTransaction ft, int viewID, OnLoginCallbackSuccess onLoginCallbackSuccess) {
//        SignUpFragment signUpFragment = new SignUpFragment();
//        signUpFragment.setOnLoginCallbackSuccess(onLoginCallbackSuccess);
//        ft.replace(viewID, signUpFragment, VoucherListFragment.TAG_LOG);
//        //ft.addToBackStack(null);
//        ft.commitAllowingStateLoss();
//    }


    public static void initialize(DeploymentType deploymentType) {
        instance = new VavDirectGenerator(deploymentType);
        instance.initialized = true;
    }

    private VavDirectGenerator(DeploymentType deploymentType) {
        this.deploymentType = deploymentType;
        switch (deploymentType) {
            case DEVELOPMENT:
                mServerUrl = Config.SERVER_API_URL_DEVELOPMENT;
                ServerManager.getInstance().setmServerUrl(mServerUrl);
                break;
            case PRODUCTION:
                mServerUrl = Config.SERVER_API_URL_STAGING;
                ServerManager.getInstance().setmServerUrl(mServerUrl);
                break;
        }
    }

    public Fragment goToVoucherBook(FragmentTransaction ft, int viewID, OnCloseFragmentListener onCloseFragmentListener) throws Exception {
        PreferenceManagerCustom.getInstance().setThirdPartyApps(true);
        this.onCloseFragmentListener = onCloseFragmentListener;
        if (PreferenceManagerCustom.getInstance().getUserIsLoggedIn()) {
            VoucherListFragment voucherFragment = new VoucherListFragment();
            ft.replace(viewID, voucherFragment, VoucherListFragment.TAG_LOG);
            //ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
            return voucherFragment;
        } else {
            throw new Exception("You must login first before using this menu");
        }
    }

    private void setOnLoginCallback(OnLoginCallbackSuccess onLoginCallbackSuccess) {
        this.currentLoginCallbackSuccess = onLoginCallbackSuccess;
    }

    public Fragment goToVavingFragment(final FragmentTransaction ft, final int viewID, OnCloseFragmentListener onCloseFragmentListener) throws Exception {
        this.onCloseFragmentListener = onCloseFragmentListener;
        PreferenceManagerCustom.getInstance().setThirdPartyApps(true);
        if (PreferenceManagerCustom.getInstance().getUserIsLoggedIn()) {
            VavFragment vavFragment = new VavFragment();
            ft.add(viewID, vavFragment, VavFragment.TAG_LOG);
            //ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
            return vavFragment;
        } else {
            throw new Exception("You must login first before using this menu");
        }
    }

    public void goToLoginPage() {
        if (currentLoginCallbackSuccess != null) {
            currentLoginCallbackSuccess.onLoginCallback(false, null);
        }
    }

    public void closeFragment() {
        if (onCloseFragmentListener != null) {
            onCloseFragmentListener.onCloseFragment();
        }
    }

    /**
     * Login to VAV server
     *
     * @param authData      Authentication data
     * @param loginCallback Callback when the login process is successful or failed
     */
    public void login(AuthData authData, final LoginCallback loginCallback) {
        BaseCallback baseCallback = new BaseCallback() {
            @Override
            public void onRequestSuccess(GeneralResponse generalResponse) {
                if (!ServerManagerCustom.getInstance().isError(generalResponse.getStatus())) {
                    PreferenceManagerCustom.getInstance().setUserAuthToken(generalResponse.getAuth_token());
                    PreferenceManagerCustom.getInstance().setUserGuid(generalResponse.getGuid());
                    PreferenceManagerCustom.getInstance().setUserIsLoggedIn(true);
                    loginCallback.onLoginSuccess();
                } else {
                    loginCallback.onLoginFailure(new ErrorInfo(generalResponse.getStatus(), generalResponse.getMessage()));
                }
            }

            @Override
            public void onRequestFailure(ErrorInfo errorInfo) {
                loginCallback.onLoginFailure(errorInfo);
            }
        };
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_LOGIN;

        Headers headers = new Headers.Builder()
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .add(Config.SERVER_API_EXT_AUTH_SOURCE, Config.COMPANY_NAME)
                .add(Config.SERVER_API_EXT_AUTH_TOKEN, authData.getAuthToken())
                .add(Config.SERVER_API_EXT_AUTH_ID, authData.getGuid())
                .add(Config.SERVER_API_REFERRAL_FROM, authData.getReferralCode())
                .add(Config.SERVER_API_APP_COMPANY_ID, String.valueOf(Config.COMPANY_ID))
                .add(Config.SERVER_API_SOURCE_GROUP_CODE, Config.COMPANY_NAME)
                .build();

        ServerManagerCustom.getInstance().sendRequestPost(requestUrl, headers, requestBody, baseCallback);
    }

    /**
     * Logout from VAV server
     *
     * @param logoutCallback
     */
    public void logout(final LogoutCallback logoutCallback) {
        BaseCallback baseCallback = new BaseCallback() {
            @Override
            public void onRequestSuccess(GeneralResponse generalResponse) {
                if (!ServerManagerCustom.getInstance().isError(generalResponse.getStatus())) {
                    PreferenceManagerCustom.getInstance().setUserIsLoggedIn(false);
                    PreferenceManagerCustom.getInstance().clearPreferences();
                    logoutCallback.onLogoutSuccess();
                } else {
                    logoutCallback.onLogoutFailure(new ErrorInfo(generalResponse.getStatus(), generalResponse.getMessage()));
                }
            }

            @Override
            public void onRequestFailure(ErrorInfo errorInfo) {
                logoutCallback.onLogoutFailure(errorInfo);
            }
        };
        String requestUrl = mServerUrl + "/" + Config.SERVER_API_APP_LOGIN + "/" + PreferenceManagerCustom.getInstance().getUserGuid();

        Headers headers = new Headers.Builder()
                .add(Config.SERVER_API_ACCEPT, Config.SERVER_API_ACCEPT_VALUE)
                .add(Config.SERVER_API_APP_AUTHORIZATION, "Token " + PreferenceManagerCustom.getInstance().getUserAuthToken())
                .build();

        RequestBody requestBody = new FormEncodingBuilder()
                .build();

        ServerManagerCustom.getInstance().sendRequestDelete(requestUrl, headers, requestBody, baseCallback);
    }

    public boolean isUserLogin(){
        return PreferenceManagerCustom.getInstance().getUserIsLoggedIn();
    }
}
