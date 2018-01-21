package com.vav.cn.fragment;

import android.app.DialogFragment;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vav.cn.R;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.listener.DialogFragmentTwoButtonListener;
import com.vav.cn.listener.DialogFragmentVoucherListener;
import com.vav.cn.listener.UpdateableFragment;
import com.vav.cn.model.VoucherItem;
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
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.LocationHelper;
import com.vav.cn.util.Logger;
import com.vav.cn.util.PreferenceManagerCustom;

import java.text.MessageFormat;

/**
 * Created by Handrata Samsul on 1/22/2016.
 */
public class VoucherPagerItemFragment extends Fragment implements ServerManagerListener, UpdateableFragment, DialogFragmentVoucherListener {
    public static final String TAG_LOG = VoucherPagerItemFragment.class.getSimpleName();
    public static final String TAG_PARAM_VOUCHER_ITEM = "TAG_PARAM_VOUCHER_ITEM";
    public static final String TAG_PARAM_VOUCHER_PAGER_POSITION = "TAG_PARAM_VOUCHER_PAGER_POSITION";

    private VoucherItem mVoucherItem = new VoucherItem();
    private int mVoucherPagerPosition = 0;

    private TextView mLblVoucherName;
    private TextView mLblVoucherDesc;
    private ImageView mImgVoucher;
    private TextView mBtnUse;
    private TextView mBtnDiscard;
    private ImageView mImgMerchantInfo;
    private TextView textViewTerms;
    private TextView textViewStocks;

