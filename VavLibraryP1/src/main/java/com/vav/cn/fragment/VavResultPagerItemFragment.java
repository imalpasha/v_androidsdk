package com.vav.cn.fragment;

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
import com.vav.cn.listener.DialogFragmentVoucherListener;
import com.vav.cn.model.VavingOfferItem;
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

import java.text.MessageFormat;

/**
 * Created by Handrata Samsul on 2/1/2016.
 */
public class VavResultPagerItemFragment extends Fragment implements ServerManagerListener, DialogFragmentVoucherListener {
    public static final String TAG_LOG = VavResultPagerItemFragment.class.getSimpleName();
    public static final String TAG_PARAM_OFFER_ITEM = "TAG_PARAM_OFFER_ITEM";

    private VavingOfferItem mVavingOfferItem = new VavingOfferItem();

    private TextView mLblOfferName;
    private TextView mLblOfferDesc;
    private ImageView mImgOffer;
    private TextView mBtnUse;
    private TextView mBtnKeep;
    private TextView textViewTerms;
    private TextView textViewStocks;
    private ImageView mImgMerchantInfo;

    private int mLastProcessedId = -1;

    private CustomDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private Boolean isUrl;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mVavingOfferItem = args.getParcelable(TAG_PARAM_OFFER_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vav_result_item_fragment, container, false);

        mLblOfferName = (TextView) view.findViewById(R.id.lblOfferName);
        mLblOfferDesc = (TextView) view.findViewById(R.id.lblOfferDesc);
        mImgOffer = (ImageView) view.findViewById(R.id.imgOffer);
        mBtnUse = (TextView) view.findViewById(R.id.btnUse);
        mBtnKeep = (TextView) view.findViewById(R.id.btnKeep);
        mImgMerchantInfo = (ImageView) view.findViewById(R.id.imgMerchantInfo);
        textViewTerms = (TextView) view.findViewById(R.id.lblTerms);
        textViewStocks = (TextView) view.findViewById(R.id.textviewStock);
        isUrl = mVavingOfferItem.getContent_source_type_name() != null && mVavingOfferItem.getContent_source_type_name().equals("HTML");
        if (isUrl)
            mBtnUse.setText(R.string.textview_open);
        if (mVavingOfferItem.getStock_available() != null) {
            textViewStocks.setText(MessageFormat.format(getString(R.string.stock_left_avaiable), mVavingOfferItem.getStock_available()));
        } else {
            textViewStocks.setVisibility(View.GONE);
        }
        SpannableString content = new SpannableString(textViewTerms.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textViewTerms.setText(content);
        if (mVavingOfferItem.getStock_available() != null && mVavingOfferItem.getStock_available() == 0) {
            mBtnUse.setVisibility(View.GONE);
        }
        mLblOfferName.setText(mVavingOfferItem.getOfferMerchantName());
        mLblOfferDesc.setText(mVavingOfferItem.getOfferDesc());
        if (mVavingOfferItem.getTermslink() == null)
            textViewTerms.setVisibility(View.INVISIBLE);
        textViewTerms.setFocusable(true);
        textViewTerms.setClickable(true);
        textViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerManager.getInstance().doRequestTermsAndCondition(
                        PreferenceManagerCustom.getInstance().getUserAuthToken(),
                        mVavingOfferItem.getTermslink(),
                        VavResultPagerItemFragment.this
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
                                    mVavingOfferItem.getOfferId(), location.getLatitude(), location.getLongitude(),
                                    VavResultPagerItemFragment.this
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
                    openWebviewFragment(mVavingOfferItem.getContent_source_url(), getString(R.string.details_title), null);
                }
            }
        });
        mBtnKeep.setFocusable(true);
        mBtnKeep.setClickable(true);
        mBtnKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

                mLastProcessedId = mVavingOfferItem.getOfferId();

                ServerManager.getInstance().doSaveVoucher(
                        PreferenceManagerCustom.getInstance().getUserAuthToken(),
                        mLastProcessedId,
                        VavResultPagerItemFragment.this
                );
            }
        });

        mImgMerchantInfo.setFocusable(true);
        mImgMerchantInfo.setClickable(true);
        mImgMerchantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Log.d(TAG_LOG, "get merchant data:" + mVavingOfferItem.getDetailslink());
                    //get merchant data from server then switch page
                    mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

                    ServerManager.getInstance().doGetMerchantDetails(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            mVavingOfferItem.getDetailslink(),
                            VavResultPagerItemFragment.this);
                }
            }
        });

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        Glide.with(VavResultPagerItemFragment.this.getActivity())
                .load(mVavingOfferItem.getOfferImgUrl())
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
                .into(mImgOffer);
        DialogFragmentListenerHandler.getInstance().setDialogFragmentVoucherListener(this);
        return view;
    }

    private void onItemsLoadComplete() {
        //mIsCheckingServer=false;

        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //dismiss loading indicator
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
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
    public void onDeleteVoucherCallback(DeleteVoucherResponse deleteVoucherResponse, boolean success) {

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
    public void onSaveVoucherCallback(final SaveVoucherResponse saveVoucherResponse, boolean success) {
        onItemsLoadComplete();
        if (success) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), saveVoucherResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            if (saveVoucherResponse.getStatus().equals("success")) {
                if (mLastProcessedId >= 0) {
                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(VavResultFragment.TAG_LOG);
                    if (fragment != null) {
                        ((VavResultFragment) fragment).removeItemByOfferId(mLastProcessedId);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {

    }

    @Override
    public void onUseVoucherCallback(final UseVoucherResponse useVoucherResponse, boolean success) {
        mProgressDialog.dismiss();
        if (success && useVoucherResponse.getStatus().equals("success")) {
            DialogHelper.getInstance().showDialogVoucherUseFragment(
                    VavResultPagerItemFragment.this.getActivity(),
                    mVavingOfferItem.getOfferId(), useVoucherResponse.getSerial_no(),
                    false, true
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
        mProgressDialog.dismiss();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        VavResultPagerItemFragment.this.getActivity(),
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

    @Override
    public void onDialogFragmentCallback(boolean isSuccess, Integer offerID) {
        onItemsLoadComplete();
        if (isSuccess) {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(VavResultFragment.TAG_LOG);
            if (fragment != null) {
                ((VavResultFragment) fragment).removeItemByOfferId(offerID);
            }
        }
    }
}
