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
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.adapter.HomeListAdapter;
import com.vav.cn.adapter.SearchListAdapter;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.listener.DialogFragmentSingleButtonListener;
import com.vav.cn.listener.ViewClickListener;
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
import com.vav.cn.util.LocationHelper;
import com.vav.cn.util.Logger;
import com.vav.cn.util.PreferenceManagerCustom;
import com.vav.cn.widget.RecyclerViewEmptySupport;
import com.vav.cn.widget.VerticalSpaceItemDecoration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class SearchFragment extends Fragment implements TextView.OnEditorActionListener, TextWatcher, ServerManagerListener, SwipeRefreshLayout.OnRefreshListener, ViewClickListener {
    public static final String TAG_LOG = SearchFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private TextView textViewHeader;
    private ImageView mBtnHeaderRight;

    private EditText mTxtSearch;
    private ImageView mImgClearTxt;
    private LinearLayoutManager mLayoutManager;
    private int mPageNumber = 1;
    private SwipeRefreshLayout mSwipeRefreshHomeList;
    private RecyclerViewEmptySupport recyclerViewList;
    private HomeListAdapter mHomeListAdapter;
    private SearchListAdapter searchListAdapter;
    private List<HomeListItem> mHomeListItems;
    private List<String> searchKeywords;
    private boolean mRecyclerViewLoading = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;
    private String lastKeyword = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.search_fragment, container, false);

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mLblHeaderTitle.setText(R.string.search_title);
        mBtnHeaderLeft.setVisibility(View.INVISIBLE);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);

        mTxtSearch = (EditText) view.findViewById(R.id.txtSearch);
        ImageSpan imageHint = new ImageSpan(getActivity(), R.drawable.ic_search);
        SpannableString spannableString = new SpannableString("_" + getString(R.string.search_txt_hint));
        spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTxtSearch.setHint(spannableString);
        mTxtSearch.setOnEditorActionListener(this);
        mTxtSearch.addTextChangedListener(this);

        mImgClearTxt = (ImageView) view.findViewById(R.id.imgClearTxt);
        mImgClearTxt.setFocusable(true);
        mImgClearTxt.setClickable(true);
        mImgClearTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtSearch.setText("");
                textViewHeader.setText(R.string.search_recent_searches);
                mHomeListItems.clear();
                mHomeListAdapter.notifyDataSetChanged();
                searchListAdapter.notifyDataSetChanged();
                recyclerViewList.setmEmptyDataObserver();
                recyclerViewList.setAdapter(searchListAdapter);
            }
        });
        if (TextUtils.isEmpty(mTxtSearch.getText())) mImgClearTxt.setVisibility(View.INVISIBLE);
        textViewHeader = (TextView) view.findViewById(R.id.lblSearchMessage);
        recyclerViewList = (RecyclerViewEmptySupport) view.findViewById(R.id.searchItemList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewList.setLayoutManager(mLayoutManager);
        recyclerViewList.addItemDecoration(new VerticalSpaceItemDecoration(5));
        recyclerViewList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            }
                        }
                    }
                }
            }
        });
        mSwipeRefreshHomeList = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshHomeList);
        mSwipeRefreshHomeList.setColorSchemeResources(
                R.color.swipe_refresh_color_1,
                R.color.swipe_refresh_color_2,
                R.color.swipe_refresh_color_3);
        mSwipeRefreshHomeList.setOnRefreshListener(this);
        mHomeListItems = new ArrayList<>();
        mHomeListAdapter = new HomeListAdapter(getActivity(), mHomeListItems);
        searchKeywords = new ArrayList<>(5);
        searchKeywords.addAll(PreferenceManagerCustom.getInstance().getSearchKeywords());
        searchListAdapter = new SearchListAdapter(getActivity(), searchKeywords, this);
        recyclerViewList.setEmptyView(view.findViewById(R.id.emptyView));
        recyclerViewList.setAdapter(searchListAdapter);
        recyclerViewList.setHasFixedSize(false);
        return view;
    }

    public void processKeyword(String keyword) {
        if (searchKeywords.size() > 5) {
            if (searchKeywords.indexOf(keyword) > 0) {
                searchKeywords.remove(searchKeywords.indexOf(keyword));
            } else {
                searchKeywords.remove(searchKeywords.size() - 1);
            }
        } else if (searchKeywords.indexOf(keyword) >= 0) {
            searchKeywords.remove(searchKeywords.indexOf(keyword));
        }
        searchKeywords.add(0, keyword);
    }

    public void performSearch(final String keyword) {
        if (keyword.equals("")) {
            mSwipeRefreshHomeList.setRefreshing(false);
            return;
        }
        lastKeyword = keyword;
        textViewHeader.setText(R.string.searching);
        setEnableSearch(false);
        processKeyword(keyword);
        PreferenceManagerCustom.getInstance().setSearchKeywords(new HashSet<String>(searchKeywords));
        recyclerViewList.setmEmptyDataObserver();
        recyclerViewList.removeEmptyView();
        recyclerViewList.setAdapter(mHomeListAdapter);
        mSwipeRefreshHomeList.setRefreshing(true);

        LocationHelper.getInstance().getLocation(getActivity(), new LocationHelper.LocationResult() {
            @Override
            public void gotLocation(final Location location) {
                Log.d(TAG_LOG, "latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
                ServerManager.getInstance().doGetMerchantSearch(
                        PreferenceManagerCustom.getInstance().getUserAuthToken(),
                        location.getLatitude(),
                        location.getLongitude(),
                        Config.HOME_LISTING_DISTANCE,
                        Config.COMPANY_ID,
                        mPageNumber,
                        keyword,
                        SearchFragment.this);
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

                    performSearch(lastKeyword);

                } else {
                    ServerManager.getInstance().doGetMerchantSearch(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            LocationHelper.DEFAULT_LAT,
                            LocationHelper.DEFAULT_LONG,
                            Config.HOME_LISTING_DISTANCE,
                            Config.COMPANY_ID,
                            mPageNumber,
                            lastKeyword,
                            SearchFragment.this);
                }
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String search = mTxtSearch.getText().toString();
            performSearch(search);
            Log.d(TAG_LOG, "Search String:" + search);
            return true;
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
                // Update the adapter and notify data set changed
                mHomeListAdapter.setHomeListItems(mHomeListItems);
                mHomeListAdapter.notifyDataSetChanged();

                // Stop refresh animation
                mSwipeRefreshHomeList.setRefreshing(false);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            mImgClearTxt.setVisibility(View.INVISIBLE);
        } else {
            mImgClearTxt.setVisibility(View.VISIBLE);
        }
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
        setEnableSearch(true);
        mRecyclerViewLoading = true;
        if (success) {
            if (getHomeListResponse.getStatus().equals("success")) {
                List<HomeListItem> homeListItemList = new ArrayList<>();

                List<GetHomeListResponse.Surprise> surpriseList = getHomeListResponse.getSurprise();
                final int totalItem = surpriseList.size();
                for (int i = 0; i < totalItem; i++) {
                    HomeListItem item = new HomeListItem();
                    item.setItemName(surpriseList.get(i).getMerchantname());
                    item.setItemDescription(surpriseList.get(i).getPromo_text());
                    item.setItemLocation(surpriseList.get(i).getLocation());
                    item.setItemImageUrl(surpriseList.get(i).getCoverimg());
                    item.setItemDetailsLink(surpriseList.get(i).getDetailslink());

                    homeListItemList.add(item);
                }
                if (SearchFragment.this.getActivity() != null) {
                    SearchFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (totalItem > 0) {
                                textViewHeader.setText(MessageFormat.format(ApplicationHelper.getInstance().getApplication().getString(R.string.offers_found), totalItem));
                            } else {
                                textViewHeader.setText(ApplicationHelper.getInstance().getApplication().getString(R.string.search_recent_searches));
                            }
                        }
                    });
                }
                mHomeListItems = homeListItemList;

                //Load complete
                onItemsLoadComplete();

            } else if (getHomeListResponse.getStatus().equals("warning")) {
                //Load complete
                onItemsLoadComplete();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewHeader.setText(R.string.search_no_data);
                        }
                    });
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DialogHelper.getInstance().showDialogBasicCustomFragment(
                                SearchFragment.this.getActivity(),
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
        } else {
            if (SearchFragment.this.getActivity() != null) {
                SearchFragment.this.getActivity().runOnUiThread(new Runnable() {
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
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {

    }

    @Override
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(String status, String message, final boolean isUnauthorizedError) {
        if (getActivity() == null) {
            return;
        }
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(getActivity());
            return;
        }
    }

    @Override
    public void onTermsConditionCallback(TermsResponse termsResponse, boolean isSuccess) {

    }

    @Override
    public void onSendTokenCallback(boolean isSuccess) {

    }

    @Override
    public void onRefresh() {
        performSearch(mTxtSearch.getText().toString());
    }

    @Override
    public void onItemClick(String id) {
        mTxtSearch.setText(id);
        performSearch(id);
    }

    public void setEnableSearch(final Boolean enableSearch) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTxtSearch.setEnabled(enableSearch);
                    mImgClearTxt.setEnabled(enableSearch);
                }
            });
        }
    }
}
