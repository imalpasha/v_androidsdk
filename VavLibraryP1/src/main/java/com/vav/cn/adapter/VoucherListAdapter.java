package com.vav.cn.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vav.cn.R;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.fragment.VoucherFragment;
import com.vav.cn.listener.DialogFragmentTwoButtonListener;
import com.vav.cn.model.VoucherItem;
import com.vav.cn.server.ServerManager;
import com.vav.cn.server.listener.ServerManagerListener;
import com.vav.cn.util.DialogHelper;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.PreferenceManagerCustom;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/20/2016.
 */
public class VoucherListAdapter extends RecyclerView.Adapter<VoucherListAdapter.VoucherListViewHolder> {
    public static final String LOG_TAG = VoucherListAdapter.class.getSimpleName();
    private Activity mActivity;
    private List<VoucherItem> mVoucherListItemList;
    private int LIST_SWIPE_OFFSET_POINT = 20;
    private ServerManagerListener mServerManagerListener;

    public VoucherListAdapter(Activity activity, List<VoucherItem> voucherListItemList, ServerManagerListener listener) {
        mActivity = activity;
        mVoucherListItemList = voucherListItemList;
        mServerManagerListener = listener;
    }

    public List<VoucherItem> getVoucherListItemList() {
        return mVoucherListItemList;
    }

    public void setVoucherListItemList(List<VoucherItem> voucherListItemList) {
        mVoucherListItemList = voucherListItemList;
    }

