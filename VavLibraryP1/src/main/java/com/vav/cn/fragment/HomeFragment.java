package com.vav.cn.fragment;

import android.app.DialogFragment;
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
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.adapter.HomeListAdapter;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.listener.DialogFragmentSingleButtonListener;
import com.vav.cn.model.HomeListItem;
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
import com.vav.cn.widget.RecyclerViewEmptySupport;
import com.vav.cn.widget.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ServerManagerListener {
    public static final String TAG_LOG = HomeFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;

    private SwipeRefreshLayout mSwipeRefreshHomeList;
    private RecyclerViewEmptySupport mHomeItemList;
    private HomeListAdapter mHomeListAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<HomeListItem> mHomeListItems;

    private int mPageNumber = 1;
    private int lastSyncPage = 0;
    private boolean mRecyclerViewLoading = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mLblHeaderTitle.setText(R.string.home_member_title);
        mBtnHeaderLeft.setVisibility(View.INVISIBLE);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);

        mSwipeRefreshHomeList = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshHomeList);
        mSwipeRefreshHomeList.setColorSchemeResources(
                R.color.swipe_refresh_color_1,
                R.color.swipe_refresh_color_2,
                R.color.swipe_refresh_color_3);
        mSwipeRefreshHomeList.setOnRefreshListener(this);

        //mHomeListItems = getDummyItemList(); //change to refresh
        mHomeListItems = new ArrayList<>();

        mHomeItemList = (RecyclerViewEmptySupport) view.findViewById(R.id.homeItemList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHomeItemList.setLayoutManager(mLayoutManager);
        mHomeItemList.setEmptyView(view.findViewById(R.id.emptyView));
        mHomeItemList.addItemDecoration(new VerticalSpaceItemDecoration(5));
        mHomeItemList.setItemAnimator(new DefaultItemAnimator());
        mHomeItemList.addOnScrollListener(new OnScrollListener() {
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
                            if (mHomeListItems.size() % 10 == 0) {
                                mSwipeRefreshHomeList.setRefreshing(true);
                                mPageNumber++;
                                refreshContent();
                            }
                        }
                    }
                }
            }
        });

        mHomeListAdapter = new HomeListAdapter(getActivity(), mHomeListItems);

        mHomeItemList.setAdapter(mHomeListAdapter);
        mHomeItemList.setHasFixedSize(false);

        //////////////////
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        LocationHelper.getInstance().cancelTimer();
        if (mHomeListAdapter != null) {
            mHomeListAdapter.dismissLoadingProgressDialog();
        }
        super.onDestroy();
    }

    public List<HomeListItem> getDummyItemList() {
        List<HomeListItem> dummyItemList = new ArrayList<>();
        int itemCount = GeneralUtil.randInt(1, 5);
        for (int i = 0; i < itemCount; i++) {
            HomeListItem item = new HomeListItem();
            item.setItemName(GeneralUtil.randomString(20));
            item.setItemDescription(GeneralUtil.randomString(30));
            item.setItemLocation(GeneralUtil.randomString(10));
            dummyItemList.add(item);
        }

        return dummyItemList;
    }

    @Override
    public void onRefresh() {
        refreshContent();
    }

    private void refreshContent() {

        LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
            @Override
            public void gotLocation(final Location location) {
                Log.d(TAG_LOG, "latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
                ServerManager.getInstance().doGetHomeList(
                        PreferenceManagerCustom.getInstance().getUserAuthToken(),
                        location.getLatitude(),
                        location.getLongitude(),
                        Config.HOME_LISTING_DISTANCE,
                        Config.COMPANY_ID,
                        mPageNumber,
                        HomeFragment.this);
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
                    ServerManager.getInstance().doGetHomeList(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            LocationHelper.DEFAULT_LAT,
                            LocationHelper.DEFAULT_LONG,
                            Config.HOME_LISTING_DISTANCE,
                            Config.COMPANY_ID,
                            mPageNumber,
                            HomeFragment.this);
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
                mHomeListAdapter.setHomeListItems(mHomeListItems);
                mHomeListAdapter.notifyDataSetChanged();

                // Stop refresh animation
                mSwipeRefreshHomeList.setRefreshing(false);
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
    public void onGetHomeListCallback(final GetHomeListResponse getHomeListResponse, boolean success) {
        mRecyclerViewLoading = true;
        if (success) {
            if (getHomeListResponse.getStatus().equals("success")) {
                List<HomeListItem> homeListItemList = new ArrayList<>();
                if (lastSyncPage < mPageNumber) {
                    lastSyncPage = mPageNumber;
                } else {
                    onItemsLoadComplete();
                    return;
                }
                List<GetHomeListResponse.Surprise> surpriseList = getHomeListResponse.getSurprise();
                int totalItem = surpriseList.size();
                for (int i = 0; i < totalItem; i++) {
                    HomeListItem item = new HomeListItem();
                    item.setItemName(surpriseList.get(i).getMerchantname());
                    item.setItemDescription(surpriseList.get(i).getPromo_text());
                    item.setItemLocation(surpriseList.get(i).getLocation());
                    item.setItemImageUrl(surpriseList.get(i).getCoverimg());
                    item.setItemDetailsLink(surpriseList.get(i).getDetailslink());

                    homeListItemList.add(item);
                }

                mHomeListItems.addAll(homeListItemList);

                //Load complete
                onItemsLoadComplete();

            } else if (getHomeListResponse.getStatus().equals("warning")) {
                //Load complete
                onItemsLoadComplete();
                if (getActivity() != null && isAdded()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DialogHelper.getInstance().showDialogBasicCustomFragment(
                                    HomeFragment.this.getActivity(),
                                    "",
                                    getHomeListResponse.getMessage(),
                                    getString(R.string.ok),
                                    false,
                                    new DialogFragmentSingleButtonListener() {
                                        @Override
                                        public void onBtnClick(DialogFragment dialogFragment) {
                                            dialogFragment.dismiss();
                                        }
                                    });
                        }
                    });
                }
            }
        } else {
            if (HomeFragment.this.getActivity() != null) {
                HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplicationHelper.getInstance().getApplication(), "Get home list failed..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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
                        HomeFragment.this.getActivity(),
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

