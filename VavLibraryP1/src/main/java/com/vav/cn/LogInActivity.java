package com.vav.cn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.listener.DialogTwoButtonListener;
import com.vav.cn.server.ServerManager;
import com.vav.cn.server.listener.ServerManagerListener;
import com.vav.cn.server.model.DeleteVoucherResponse;
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
import com.vav.cn.util.DialogHelper;
import com.vav.cn.util.DirectToSplashScreenHelper;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.LoginCallbackHelper;
import com.vav.cn.util.PreferenceManagerCustom;

import java.util.Calendar;

/**
 * Created by Handrata Samsul on 1/11/2016.
 */
public class LogInActivity extends AppCompatActivity implements ServerManagerListener {

    private ImageView mBtnBack;
    private TextView mLblTitle;

    private TextView mTxtEmail;
    private TextView mTxtPassword;
    private TextView mBtnLogin;
    private TextView textViewNeedHelp;
    private CustomDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mBtnBack = (ImageView) findViewById(R.id.btnHeaderLeft);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnBack.setFocusable(true);
        mBtnBack.setClickable(true);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        textViewNeedHelp = (TextView) findViewById(R.id.btnNeedHelp);
        textViewNeedHelp.setFocusable(true);
        textViewNeedHelp.setClickable(true);
        textViewNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, TermsAndCondActivity.class);
                intent.putExtra(TermsAndCondActivity.URL_RESOURCE, getString(R.string.need_help_link));
                intent.putExtra(TermsAndCondActivity.TITLE_RESOURCE, getString(R.string.help_title));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mLblTitle = (TextView) findViewById(R.id.lblHeaderTitle);
        mLblTitle.setText(R.string.login_title);

        mTxtEmail = (TextView) findViewById(R.id.txtEmail);
        mTxtPassword = (TextView) findViewById(R.id.txtPassword);

        String email = PreferenceManagerCustom.getInstance().getUserEmail();
        if (!email.equals("")) {
            mTxtEmail.setText(email);
        }

        mBtnLogin = (TextView) findViewById(R.id.btnLogin);
        mBtnLogin.setFocusable(true);
        mBtnLogin.setClickable(true);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnLoginClicked();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void onBtnLoginClicked() {
        final String email = mTxtEmail.getText().toString();
        final String password = mTxtPassword.getText().toString();

        // check valid email, password, then check connectivity
        if (!GeneralUtil.isValidEmail(email)) {
            DialogHelper.getInstance().showSingleButtonBasicDialog(
                    this,
                    getString(R.string.dialog_sign_up_invalid_email_title),
                    getString(R.string.dialog_sign_up_invalid_email_content),
                    getString(R.string.dialog_sign_up_invalid_email_button));
            mTxtEmail.requestFocus();
        } else if (!GeneralUtil.isValidPassword(password)) {
            DialogHelper.getInstance().showSingleButtonBasicDialog(
                    this,
                    getString(R.string.dialog_sign_up_password_too_short_title),
                    getString(R.string.dialog_sign_up_password_too_short_content),
                    getString(R.string.dialog_sign_up_password_too_short_button));
            mTxtPassword.requestFocus();
        } else if (!GeneralUtil.getInstance().isOnline(this)) {
            DialogHelper.getInstance().showTwoButtonBasicDialog(
                    this,
                    getString(R.string.dialog_not_online_title),
                    getString(R.string.dialog_not_online_content),
                    getString(R.string.dialog_not_online_positive_button),
                    getString(R.string.dialog_not_online_negative_button),
                    new DialogTwoButtonListener() {
                        @Override
                        public void onBtnPositiveClick(DialogInterface dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onBtnNegativeClick(DialogInterface dialog) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    });
        } else {

/*			//BY PASS LOGIN
            PreferenceManagerCustom.getInstance().setUserName(GeneralUtil.randomString(10)+" "+GeneralUtil.randomString(10));
			PreferenceManagerCustom.getInstance().setUserEmail(email);
			PreferenceManagerCustom.getInstance().setUserLastLoginDateTimeMillis(Calendar.getInstance().getTimeInMillis());
			PreferenceManagerCustom.getInstance().setUserIsLoggedIn(true);
			GeneralUtil.getInstance().goToHomeMemberPage(LogInActivity.this);
*/


            //All is valid
            mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(this);

            String hashedPassword = GeneralUtil.hashPassword(password);
            PreferenceManagerCustom.getInstance().setUserHashedPassword(hashedPassword);

            ServerManager.getInstance().doNormalLogIn(email, password, LogInActivity.this);
        }
    }

    @Override
    public void onSignUpCallback(final SignUpResponse signUpResponse, boolean success) {

    }

    @Override
    public void onLoginCallback(final LoginResponse loginResponse, boolean success) {
        mProgressDialog.dismiss();
        if (success && loginResponse.getStatus().equals("success")) {
            PreferenceManagerCustom.getInstance().setUserGuid(loginResponse.getGuid());
            PreferenceManagerCustom.getInstance().setUserAuthToken(loginResponse.getAuthToken());
            PreferenceManagerCustom.getInstance().setUserLastLoginDateTimeMillis(Calendar.getInstance().getTimeInMillis());
            PreferenceManagerCustom.getInstance().setUserIsLoggedIn(true);

            LogInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplicationHelper.getInstance().getApplication(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (LoginCallbackHelper.getInstance().getOnLoginCallbackSuccess() != null) {
                        finish();
                        LoginCallbackHelper.getInstance().getOnLoginCallbackSuccess().onLoginCallback(true, null);
                    } else {
                        GeneralUtil.getInstance().goToHomeMemberPage(LogInActivity.this);
                    }
                    /*//get user profile data
                    ServerManager.getInstance().doGetProfile(PreferenceManagerCustom.getInstance().getUserAuthToken(), LogInActivity.this);*/
                }
            });
        } else {
            LogInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplicationHelper.getInstance().getApplication(), (loginResponse != null && loginResponse.getMessage() != null) ? loginResponse.getMessage() : "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onLogoutCallback(LogoutResponse logoutResponse, boolean success) {

    }

    @Override
    public void onDeleteVoucherCallback(DeleteVoucherResponse deleteVoucherResponse, boolean success) {

    }

    @Override
    public void onGetDetailCallback(GetDetailResponse getDetailResponse, boolean success) {

    }

    @Override
    public void onGetHomeListCallback(GetHomeListResponse getHomeListResponse, boolean success) {

    }

    @Override
    public void onGetProfileCallback(GetProfileResponse getProfileResponse, boolean success) {
        mProgressDialog.dismiss();
        if (success) {
            PreferenceManagerCustom.getInstance().setUserName(getProfileResponse.getName());
            PreferenceManagerCustom.getInstance().setUserEmail(getProfileResponse.getEmail());
            PreferenceManagerCustom.getInstance().setUserPhone(getProfileResponse.getPhone());
            PreferenceManagerCustom.getInstance().setUserVavCounter(getProfileResponse.getVav_counter());

            LogInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (LoginCallbackHelper.getInstance().getOnLoginCallbackSuccess() != null) {
                        LoginCallbackHelper.getInstance().getOnLoginCallbackSuccess().onLoginCallback(true, null);
                    } else {
                        GeneralUtil.getInstance().goToHomeMemberPage(LogInActivity.this);
                    }
                }
            });
        } else {
            LogInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplicationHelper.getInstance().getApplication(), "Get user profile failed..", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onGetVoucherListCallback(GetVoucherListResponse getVoucherListResponse, boolean success) {

    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {

    }

    @Override
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        mProgressDialog.dismiss();
        if (LogInActivity.this == null) {
            return;
        }
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(this);
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        LogInActivity.this,
                        status,
                        message,
                        true
                );
            }
        });
    }

    @Override
    public void onTermsConditionCallback(TermsResponse termsResponse, boolean isSuccess) {

    }

    @Override
    public void onSendTokenCallback(boolean isSuccess) {

    }
}
