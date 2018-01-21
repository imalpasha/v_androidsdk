package com.vav.cn.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.config.Config;
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
import com.vav.cn.util.DialogFragmentListenerHandler;
import com.vav.cn.util.DialogHelper;
import com.vav.cn.util.DirectToSplashScreenHelper;
import com.vav.cn.util.LocationHelper;
import com.vav.cn.util.Logger;
import com.vav.cn.util.PreferenceManagerCustom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/28/2016.
 */
public class DialogVoucherUseFragment extends DialogFragment implements TextWatcher, ServerManagerListener {
    public static final String LOG_TAG = DialogVoucherUseFragment.class.getSimpleName();
    public static final String CANCELED_ON_TOUCH_OUTSIDE = "CANCELED_ON_TOUCH_OUTSIDE";
    public static final String TAG_PARAM_OFFER_ID = "TAG_PARAM_OFFER_ID";
    public static final String TAG_PARAM_SERIAL_NO = "TAG_PARAM_SERIAL_NO";
    public static final String TAG_FOOTER_HIDE = "TAG_FOOTER_HIDE";
    private static DialogVoucherUseFragment instance = new DialogVoucherUseFragment();
    private boolean mCanceledOnTouchOutside;
    private int mOfferId;
    private String serialNo;
    private ViewGroup mViewGroupDialogContainer;
    private List<EditText> mTxtInputList;
    private EditText mTxtInput1;
    private EditText mTxtInput2;
    private EditText mTxtInput3;
    private EditText mTxtInput4;
    private EditText mTxtInput5;
    private EditText mTxtInput6;
    private TextView mBtnConfirm;
    private TextView mBtnCancel;
    private boolean mInputAddMode;
    private boolean footerNeedHide;
    private TextView mLblVoucherTimer;
    private CountDownTimer mCountDownTimer;
    private CustomDialog mProgressDialog;
    private View viewFooter;

    public static DialogVoucherUseFragment getInstance() {
        return instance;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mCanceledOnTouchOutside = args.getBoolean(CANCELED_ON_TOUCH_OUTSIDE, false);
        mOfferId = args.getInt(TAG_PARAM_OFFER_ID, -1);
        serialNo = args.getString(TAG_PARAM_SERIAL_NO, "");
        footerNeedHide = args.getBoolean(TAG_FOOTER_HIDE, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.voucher_use_fragment, null);

        mLblVoucherTimer = (TextView) view.findViewById(R.id.lblVoucherTimer);
        mCountDownTimer = new CountDownTimer(Config.USE_VOUCHER_TIMER_IN_MILLISECONDS, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = Math.round((float) millisUntilFinished / 1000);
                mLblVoucherTimer.setText("" + seconds);
            }

            @Override
            public void onFinish() {
                mLblVoucherTimer.setText("0");
                mBtnCancel.performClick();
            }
        }.start();

        mViewGroupDialogContainer = (ViewGroup) view.findViewById(R.id.viewGroupDialogContainer);
        mViewGroupDialogContainer.setFocusable(true);
        mViewGroupDialogContainer.setClickable(true);
        if (mCanceledOnTouchOutside)
            mViewGroupDialogContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCountDownTimer.cancel();
                    dismiss();
                }
            });

        mTxtInputList = new ArrayList<>();
        mTxtInputList.add(mTxtInput1 = (EditText) view.findViewById(R.id.txtInput1));
        mTxtInputList.add(mTxtInput2 = (EditText) view.findViewById(R.id.txtInput2));
        mTxtInputList.add(mTxtInput3 = (EditText) view.findViewById(R.id.txtInput3));
        mTxtInputList.add(mTxtInput4 = (EditText) view.findViewById(R.id.txtInput4));
        mTxtInputList.add(mTxtInput5 = (EditText) view.findViewById(R.id.txtInput5));
        mTxtInputList.add(mTxtInput6 = (EditText) view.findViewById(R.id.txtInput6));

        mTxtInput1.addTextChangedListener(this);
        mTxtInput2.addTextChangedListener(this);
        mTxtInput3.addTextChangedListener(this);
        mTxtInput4.addTextChangedListener(this);
        mTxtInput5.addTextChangedListener(this);
        mTxtInput6.addTextChangedListener(this);

        mBtnConfirm = (TextView) view.findViewById(R.id.btnConfirm);
        mBtnConfirm.setFocusable(true);
        mBtnConfirm.setClickable(true);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasEmptyInput(mTxtInputList)) {
                    //check to server valid code
                    mCountDownTimer.cancel();
                    mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

                    LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
                        @Override
                        public void gotLocation(final Location location) {
                            String merchantPin = "";
                            for (EditText editText : mTxtInputList) {
                                merchantPin = merchantPin + editText.getText();
                            }
                            ServerManager.getInstance().doUseVoucher(
                                    PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                    mOfferId, location.getLatitude(), location.getLongitude(), merchantPin,
                                    DialogVoucherUseFragment.this
                            );
                        }

                        @Override
                        public void onFailCallback(String message) {
                            Logger.getInstance().e(LOG_TAG, "Getting location failed:" + message);
                        }

                        @Override
                        public void onRequestTurnGPS() {
                            LocationHelper.getInstance().requestUserPermissionTurnGPS(getActivity());
                        }

                    });
                    //show loading indicator

                    //send data to server


                } else {
                    Toast.makeText(getActivity(), getString(R.string.voucher_use_confirm_has_empty_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtnCancel = (TextView) view.findViewById(R.id.btnCancel);
        mBtnCancel.setFocusable(true);
        mBtnCancel.setClickable(true);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.cancel();
                dismiss();
            }
        });
        viewFooter = view.findViewById(R.id.footer);
        if (footerNeedHide) {
            viewFooter.setVisibility(View.GONE);
        }

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }

    public boolean hasEmptyInput(List<EditText> txtInputList) {
        for (int i = 0; i < txtInputList.size(); i++) {
            if (TextUtils.isEmpty(txtInputList.get(i).getText())) return true;
        }
        return false;
    }

    private void onItemsLoadComplete() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //dismiss loading indicator
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.length() == 0) mInputAddMode = true;
        else mInputAddMode = false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //if user input something, move the focus to next text field
        if (mInputAddMode) {
            for (int i = 0; i < mTxtInputList.size() - 1; i++) {
                if (s == mTxtInputList.get(i).getEditableText()) {
                    if (s.length() > 0 && mTxtInputList.get(i + 1).getText().length() == 0) {
                        mTxtInputList.get(i + 1).requestFocus();
                        break;
                    }
                }
            }
        } else {
            for (int i = 1; i < mTxtInputList.size(); i++) {
                if (s == mTxtInputList.get(i).getEditableText()) {
                    if (s.length() == 0 && mTxtInputList.get(i - 1).getText().length() > 0) {
                        mTxtInputList.get(i - 1).requestFocus();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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

    }

    @Override
    public void onUseVoucherCallback(final UseVoucherResponse useVoucherResponse, final boolean success) {
        onItemsLoadComplete();
        if (success) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (success) {
                        dismiss();
                        DialogHelper.getInstance().showDialogVoucherResponseFragment(
                                getActivity(),
                                serialNo, useVoucherResponse.getMessage(),
                                useVoucherResponse.getStatus().equals("success"), DialogFragmentListenerHandler.getInstance().getOfferID(),
                                false, footerNeedHide
                        );
                    }
                }
            });
        }
    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        onItemsLoadComplete();
        if (getActivity() == null) {
            return;
        }
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(getActivity());
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        DialogVoucherUseFragment.this.getActivity(),
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