    public void removeItemByOfferId(int offerId) {
        for (int i = 0; i < mVoucherListItemList.size(); i++) {
            if (mVoucherListItemList.get(i).getVoucherId() == offerId) {
                mVoucherListItemList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mVoucherListItemList.size());
                break;
            }
        }
    }

    @Override
    public VoucherListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_list_item, parent, false);
        VoucherListViewHolder viewHolder = new VoucherListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoucherListViewHolder holder, int position) {
        final VoucherItem currentItem = mVoucherListItemList.get(position);

        holder.getLblVoucherName().setText(currentItem.getVoucherName());
        holder.getLblVoucherDesc().setText(currentItem.getVoucherDesc());
        if (currentItem.getVoucherExpiredDate() != null) {
            holder.getLblVoucherExpiredDate().setVisibility(View.VISIBLE);
            holder.getLblVoucherExpiredDate().setText(mActivity.getString(R.string.voucher_expire) + GeneralUtil.getInstance().formatExpireDateFromServer(currentItem.getVoucherExpiredDate()));
        } else
            holder.getLblVoucherExpiredDate().setVisibility(View.GONE);
        if (currentItem.getStock_available() != null) {
            holder.getTextViewStocks().setVisibility(View.VISIBLE);
            holder.getTextViewStocks().setText(MessageFormat.format(mActivity.getString(R.string.stock_left_avaiable), currentItem.getStock_available()));
        } else
            holder.getTextViewStocks().setVisibility(View.GONE);
        if (currentItem.getVoucherExpiredDate() != null) {
            if (GeneralUtil.getInstance().isDateExpired(currentItem.getVoucherExpiredDate())) {
                holder.getViewGroupItemContent().setBackgroundColor(mActivity.getResources().getColor(R.color.expired_voucher_item_background));
            } else if (GeneralUtil.getInstance().isAlmostExpired(currentItem.getVoucherExpiredDate())) {
                holder.getViewGroupItemContent().setBackgroundColor(mActivity.getResources().getColor((R.color.almost_expired_voucher_item_background)));
            } else {
                holder.getViewGroupItemContent().setBackgroundColor(mActivity.getResources().getColor((R.color.white)));
            }
        }
        final ImageView imgVoucherIcon = holder.getImgVoucherIcon();

        final ProgressBar progressBar = holder.getProgressBar();
        progressBar.setVisibility(View.VISIBLE);

        final int imgSize = (int) mActivity.getResources().getDimension(R.dimen.voucher_list_icon);

        Glide.with(mActivity)
                .load(currentItem.getVoucherImgThumbUrl())
                .asBitmap()
                .placeholder(mActivity.getResources().getColor(R.color.transparent_dark))
                .error(R.drawable.ss_profile)
                .into(new SimpleTarget<Bitmap>(imgSize, imgSize) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mActivity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        progressBar.setVisibility(View.GONE);
                        imgVoucherIcon.setImageDrawable(circularBitmapDrawable); // Possibly runOnUiThread()
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mVoucherListItemList != null) {
            return mVoucherListItemList.size();
        }
        return 0;
    }

    public class VoucherListViewHolder extends RecyclerView.ViewHolder {
        float dX = 0;
        boolean hasMoved = false;
        private ViewGroup viewGroupItemContent;
        private ViewGroup viewGroupShare;
        private ViewGroup viewGroupDelete;
        private ImageView imgShare;
        private ImageView imgDelete;
        private ImageView imgVoucherIcon;
        private TextView lblVoucherName;
        private TextView lblVoucherDesc;
        private TextView lblVoucherExpiredDate;
        private ProgressBar progressBar;
        private TextView textViewStocks;
        private int position = 0;

        public VoucherListViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            viewGroupItemContent = (ViewGroup) itemView.findViewById(R.id.viewGroupItemContent);
            viewGroupItemContent.setFocusable(true);
            viewGroupItemContent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    int imgShareWidth = imgShare.getWidth();
                    int imgDeleteWidth = imgDelete.getWidth();
                    int duration = ApplicationHelper.getInstance().getApplication().getResources().getInteger(android.R.integer.config_shortAnimTime);

                    //Log.d("voucher event:", event.toString());
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = view.getX() - event.getRawX();
                            hasMoved = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float viewX = event.getRawX() + dX;

                            if (viewX > LIST_SWIPE_OFFSET_POINT || viewX < -LIST_SWIPE_OFFSET_POINT) {
                                hasMoved = true;
                                viewX = (viewX > 0) ?
                                        /*(viewX < imgShareWidth) ? viewX : imgShareWidth :*/
                                        0 : //delete this row and uncomment above line if want to show share button
                                        (viewX * -1 < imgDeleteWidth) ? viewX : -imgDeleteWidth;
                                view.animate()
                                        .x(viewX)
                                        .setDuration(0)
                                        .start();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (view.getX() == 0 && !hasMoved) { //On Click
                                //Toast.makeText(mActivity, "view group clicked", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = ((AppCompatActivity) mActivity).getSupportFragmentManager().beginTransaction();
                                Fragment fragment;
                                fragment = new VoucherFragment();
                                Bundle args1 = new Bundle();
                                ArrayList<VoucherItem> listParam = new ArrayList<>(mVoucherListItemList);
                                args1.putParcelableArrayList(VoucherFragment.TAG_PARAM_VOUCHER_LIST, listParam);
                                args1.putInt(VoucherFragment.TAG_PARAM_SELECTED_VOUCHER, getAdapterPosition());
                                fragment.setArguments(args1);
                                ft.replace(R.id.frame_home_member_content, fragment, VoucherFragment.TAG_LOG);
                                ft.addToBackStack(VoucherFragment.TAG_LOG);
                                ft.commit();

                            } else if (view.getX() > LIST_SWIPE_OFFSET_POINT && view.getX() <= imgShareWidth / 2
                                    || view.getX() < LIST_SWIPE_OFFSET_POINT && view.getX() * -1 <= imgDeleteWidth / 2) {
                                view.animate()
                                        .x(0)
                                        .setDuration(duration)
                                        .start();
                            } else {
                                if (view.getX() > LIST_SWIPE_OFFSET_POINT) {
                                    view.animate()
                                            .x(imgShareWidth)
                                            .setDuration(duration)
                                            .start();
                                } else if (view.getX() < -LIST_SWIPE_OFFSET_POINT) {
                                    view.animate()
                                            .x(-imgDeleteWidth)
                                            .setDuration(duration)
                                            .start();
                                }
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            if (view.getX() > LIST_SWIPE_OFFSET_POINT && view.getX() <= imgShareWidth / 2
                                    || view.getX() < LIST_SWIPE_OFFSET_POINT && view.getX() * -1 <= imgDeleteWidth / 2) {
                                view.animate()
                                        .x(0)
                                        .setDuration(duration)
                                        .start();
                            } else {
                                if (view.getX() > LIST_SWIPE_OFFSET_POINT) {
                                    view.animate()
                                            .x(imgShareWidth)
                                            .setDuration(duration)
                                            .start();
                                } else if (view.getX() < -LIST_SWIPE_OFFSET_POINT) {
                                    view.animate()
                                            .x(-imgDeleteWidth)
                                            .setDuration(duration)
                                            .start();
                                }
                            }
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
            viewGroupShare = (ViewGroup) itemView.findViewById(R.id.viewGroupShare);

            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            imgShare.setFocusable(true);
            imgShare.setClickable(true);
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mActivity, "share clicked", Toast.LENGTH_SHORT).show();
                }
            });
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgDelete.setFocusable(true);
            imgDelete.setClickable(true);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogHelper.getInstance().showDialogBasicCustomFragment(
                            mActivity,
                            "",
                            mActivity.getString(R.string.dialog_voucher_delete_confirm_message),
                            mActivity.getString(R.string.dialog_yes_btn),
                            mActivity.getString(R.string.dialog_no_btn),
                            false,
                            new DialogFragmentTwoButtonListener() {
                                @Override
                                public void onBtnPositiveClick(DialogFragment dialogFragment) {
                                    viewGroupItemContent.animate()
                                            .x(0)
                                            .setDuration(0)
                                            .start();
                                    //delete voucher from server
                                    ServerManager.getInstance().doDeleteSavedVoucher(
                                            PreferenceManagerCustom.getInstance().getUserAuthToken(),
                                            mVoucherListItemList.get(getAdapterPosition()).getVoucherId(),
                                            mServerManagerListener
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
            imgVoucherIcon = (ImageView) itemView.findViewById(R.id.imgVoucherItemIcon);
            lblVoucherName = (TextView) itemView.findViewById(R.id.lblVoucherItemName);
            lblVoucherDesc = (TextView) itemView.findViewById(R.id.lblVoucherItemDesc);
            lblVoucherExpiredDate = (TextView) itemView.findViewById(R.id.lblVoucherItemExpiredDate);
            textViewStocks = (TextView) itemView.findViewById(R.id.textviewStock);
        }

        public TextView getTextViewStocks() {
            return textViewStocks;
        }

        public void setTextViewStocks(TextView textViewStocks) {
            this.textViewStocks = textViewStocks;
        }

        public ImageView getImgVoucherIcon() {
            return imgVoucherIcon;
        }

        public void setImgVoucherIcon(ImageView imgVoucherIcon) {
            this.imgVoucherIcon = imgVoucherIcon;
        }

        public TextView getLblVoucherName() {
            return lblVoucherName;
        }

        public void setLblVoucherName(TextView lblVoucherName) {
            this.lblVoucherName = lblVoucherName;
        }

        public TextView getLblVoucherDesc() {
            return lblVoucherDesc;
        }

        public void setLblVoucherDesc(TextView lblVoucherDesc) {
            this.lblVoucherDesc = lblVoucherDesc;
        }

        public TextView getLblVoucherExpiredDate() {
            return lblVoucherExpiredDate;
        }

        public void setLblVoucherExpiredDate(TextView lblVoucherExpiredDate) {
            this.lblVoucherExpiredDate = lblVoucherExpiredDate;
        }

        public ImageView getImgDelete() {
            return imgDelete;
        }

        public void setImgDelete(ImageView imgDelete) {
            this.imgDelete = imgDelete;
        }

        public ViewGroup getViewGroupItemContent() {
            return viewGroupItemContent;
        }

        public void setViewGroupItemContent(ViewGroup viewGroupItemContent) {
            this.viewGroupItemContent = viewGroupItemContent;
        }

        public ImageView getImgShare() {
            return imgShare;
        }

        public void setImgShare(ImageView imgShare) {
            this.imgShare = imgShare;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }
    }
}