    private ProgressBar mProgressBar;
    private Boolean isUrl;
    private CustomDialog mProgressDialog;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mVoucherItem = args.getParcelable(TAG_PARAM_VOUCHER_ITEM);
        mVoucherPagerPosition = args.getInt(TAG_PARAM_VOUCHER_PAGER_POSITION, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.voucher_item_fragment, container, false);
        mLblVoucherName = (TextView) view.findViewById(R.id.lblVoucherName);
        mLblVoucherDesc = (TextView) view.findViewById(R.id.lblVoucherDesc);
        textViewStocks = (TextView) view.findViewById(R.id.textviewStock);
        mImgVoucher = (ImageView) view.findViewById(R.id.imgVoucher);
        mBtnUse = (TextView) view.findViewById(R.id.btnUse);
        mBtnDiscard = (TextView) view.findViewById(R.id.btnDiscard);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mImgMerchantInfo = (ImageView) view.findViewById(R.id.imgMerchantInfo);
        textViewTerms = (TextView) view.findViewById(R.id.lblTerms);
        mLblVoucherName.setText(mVoucherItem.getVoucherName());
        mLblVoucherDesc.setText(mVoucherItem.getVoucherDesc());
        isUrl = mVoucherItem.getContent_source_type_name() != null && mVoucherItem.getContent_source_type_name().equals("HTML");
        if (isUrl)
            mBtnUse.setText(R.string.textview_open);
        if (mVoucherItem.getStock_available() != null) {
            textViewStocks.setText(MessageFormat.format(getString(R.string.stock_left_avaiable), mVoucherItem.getStock_available()));
        } else {
            textViewStocks.setVisibility(View.GONE);
        }
        SpannableString content = new SpannableString(textViewTerms.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textViewTerms.setText(content);
        if ((mVoucherItem.getStock_available() != null && mVoucherItem.getStock_available() == 0) || (mVoucherItem.getVoucherExpiredDate() != null && GeneralUtil.getInstance().isDateExpired(mVoucherItem.getVoucherExpiredDate()))) {
            mBtnUse.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        if (mVoucherItem.getTermslink() == null)
            textViewTerms.setVisibility(View.INVISIBLE);
        textViewTerms.setFocusable(true);
        textViewTerms.setClickable(true);
        textViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerManager.getInstance().doRequestTermsAndCondition(
                        PreferenceManagerCustom.getInstance().getUserAuthToken(),
                        mVoucherItem.getTermslink(),
                        VoucherPagerItemFragment.this
                );
            }
        });
        mBtnUse.setFocusable(true);
        mBtnUse.setClickable(true);
        mBtnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isUrl) {
                    mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());
                    LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
                        @Override
                        public void gotLocation(final Location location) {

                            ServerManager.getInstance().doRequestVoucher(
                                    PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                    mVoucherItem.getVoucherId(), location.getLatitude(), location.getLongitude(),
                                    VoucherPagerItemFragment.this
                            );
                        }

                        @Override
                        public void onFailCallback(String message) {
                            mProgressDialog.dismiss();
                            Logger.getInstance().e(TAG_LOG, "Getting location failed:" + message);
                        }

                        @Override
                        public void onRequestTurnGPS() {
                            LocationHelper.getInstance().requestUserPermissionTurnGPS(getActivity());
                        }
                    });
                } else {
                    openWebviewFragment(mVoucherItem.getContent_source_url(), getString(R.string.details_title), null);
                }
            }
        });
        mBtnDiscard.setFocusable(true);
        mBtnDiscard.setClickable(true);
        mBtnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        getActivity(),
                        "",
                        getActivity().getString(R.string.dialog_voucher_delete_confirm_message),
                        getActivity().getString(R.string.dialog_yes_btn),
                        getActivity().getString(R.string.dialog_no_btn),
                        false,
                        new DialogFragmentTwoButtonListener() {
                            @Override
                            public void onBtnPositiveClick(DialogFragment dialogFragment) {
                                //delete voucher from server
                                ServerManager.getInstance().doDeleteSavedVoucher(
                                        PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                        mVoucherItem.getVoucherId(),
                                        VoucherPagerItemFragment.this
                                );

                                dialogFragment.dismiss();
                            }

                            @Override
                            public void onBtnNegativeClick(DialogFragment dialogFragment) {
                                dialogFragment.dismiss();
                            }
                        });
            }
        });

        mImgMerchantInfo.setFocusable(true);
        mImgMerchantInfo.setClickable(true);
        mImgMerchantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Log.d(TAG_LOG, "get merchant data:" + mVoucherItem.getDetailslink());
                    //get merchant data from server then switch page
                    mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

                    ServerManager.getInstance().doGetMerchantDetails(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            mVoucherItem.getDetailslink(),
                            VoucherPagerItemFragment.this);
                }
            }
        });

        Glide.with(VoucherPagerItemFragment.this.getActivity())
                .load(mVoucherItem.getVoucherImgUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImgVoucher);

        DialogFragmentListenerHandler.getInstance().setDialogFragmentVoucherListener(this);

        return (view);
    }

    private void openWebviewFragment(String content_source_url, String title, String htmlText) {
        FragmentTransaction ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        fragment = new VoucherWebviewFragment();
        Bundle args1 = new Bundle();
        args1.putString(VoucherWebviewFragment.TAG_PARAM_URL_ITEM, content_source_url);
        args1.putString(VoucherWebviewFragment.TAG_PARAM_TITLE, title);
        args1.putString(VoucherWebviewFragment.TAG_PARAM_HTML_TEXT, htmlText);
        fragment.setArguments(args1);
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.frame_home_member_content_full, fragment, VoucherWebviewFragment.TAG_LOG);
        ft.addToBackStack(VoucherWebviewFragment.TAG_LOG);
        ft.commit();
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
    public void onDeleteVoucherCallback(final DeleteVoucherResponse deleteVoucherResponse, boolean success) {
        if (success) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), deleteVoucherResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    android.app.Fragment fragment = getActivity().getFragmentManager().findFragmentByTag(VoucherListFragment.TAG_LOG);
                    if (fragment != null) {
                        ((VoucherListFragment) fragment).removeListItemById(mVoucherItem.getVoucherId());
                    }

                    Fragment fragment2 = getActivity().getSupportFragmentManager().findFragmentByTag(VoucherFragment.TAG_LOG);
                    if (fragment2 != null) {
                        ((VoucherFragment) fragment2).removeItemByOfferId(
                                mVoucherItem.getVoucherId()
                        );
                    }
                }
            });
        }
    }

    @Override
    public void onGetDetailCallback(final GetDetailResponse getDetailResponse, boolean success) {
        if (getActivity() != null) {
            mProgressDialog.dismiss();
            if (success) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // switch page
                        FragmentTransaction ft = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                        Fragment fragment;
                        fragment = new DetailsFragment();
                        Bundle args1 = new Bundle();
                        args1.putParcelable(DetailsFragment.TAG_PARAM_DETAIL_ITEM, getDetailResponse);
                        fragment.setArguments(args1);
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.add(R.id.frame_home_member_content_full, fragment, DetailsFragment.TAG_LOG);
                        ft.addToBackStack(DetailsFragment.TAG_LOG);
                        ft.commit();
                    }
                });
            }
        }
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
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {

    }

    @Override
    public void onUseVoucherCallback(final UseVoucherResponse useVoucherResponse, boolean success) {
        mProgressDialog.dismiss();
        if (success && useVoucherResponse.getStatus().equals("success")) {
            DialogHelper.getInstance().showDialogVoucherUseFragment(
                    getActivity(),
                    mVoucherItem.getVoucherId(), useVoucherResponse.getSerial_no(),
                    false, false
            );
        } else {
            if (useVoucherResponse.getMessage() != null)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), useVoucherResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
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
                mProgressDialog.dismiss();
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        getActivity(),
                        status,
                        message,
                        true
                );
            }
        });
    }

    @Override
    public void onTermsConditionCallback(TermsResponse termsResponse, boolean isSuccess) {
        if (isSuccess) {
            openWebviewFragment(null, getString(R.string.terms_and_conditions), termsResponse.getTerms());
        }
    }

    @Override
    public void onSendTokenCallback(boolean isSuccess) {

    }

    public VoucherItem getVoucherItem() {
        return mVoucherItem;
    }

    public void setVoucherItem(VoucherItem voucherItem) {
        mVoucherItem = voucherItem;
    }

    @Override
    public void update(VoucherItem voucherItem) {
        mLblVoucherName.setText(mVoucherItem.getVoucherName());
        mLblVoucherDesc.setText(mVoucherItem.getVoucherDesc());

        mProgressBar.setVisibility(View.VISIBLE);

        Glide.with(VoucherPagerItemFragment.this.getActivity())
                .load(mVoucherItem.getVoucherImgUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImgVoucher);
    }

    @Override
    public void onDialogFragmentCallback(boolean isSuccess, final Integer offerID) {
        if (isSuccess) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    android.app.Fragment fragment = getActivity().getFragmentManager().findFragmentByTag(VoucherListFragment.TAG_LOG);
                    if (fragment != null) {
                        ((VoucherListFragment) fragment).removeListItemById(offerID);
                    }

                    Fragment fragment2 = getActivity().getSupportFragmentManager().findFragmentByTag(VoucherFragment.TAG_LOG);
                    if (fragment2 != null) {
                        ((VoucherFragment) fragment2).removeItemByOfferId(
                                offerID
                        );
                    }
                }
            });
        }
    }
}
