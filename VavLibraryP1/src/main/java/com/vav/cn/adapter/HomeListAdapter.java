package com.vav.cn.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vav.cn.R;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.fragment.DetailsFragment;
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
import com.vav.cn.util.PreferenceManagerCustom;

import java.util.List;

/**
 * Created by Handrata Samsul on 1/14/2016.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder> implements ServerManagerListener {
    public static final String TAG_LOG = HomeListAdapter.class.getSimpleName();
    private Activity mActivity;
    private List<HomeListItem> mHomeListItems;
    private CustomDialog mProgressDialog;

    public HomeListAdapter(Activity activity, List<HomeListItem> homeListItems) {
        mActivity = activity;
        mHomeListItems = homeListItems;
    }

    public List<HomeListItem> getHomeListItems() {
        return mHomeListItems;
    }

    public void setHomeListItems(List<HomeListItem> homeListItems) {
        mHomeListItems = homeListItems;
    }

    public void dismissLoadingProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public HomeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);

        runSlideInAnimation(view);
        HomeListViewHolder viewHolder = new HomeListViewHolder(view);
        //Log.d("Adapter", "onCreate position:"+position);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeListViewHolder holder, final int position) {
        final HomeListItem currentItem = mHomeListItems.get(position);

        //runSlideInAnimation(holder.getItemView());
        holder.getLblItemName().setText(currentItem.getItemName());
        holder.getLblItemDescription().setText(currentItem.getItemDescription());
        holder.getLblItemLocation().setText(currentItem.getItemLocation());
        ViewGroup itemContainer = holder.getItemContainer();
        itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity != null) {
                    Log.d(TAG_LOG, "get merchant data:" + mHomeListItems.get(position).getItemDetailsLink());
                    //get merchant data from server then switch page
                    mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(mActivity);

                    ServerManager.getInstance().doGetMerchantDetails(
                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                            currentItem.getItemDetailsLink(),
                            HomeListAdapter.this);
                }
            }
        });

        final ProgressBar progressBar = holder.getProgressBar();
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(mActivity)
                .load(currentItem.getItemImageUrl())
                .centerCrop()
                .placeholder(mActivity.getResources().getColor(R.color.transparent_dark))
                .error(R.drawable.ic_image_placeholder)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.getImgItemCoverImage());
    }

    @Override
    public int getItemCount() {
        if (mHomeListItems != null) {
            return mHomeListItems.size();
        }
        return 0;
    }


    private void runSlideInAnimation(View view) {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        view.setTranslationX(-width);
        view.animate()
                .translationX(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(mActivity.getResources().getInteger(android.R.integer.config_longAnimTime))
                //.setDuration(20000)
                .start();
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
        if (mActivity != null) {
            mProgressDialog.dismiss();
            if (success) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // switch page
                        FragmentTransaction ft = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();
                        Fragment fragment;
                        fragment = new DetailsFragment();
                        Bundle args1 = new Bundle();
                        args1.putParcelable(DetailsFragment.TAG_PARAM_DETAIL_ITEM, getDetailResponse);
                        fragment.setArguments(args1);
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.frame_home_member_content, fragment, DetailsFragment.TAG_LOG);
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
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        if (mActivity.getBaseContext() == null) {
            return;
        }
        mProgressDialog.dismiss();
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(mActivity);
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        mActivity,
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

    public class HomeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mItemView;
        private ViewGroup itemContainer;
        private TextView lblItemName;
        private TextView lblItemDescription;
        private TextView lblItemLocation;
        private ImageView imgItemCoverImage;
        private ProgressBar progressBar;

        public HomeListViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;

            itemContainer = (ViewGroup) itemView.findViewById(R.id.viewGroupItemContainer);
            itemContainer.setFocusable(true);
            itemContainer.setClickable(true);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            imgItemCoverImage = (ImageView) itemView.findViewById(R.id.imgItemCoverImage);
            lblItemName = (TextView) itemView.findViewById(R.id.lblItemName);
            lblItemDescription = (TextView) itemView.findViewById(R.id.lblItemDesc);
            lblItemLocation = (TextView) itemView.findViewById(R.id.lblItemLocation);
        }


        public View getItemView() {
            return mItemView;
        }

        public void setItemView(View itemView) {
            mItemView = itemView;
        }

        public ViewGroup getItemContainer() {
            return itemContainer;
        }

        public void setItemContainer(ViewGroup itemContainer) {
            this.itemContainer = itemContainer;
        }

        public TextView getLblItemName() {
            return lblItemName;
        }

        public void setLblItemName(TextView lblItemName) {
            this.lblItemName = lblItemName;
        }

        public TextView getLblItemDescription() {
            return lblItemDescription;
        }

        public void setLblItemDescription(TextView lblItemDescription) {
            this.lblItemDescription = lblItemDescription;
        }

        public TextView getLblItemLocation() {
            return lblItemLocation;
        }

        public void setLblItemLocation(TextView lblItemLocation) {
            this.lblItemLocation = lblItemLocation;
        }

        public ImageView getImgItemCoverImage() {
            return imgItemCoverImage;
        }

        public void setImgItemCoverImage(ImageView imgItemCoverImage) {
            this.imgItemCoverImage = imgItemCoverImage;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
