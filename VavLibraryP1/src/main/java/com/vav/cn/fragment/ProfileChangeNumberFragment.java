package com.vav.cn.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.dialog.CustomDialog;
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
import com.vav.cn.util.PreferenceManagerCustom;

/**
 * Created by Calvin on 1/20/2016.
 */
public class ProfileChangeNumberFragment extends Fragment implements ServerManagerListener {
    public static final String TAG_LOG = ProfileChangeNumberFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;

    private EditText mTxtNewNumber;

    private TextView mBtnChange;

    private CustomDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.profile_change_number, container, false);

        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderLeft.setImageResource(R.drawable.header_btn_back);
        mBtnHeaderLeft.setVisibility(View.VISIBLE);
        mBtnHeaderLeft.setFocusable(true);
        mBtnHeaderLeft.setClickable(true);
        mBtnHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment;
                GeneralUtil.getInstance().closeSoftKeyboard(getActivity());
                fragment = fm.findFragmentByTag(TAG_LOG);
                if (fragment instanceof ProfileChangeNumberFragment) {
                    ft.remove(fragment);
                    ft.commit();
                }
            }
        });

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.profile_change_number);

        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);

        mTxtNewNumber = (EditText) view.findViewById(R.id.txtNewNumber);
        mBtnChange = (TextView) view.findViewById(R.id.btnChange);
        mBtnChange.setFocusable(true);
        mBtnChange.setClickable(true);
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mTxtNewNumber.getText().toString();
                if (!GeneralUtil.isValidPhoneNumber(phoneNumber)) {
                    DialogHelper.getInstance().showSingleButtonBasicDialog(
                            getActivity(),
                            getString(R.string.dialog_sign_up_phone_number_too_short_title),
                            getString(R.string.dialog_sign_up_phone_number_too_short_content),
                            getString(R.string.dialog_sign_up_phone_number_too_short_button));
                    mTxtNewNumber.requestFocus();
                } else {
                    //All is valid
                    mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());
                    //update profile data
                    ServerManager.getInstance().doUpdatePhoneNumber(PreferenceManagerCustom.getInstance().getUserAuthToken(), phoneNumber, ProfileChangeNumberFragment.this);
                }
            }
        });

        return view;
    }

    @Override
    public void onSignUpCallback(SignUpResponse signUpResponse, boolean success) {

    }

    @Override
    public void onLoginCallback(LoginResponse loginResponse, boolean success) {

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

    }

    @Override
    public void onGetVoucherListCallback(GetVoucherListResponse getVoucherListResponse, boolean success) {

    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {
        mProgressDialog.dismiss();
        if (success) {
            PreferenceManagerCustom.getInstance().setUserPhone(updateProfileResponse.getPhone());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentManager fm = getActivity().getFragmentManager();
                    Fragment fragment;
                    fragment = fm.findFragmentByTag(ProfileFragment.TAG_LOG);
                    if (fragment instanceof ProfileFragment) {
                        ((ProfileFragment) fragment).getLblUserName().setText(PreferenceManagerCustom.getInstance().getUserName());
                    }
                    mBtnHeaderLeft.performClick();
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplicationHelper.getInstance().getApplication(), "Update user profile failed..", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(getActivity());
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        ProfileChangeNumberFragment.this.getActivity(),
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