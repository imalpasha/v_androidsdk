package com.vav.cn.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.dialog.CustomDialog;
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

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by Calvin on 1/28/2016.
 */
public class DialogVoucherResponseFragment extends DialogFragment implements ServerManagerListener {
    public static final String LOG_TAG = DialogVoucherResponseFragment.class.getSimpleName();
    public static final String CANCELED_ON_TOUCH_OUTSIDE = "CANCELED_ON_TOUCH_OUTSIDE";
    public static final String RESPONSE_TEXT = "RESPONSE_TEXT";
    public static final String TAG_PARAM_SERIAL_NO = "TAG_PARAM_SERIAL_NO";
    public static final String TAG_PARAM_OFFER_ID = "TAG_PARAM_OFFER_ID";
    public static final String TAG_PARAM_SUCCESS_ENTRY = "TAG_PARAM_SUCCESS_ENTRY";
    public static final String TAG_FOOTER_HIDE = "TAG_FOOTER_HIDE";
    private static DialogVoucherResponseFragment instance = new DialogVoucherResponseFragment();
    private boolean mCanceledOnTouchOutside;
    private String serialNo;
    private Integer offeriD;
    private boolean successEntry;
    private TextView textViewResponse;
    private ImageView imageViewClose;
    private String responseText;
    private TextView textViewVoucherCode;
    private CustomDialog mProgressDialog;
    private boolean isNeedHide;
    private View footer;

    public static DialogVoucherResponseFragment getInstance() {
        return instance;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mCanceledOnTouchOutside = args.getBoolean(CANCELED_ON_TOUCH_OUTSIDE, false);
        responseText = args.getString(RESPONSE_TEXT, "");
        serialNo = args.getString(TAG_PARAM_SERIAL_NO, "");
        offeriD = args.getInt(TAG_PARAM_OFFER_ID, -1);
        successEntry = args.getBoolean(TAG_PARAM_SUCCESS_ENTRY, false);
        isNeedHide = args.getBoolean(TAG_FOOTER_HIDE, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.voucher_response_fragment, null);

        textViewResponse = (TextView) view.findViewById(R.id.textviewResponse);
        textViewVoucherCode = (TextView) view.findViewById(R.id.textviewVoucherCode);
        imageViewClose = (ImageView) view.findViewById(R.id.closeImageView);
        imageViewClose.setFocusable(true);
        imageViewClose.setClickable(true);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRemoveSuccessDeals();
            }
        });
        if (successEntry && !serialNo.equals("")) {
            textViewVoucherCode.setText(MessageFormat.format("{0}\n{1}", getString(R.string.voucher_code), serialNo));
        } else {
            textViewVoucherCode.setVisibility(View.GONE);
        }
        footer = view.findViewById(R.id.footer);
        if (isNeedHide) {
            footer.setVisibility(View.GONE);
        }
        textViewResponse.setText(responseText);
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        return dialog;
    }

    private void checkAndRemoveSuccessDeals() {
        DialogFragmentListenerHandler.getInstance().publishToListener(successEntry, offeriD);
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkAndRemoveSuccessDeals();
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
    public void onUseVoucherCallback(final UseVoucherResponse useVoucherResponse, boolean success) {
        onItemsLoadComplete();
        if (success) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), useVoucherResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                        DialogVoucherResponseFragment.this.getActivity(),
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
