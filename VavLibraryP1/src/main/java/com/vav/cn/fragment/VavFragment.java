package com.vav.cn.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.model.VavingOfferItem;
import com.vav.cn.pincast.PincastHelper;
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
import com.vav.cn.util.LocationHelper;
import com.vav.cn.util.Logger;
import com.vav.cn.util.MarshmallowHelper;
import com.vav.cn.util.PreferenceManagerCustom;
import com.vav.cn.widget.CountDownTimerCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class VavFragment extends Fragment implements Observer, ServerManagerListener {
    public static final String TAG_LOG = VavFragment.class.getSimpleName();

    private ImageView mImgVavingIndicator;
    private ImageView mImgClose;

    private HashMap<String, Integer> mUltraSoundIdMap = new HashMap<>();

    private boolean mIsCheckingServer = false;
    private boolean mListenNextOneId = false;

    private CustomDialog mProgressDialog;

    private CountDownTimerCustom mVavingTimer;

    private AnimationDrawable mFrameAnimation;
    private RequestType requestType = RequestType.VAVINGLOCATION;
    private String ultrasoundID;
    private boolean isNeedCloseFragment = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.vav_fragment, container, false);
        mImgClose = (ImageView) view.findViewById(R.id.imgClose);
        mImgClose.setFocusable(true);
        mImgClose.setClickable(true);
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    FragmentManager fm = getActivity().getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment fragment = fm.findFragmentByTag(TAG_LOG);
                    if (fragment instanceof VavFragment) {
                        ft.remove(fragment);
                        ft.commitAllowingStateLoss();
                    }
                    if (isNeedCloseFragment)
                        VavDirectGenerator.getInstance().closeFragment();
                }
            }
        });

        mImgVavingIndicator = (ImageView) view.findViewById(R.id.imgVavingIndicator);
        mImgVavingIndicator.setFocusable(true);
        mImgVavingIndicator.setClickable(true);
        mImgVavingIndicator.setBackgroundResource(R.drawable.vaving_animation);
        mFrameAnimation = (AnimationDrawable) mImgVavingIndicator.getBackground();
        mFrameAnimation.start();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MarshmallowHelper.getInstance().handleAudioPermission(getActivity());
        PincastHelper.getInstance().startPincast(this);
        mVavingTimer = new CountDownTimerCustom(Config.VAVING_EMPTY_ID_TIMER_IN_MILLISECONDS, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = Math.round((float) millisUntilFinished / 1000);

                if (seconds == ((Config.VAVING_EMPTY_ID_TIMER_IN_MILLISECONDS - Config.VAVING_TIMER_IN_MILLISECONDS) / 1000)) {
                    if (!isListenedIdEmpty(mUltraSoundIdMap)) {
                        if (getActivity() != null) {
                            String mostFreqUltrasoundId = getMostFrequentId(mUltraSoundIdMap);
                            if (!mListenNextOneId) {
                                //max freq id don't have the same value with other freq
                                refreshContent(mostFreqUltrasoundId);

                            }

                            mVavingTimer.cancel();
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                if (isListenedIdEmpty(mUltraSoundIdMap)) {
                    requestType = RequestType.VAVINGLOCATION;
                    requestVavingLocation();
                } else {
                    //check the most freq signal id, and check whether there is the same id
                    if (getActivity() != null) {
                        String mostFreqUltrasoundId = getMostFrequentId(mUltraSoundIdMap);
                        if (!mListenNextOneId) {
                            //max freq id don't have the same value with other freq
                            refreshContent(mostFreqUltrasoundId);
                        }
                    }
                }
            }
        }.start();
    }

    private void requestVavingLocation() {
        if (getActivity() != null) {
            //got no id, should display location based offers from API (server not ready yet)
            LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
                @Override
                public void gotLocation(final Location location) {
                    Log.d(TAG_LOG, "latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
                    ServerManager.getInstance().doVavingLocation(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            location.getLatitude(), location.getLongitude(), Config.HOME_LISTING_DISTANCE,
                            Config.COMPANY_ID,
                            VavFragment.this);
                }

                @Override
                public void onFailCallback(String message) {
                    Logger.getInstance().e(TAG_LOG, "Getting location failed:" + message);
                }

                @Override
                public void onRequestTurnGPS() {
                    LocationHelper.getInstance().requestUserPermissionTurnGPS(getActivity());
                }

            });
//							Toast.makeText(getActivity(), "counted 5 seconds, empty id, get location based offers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPincast();
    }

    public boolean isListenedIdEmpty(HashMap<String, Integer> ultraSoundIdMap) {
        return ultraSoundIdMap.isEmpty();
    }

    private String getMostFrequentId(HashMap<String, Integer> ultraSoundIdMap) {
        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : ultraSoundIdMap.entrySet()) {
            if (maxEntry == null) {
                maxEntry = entry;
            } else {
                if (entry.getValue().equals(maxEntry.getValue())) {
                    mListenNextOneId = true;
                    maxEntry = entry;
                } else if (entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
        }

        return maxEntry != null ? maxEntry.getKey() : "";
    }

    private void stopPincast() {
        PincastHelper.getInstance().stopPincast();
    }

    private void refreshContent(final String ultraSoundId) {
        mIsCheckingServer = true;
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //show loading indicator
                    ultrasoundID = ultraSoundId;
                    requestType = RequestType.VAVINGULTRASOUND;
                    showLoadingProgressDialog();

                    //get data
                    LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
                        @Override
                        public void gotLocation(final Location location) {
                            Log.d(TAG_LOG, "latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
                            ServerManager.getInstance().doVaving(
                                    PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                    ultraSoundId,
                                    VavFragment.this, Config.COMPANY_ID, location.getLatitude(), location.getLongitude(), Config.HOME_LISTING_DISTANCE);
                        }

                        @Override
                        public void onFailCallback(String message) {
                            Logger.getInstance().e(TAG_LOG, "Getting location failed:" + message);
                        }

                        @Override
                        public void onRequestTurnGPS() {
                            LocationHelper.getInstance().requestUserPermissionTurnGPS(getActivity());
                        }

                    });
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Config.REQUEST_PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (requestType.equals(RequestType.VAVINGULTRASOUND)) {
                        refreshContent(ultrasoundID);
                    } else {
                        requestVavingLocation();
                    }
                } else {
                    showLoadingProgressDialog();
                    if (requestType.equals(RequestType.VAVINGULTRASOUND)) {
                        ServerManager.getInstance().doVaving(
                                PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                ultrasoundID,
                                VavFragment.this, Config.COMPANY_ID, LocationHelper.DEFAULT_LAT, LocationHelper.DEFAULT_LONG, Config.HOME_LISTING_DISTANCE);
                    } else {
                        ServerManager.getInstance().doVavingLocation(
                                PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                LocationHelper.DEFAULT_LAT, LocationHelper.DEFAULT_LONG, Config.HOME_LISTING_DISTANCE,
                                Config.COMPANY_ID,
                                VavFragment.this);
                    }
                }
            }
        }
    }

    public void showLoadingProgressDialog() {
        if (getActivity() != null) {
            if (mProgressDialog == null) {
                mProgressDialog = new CustomDialog(getActivity());
                mProgressDialog.setMessage(ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_loading_message));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog(){
        if (getActivity() != null && mProgressDialog!=null) {
            mProgressDialog.dismiss();
        }
    }

    private void onItemsLoadComplete(final List<VavingOfferItem> vavingOfferItemList) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //dismiss loading indicator
                    hideProgressDialog();

                    if (vavingOfferItemList.size() > 0) {
                        android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();
                        android.support.v4.app.Fragment fragment;
                        fragment = new VavResultFragment();
                        Bundle args1 = new Bundle();
                        ArrayList<VavingOfferItem> listParam = new ArrayList<>(vavingOfferItemList);
                        args1.putParcelableArrayList(VavResultFragment.TAG_PARAM_OFFER_LIST, listParam);
                        if (getActivity() != null && !getActivity().isFinishing()) {
                            fragment.setArguments(args1);
                            ft.replace(R.id.frame_home_member_content_full, fragment, VavResultFragment.TAG_LOG);
//                            ft.addToBackStack(VavResultFragment.TAG_LOG);
                            ft.commitAllowingStateLoss();
                        }
                        isNeedCloseFragment = false;
                        mImgClose.performClick();
                    } else {
                        // set retry searching code?

                    }
                }
            });
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            if (!mIsCheckingServer) {
                String ultraSoundId = "";
                if (data != null) {
                    ultraSoundId = data.toString();
                } else {
                }
                ultraSoundId = ultraSoundId.replace(".", "");
//            if (ultraSoundId.contains("InphosoftTV")) {
                if (mUltraSoundIdMap.get(ultraSoundId) == null) {
                    mUltraSoundIdMap.put(ultraSoundId, 1);
                } else {
                    mUltraSoundIdMap.put(ultraSoundId, mUltraSoundIdMap.get(ultraSoundId) + 1);
                }

                if (mListenNextOneId) {

                    //get offer list from server
                    refreshContent(ultraSoundId);
                }

                //if ultrasound id is captured 5 or more, get the data from server
/*				if (mUltraSoundIdMap.get(ultraSoundId) >= 5) {
                    mIsCheckingServer = true;

					//get offer list from server
					refreshContent(ultraSoundId);
				}*/
//            }
            }
        } catch (Exception e) {
        }

        Log.d(TAG_LOG, "pincast data:" + data.toString() + " " + mUltraSoundIdMap.toString());
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
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {
        List<VavingOfferItem> vavingOfferItemList = new ArrayList<>();
        if (success) {
            List<VavingResponse.Offer> offerList = vavingResponse.getOffers();
            int offerListSize = offerList.size();
            for (int i = 0; i < offerListSize; i++) {
                VavingOfferItem item = new VavingOfferItem();
                item.setOfferId(offerList.get(i).getOfferid());
                item.setOfferMerchantName(offerList.get(i).getMerchantname());
                item.setOfferDesc(offerList.get(i).getDeals());
                item.setOfferUrl(offerList.get(i).getOfferurl());
                item.setOfferImgUrl(offerList.get(i).getOfferimg());
                item.setContent_source_type_name(offerList.get(i).getContent_source_type_name());
                item.setContent_source_url(offerList.get(i).getContent_source_url());
                item.setDetailslink(offerList.get(i).getDetailslink());
                item.setTermslink(offerList.get(i).getTermslink());
                item.setStock_available(offerList.get(i).getStock_available());
                vavingOfferItemList.add(item);
            }

        } else {
            final String message = vavingResponse.getMessage();
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        onItemsLoadComplete(vavingOfferItemList);
    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        List<VavingOfferItem> vavingOfferItemList = new ArrayList<>();

        //Load complete
        onItemsLoadComplete(vavingOfferItemList);
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
                        VavFragment.this.getActivity(),
                        status,
                        message,
                        true
                );
            }
        });
        stopPincast();
    }

    @Override
    public void onTermsConditionCallback(TermsResponse termsResponse, boolean isSuccess) {

    }

    @Override
    public void onSendTokenCallback(boolean isSuccess) {

    }

    private enum RequestType {
        VAVINGLOCATION, VAVINGULTRASOUND
    }
}
