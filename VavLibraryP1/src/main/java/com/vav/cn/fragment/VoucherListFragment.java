package com.vav.cn.fragment;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.adapter.VoucherListAdapter;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.directhelper.VavDirectGenerator;
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
import com.vav.cn.util.DialogHelper;
import com.vav.cn.util.DirectToSplashScreenHelper;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.LocationHelper;
import com.vav.cn.util.Logger;
import com.vav.cn.util.PreferenceManagerCustom;
import com.vav.cn.util.VoucherListComparatorAlphabet;
import com.vav.cn.util.VoucherListComparatorExpireDate;
import com.vav.cn.widget.RecyclerViewEmptySupport;
import com.vav.cn.widget.SearchListDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class VoucherListFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ServerManagerListener {
    public static final String TAG_LOG = VoucherListFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;
    private SwipeRefreshLayout mSwipeRefreshHomeList;

    private List<VoucherItem> mVoucherListItems;
    private RecyclerViewEmptySupport mVoucherItemList;
    private TextView mEmptyView;
    private LinearLayoutManager mLayoutManager;
    private VoucherListAdapter mVoucherListAdapter;
    private int mPageNumber = 1;
    private boolean mRecyclerViewLoading = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;
    private int lastSyncPage = 0;
    private boolean isReset = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.voucher_list_fragment, container, false);
        mSwipeRefreshHomeList = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshHomeList);
        mSwipeRefreshHomeList.setColorSchemeResources(
                R.color.swipe_refresh_color_1,
                R.color.swipe_refresh_color_2,
                R.color.swipe_refresh_color_3);
        mSwipeRefreshHomeList.setOnRefreshListener(this);
        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mLblHeaderTitle.setText(R.string.voucher_list_title);
        mBtnHeaderLeft.setVisibility(View.INVISIBLE);
        mBtnHeaderRight.setVisibility(View.VISIBLE);
        mBtnHeaderRight.setImageResource(R.drawable.header_btn_close);
        mBtnHeaderRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VavDirectGenerator.getInstance().closeFragment();
            }
        });

        mVoucherListItems = new ArrayList<>();

        mVoucherItemList = (RecyclerViewEmptySupport) view.findViewById(R.id.voucherItemList);
        mEmptyView = (TextView) view.findViewById(R.id.emptyView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVoucherItemList.setLayoutManager(mLayoutManager);
        mVoucherItemList.setEmptyView(mEmptyView);
        mVoucherItemList.addItemDecoration(new SearchListDividerItemDecoration(getActivity()));
        mVoucherItemList.setItemAnimator(new DefaultItemAnimator());
        mVoucherItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down{
                    mVisibleItemCount = mLayoutManager.getChildCount();
                    mTotalItemCount = mLayoutManager.getItemCount();
                    mPastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (mRecyclerViewLoading) {
                        if ((mVisibleItemCount + mTotalItemCount) >= mPastVisiblesItems) {
                            mRecyclerViewLoading = false;
                            Log.d(TAG_LOG, "last item");
                            //scrolled to last item, 10 is number of item per page
                            if (mVoucherListItems.size() % 10 == 0) {
                                mSwipeRefreshHomeList.setRefreshing(true);
                                mPageNumber++;
                                refreshContent();
                            }
                        }
                    }
                }
            }
        });

        mVoucherListAdapter = new VoucherListAdapter(getActivity(), mVoucherListItems, this);
        mVoucherListAdapter.hasStableIds();

        mVoucherItemList.setAdapter(mVoucherListAdapter);
        mVoucherItemList.setHasFixedSize(false);
        mSwipeRefreshHomeList.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshHomeList.setRefreshing(true);
                refreshContent();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        if (mSwipeRefreshHomeList != null) {
            mSwipeRefreshHomeList.setRefreshing(false);
        }
        super.onDestroy();
    }

    private void refreshContent() {
        if (getActivity() != null) {
            //show loading indicator
//			mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

            //get data
            LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
                @Override
                public void gotLocation(final Location location) {
                    Log.d(TAG_LOG, "latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
                    ServerManager.getInstance().doGetVoucherList(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            VoucherListFragment.this, location.getLatitude(), location.getLongitude(), Config.HOME_LISTING_DISTANCE, mPageNumber);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Config.REQUEST_PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    refreshContent();

                } else {
                    ServerManager.getInstance().doGetVoucherList(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            VoucherListFragment.this, LocationHelper.DEFAULT_LAT,
                            LocationHelper.DEFAULT_LONG, Config.HOME_LISTING_DISTANCE, mPageNumber);
                }
            }
        }
    }

    private void onItemsLoadComplete() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update the adapter and notify data set changed
                mVoucherListAdapter.setVoucherListItemList(mVoucherListItems);
                mVoucherListAdapter.notifyDataSetChanged();

                //dismiss loading indicator
                mSwipeRefreshHomeList.setRefreshing(false);
            }
        });
    }

    public List<VoucherItem> getDummyItemList() {
        List<VoucherItem> dummyItemList = new ArrayList<>();
        int itemCount = GeneralUtil.randInt(1, 10);
        for (int i = 0; i < itemCount; i++) {
            VoucherItem item = new VoucherItem();
            item.setVoucherName(GeneralUtil.randomString(20));
            item.setVoucherDesc(GeneralUtil.randomString(30));
            Calendar calendar = Calendar.getInstance();
            item.setVoucherExpiredDate(getString(R.string.voucher_expire) + GeneralUtil.getDateFromTimeMillis(GeneralUtil.randLong(0, calendar.getTimeInMillis()), Config.SEARCH_EXPIRE_DATE_FORMAT));
            dummyItemList.add(item);
        }

        return dummyItemList;
    }

    public void removeListItemById(int offerId) {
        mVoucherListAdapter.removeItemByOfferId(offerId);
    }

    public void sortVoucherListByAlphabet(List<VoucherItem> voucherListItems) {
        Collections.sort(voucherListItems, new VoucherListComparatorAlphabet());
    }

    public void sortVoucherListByEpireDate(List<VoucherItem> voucherListItems) {
        Collections.sort(voucherListItems, new VoucherListComparatorExpireDate());
    }

    @Override
    public void onClick(View v) {

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
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), deleteVoucherResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        refreshContent();
                    }
                });
            }
        }
    }

    @Override
    public void onGetDetailCallback(GetDetailResponse getDetailResponse, boolean success) {

    }

    @Override
    public void onGetHomeListCallback(GetHomeListResponse getHomeListResponse, boolean success) {
        onItemsLoadComplete();
    }

    @Override
    public void onGetProfileCallback(GetProfileResponse getProfileResponse, boolean success) {

    }

    @Override
    public void onGetVoucherListCallback(GetVoucherListResponse getVoucherListResponse, boolean success) {
        if (success) {
            if (getVoucherListResponse.getStatus().equals("success")) {
                List<VoucherItem> voucherItems = new ArrayList<>();
                if (lastSyncPage < mPageNumber) {
                    lastSyncPage = mPageNumber;
                } else {
                    onItemsLoadComplete();
                    return;
                }
                List<GetVoucherListResponse.Surprise> surpriseList = getVoucherListResponse.getSurprise();
                int totalItem = surpriseList.size();
                for (int i = 0; i < totalItem; i++) {
                    VoucherItem item = new VoucherItem();
                    item.setVoucherId(surpriseList.get(i).getOfferid());
                    item.setVoucherUrl(surpriseList.get(i).getOfferurl());
                    item.setVoucherName(surpriseList.get(i).getMerchantname());
                    item.setVoucherDesc(surpriseList.get(i).getDeals());
                    item.setVoucherImgUrl(surpriseList.get(i).getOfferimg());
                    item.setVoucherImgThumbUrl(surpriseList.get(i).getThumbnail());
                    item.setVoucherExpiredDate(surpriseList.get(i).getExpire());
                    item.setTermslink(surpriseList.get(i).getTermslink());
                    item.setDetailslink(surpriseList.get(i).getDetailslink());
                    item.setContent_source_url(surpriseList.get(i).getContent_source_url());
                    item.setContent_source_type_name(surpriseList.get(i).getContent_source_type_name());
                    item.setStock_available(surpriseList.get(i).getStock_available());
                    item.setDistance(surpriseList.get(i).getDistance());
                    voucherItems.add(item);
                }

                if (PreferenceManagerCustom.getInstance().getVoucherSortByType() == PreferenceManagerCustom.VOUCHER_SORT_TYPE.ALPHABET) {
                    sortVoucherListByAlphabet(voucherItems);
                } else if (PreferenceManagerCustom.getInstance().getVoucherSortByType() == PreferenceManagerCustom.VOUCHER_SORT_TYPE.EXPIRE_DATE) {
                    sortVoucherListByEpireDate(voucherItems);
                }

                //Load items
                if(isReset){
                    mVoucherListItems = voucherItems;
                    isReset = false;
                }
                else{
                    mVoucherListItems.addAll(voucherItems);
                }
            } else if (getVoucherListResponse.getStatus().equals("error")) {
                final String message = getVoucherListResponse.getMessage();
                if (VoucherListFragment.this.getActivity() != null) {
                    VoucherListFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEmptyView.setText(message);
                        }
                    });
                }
            }
        } else {
            if (VoucherListFragment.this.getActivity() != null) {
                VoucherListFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplicationHelper.getInstance().getApplication(), "Get voucher list failed..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //Load complete
        onItemsLoadComplete();
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
        //Load complete
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
                        VoucherListFragment.this.getActivity(),
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

    @Override
    public void onRefresh() {
        lastSyncPage = 0;
        mPageNumber = 1;
        isReset = true;
        refreshContent();
    }
}
