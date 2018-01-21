package com.vav.cn.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.vav.cn.R;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.fragment.DialogBasicCustomFragment;
import com.vav.cn.fragment.DialogChooseImageFragment;
import com.vav.cn.fragment.DialogVoucherResponseFragment;
import com.vav.cn.fragment.DialogVoucherUseFragment;
import com.vav.cn.listener.DialogChooseImageFragmentListener;
import com.vav.cn.listener.DialogFragmentSingleButtonListener;
import com.vav.cn.listener.DialogFragmentTwoButtonListener;
import com.vav.cn.listener.DialogSingleButtonListener;
import com.vav.cn.listener.DialogTwoButtonListener;

/**
 * Created by Handrata Samsul on 11/8/2015.
 */
public class DialogHelper {
    private static DialogHelper instance = new DialogHelper();
    private DialogSingleButtonListener mDialogSingleButtonListener;
    private DialogTwoButtonListener mDialogTwoButtonListener;

    public static DialogHelper getInstance() {
        return instance;
    }

    public CustomDialog showLoadingProgressDialog(Context context) {
        CustomDialog progressDialog = new CustomDialog(context);
        //show progress dialog using default settings
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_loading_message));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        return progressDialog;
    }

    public void showDialogChooseImage(final Activity activity) {
        String fragmentTag = DialogChooseImageFragment.LOG_TAG;
        final FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment removeDialog = fm.findFragmentByTag(fragmentTag);
        if (removeDialog == null) {
            // Create and show the dialog.
            final DialogChooseImageFragment dialogFragment = DialogChooseImageFragment.getInstance();
            dialogFragment.addListener(new DialogChooseImageFragmentListener() {
                @Override
                public void onTakePhotoClick() {
                    MarshmallowHelper.getInstance().handleCameraPermission(activity);
                    dialogFragment.dismiss();
                }

                @Override
                public void onChooseFromGalleryClick() {
                    MarshmallowHelper.getInstance().handleStoragePermissionGallery(activity);
                    dialogFragment.dismiss();
                }
            });
            if (activity != null && !activity.isFinishing())
                dialogFragment.show(ft, fragmentTag);
        }
    }

    public void showDialogBasicCustomFragment(final Activity activity, String title, String message, boolean canceledOnTouchOutside) {
        String fragmentTag = DialogBasicCustomFragment.LOG_TAG;
        final FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment removeDialog = fm.findFragmentByTag(fragmentTag);
        if (removeDialog == null) {
            // Create and show the dialog.
            final DialogBasicCustomFragment dialogFragment = DialogBasicCustomFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putString(DialogBasicCustomFragment.LABEL_TITLE, title);
            bundle.putString(DialogBasicCustomFragment.LABEL_MESSAGE, message);
            bundle.putBoolean(DialogBasicCustomFragment.CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
            dialogFragment.setArguments(bundle);
            if (activity != null && !activity.isFinishing())
                dialogFragment.show(ft, fragmentTag);
        }
    }

    public void showDialogBasicCustomFragment(final Activity activity, String title, String message, String positiveButton, boolean canceledOnTouchOutside, DialogFragmentSingleButtonListener listener) {
        String fragmentTag = DialogBasicCustomFragment.LOG_TAG;
        final FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment removeDialog = fm.findFragmentByTag(fragmentTag);
        if (removeDialog == null) {
            // Create and show the dialog.
            final DialogBasicCustomFragment dialogFragment = DialogBasicCustomFragment.getInstance();
            dialogFragment.addSingleButtonListener(listener);
            Bundle bundle = new Bundle();
            bundle.putString(DialogBasicCustomFragment.LABEL_TITLE, title);
            bundle.putString(DialogBasicCustomFragment.LABEL_MESSAGE, message);
            bundle.putString(DialogBasicCustomFragment.BUTTON_POSITIVE_LABEL_TAG, positiveButton);
            bundle.putBoolean(DialogBasicCustomFragment.CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
            dialogFragment.setArguments(bundle);
            if (activity != null && !activity.isFinishing())
                dialogFragment.show(ft, fragmentTag);
        }
    }

    public void showDialogBasicCustomFragment(final Activity activity, String title, String message, String positiveButton, String negativeButton, boolean canceledOnTouchOutside, DialogFragmentTwoButtonListener listener) {
        String fragmentTag = DialogBasicCustomFragment.LOG_TAG;
        final FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment removeDialog = fm.findFragmentByTag(fragmentTag);
        if (removeDialog == null) {
            // Create and show the dialog.
            final DialogBasicCustomFragment dialogFragment = DialogBasicCustomFragment.getInstance();
            dialogFragment.addTwoButtonListener(listener);
            Bundle bundle = new Bundle();
            bundle.putString(DialogBasicCustomFragment.LABEL_TITLE, title);
            bundle.putString(DialogBasicCustomFragment.LABEL_MESSAGE, message);
            bundle.putString(DialogBasicCustomFragment.BUTTON_POSITIVE_LABEL_TAG, positiveButton);
            bundle.putString(DialogBasicCustomFragment.BUTTON_NEGATIVE_LABEL_TAG, negativeButton);
            bundle.putBoolean(DialogBasicCustomFragment.CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
            dialogFragment.setArguments(bundle);
            if (activity != null && !activity.isFinishing())
                dialogFragment.show(ft, fragmentTag);
        }
    }

    public void showDialogVoucherUseFragment(final Activity activity, int offerId, String serialNo, boolean canceledOnTouchOutside, boolean isNeedHideFooter) {
        String fragmentTag = DialogVoucherUseFragment.LOG_TAG;
        final FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment removeDialog = fm.findFragmentByTag(fragmentTag);
        if (removeDialog == null) {
            // Create and show the dialog.
            final DialogVoucherUseFragment dialogFragment = DialogVoucherUseFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putBoolean(DialogVoucherUseFragment.CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
            bundle.putInt(DialogVoucherUseFragment.TAG_PARAM_OFFER_ID, offerId);
            bundle.putString(DialogVoucherUseFragment.TAG_PARAM_SERIAL_NO, serialNo);
            bundle.putBoolean(DialogVoucherUseFragment.TAG_FOOTER_HIDE, isNeedHideFooter);
            dialogFragment.setArguments(bundle);
            if (activity != null && !activity.isFinishing())
                dialogFragment.show(ft, fragmentTag);
        }
    }

    public void showDialogVoucherResponseFragment(final Activity activity, String serialNo, String responseText, boolean successEntry, Integer offerID, boolean canceledOnTouchOutside, boolean isNeedHideFooter) {
        String fragmentTag = DialogVoucherResponseFragment.LOG_TAG;
        final FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment removeDialog = fm.findFragmentByTag(fragmentTag);
        if (removeDialog == null) {
            // Create and show the dialog.
            final DialogVoucherResponseFragment dialogFragment = DialogVoucherResponseFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putBoolean(DialogVoucherResponseFragment.CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside);
            bundle.putString(DialogVoucherResponseFragment.TAG_PARAM_SERIAL_NO, serialNo);
            bundle.putBoolean(DialogVoucherResponseFragment.TAG_PARAM_SUCCESS_ENTRY, successEntry);
            bundle.putInt(DialogVoucherResponseFragment.TAG_PARAM_OFFER_ID, offerID);
            bundle.putString(DialogVoucherResponseFragment.RESPONSE_TEXT, responseText);
            bundle.putBoolean(DialogVoucherResponseFragment.TAG_FOOTER_HIDE, isNeedHideFooter);
            dialogFragment.setArguments(bundle);
            if (activity != null && !activity.isFinishing())
                dialogFragment.show(ft, fragmentTag);
        }
    }

    public void showSingleButtonBasicDialog(Activity activity, String title, String message, String btnPositive) {
        final AlertDialog.Builder bluetoothDialogBuilder = new AlertDialog.Builder(activity);
        bluetoothDialogBuilder.setTitle(title);
        bluetoothDialogBuilder.setMessage(message);
        bluetoothDialogBuilder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //checkBluetoothAvailability();
                dialog.dismiss();
            }
        });

        AlertDialog bluetoothDialog = bluetoothDialogBuilder.create();
        if (activity != null && !activity.isFinishing())
            bluetoothDialog.show();
    }

    public void showSingleButtonBasicDialog(Activity activity, String title, String message, String btnPositive, DialogSingleButtonListener dialogSingleButtonListener) {
        mDialogSingleButtonListener = dialogSingleButtonListener;
        final AlertDialog.Builder bluetoothDialogBuilder = new AlertDialog.Builder(activity);
        bluetoothDialogBuilder.setTitle(title);
        bluetoothDialogBuilder.setMessage(message);
        bluetoothDialogBuilder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogSingleButtonListener.onButtonClicked(dialog);
            }
        });

        AlertDialog bluetoothDialog = bluetoothDialogBuilder.create();
        if (activity != null && !activity.isFinishing())
            bluetoothDialog.show();
    }

    public void showTwoButtonBasicDialog(Activity activity, String title, String message, String btnPositive, String btnNegative, DialogTwoButtonListener dialogTwoButtonListener) {
        mDialogTwoButtonListener = dialogTwoButtonListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mDialogTwoButtonListener.onBtnPositiveClick(dialog);
            }
        });
        builder.setNegativeButton(btnNegative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mDialogTwoButtonListener.onBtnNegativeClick(dialog);
            }
        });
        if (activity != null && !activity.isFinishing())
            builder.show();
    }


}
