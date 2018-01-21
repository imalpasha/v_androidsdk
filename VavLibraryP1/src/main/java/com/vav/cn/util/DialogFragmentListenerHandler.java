package com.vav.cn.util;

import com.vav.cn.listener.DialogFragmentVoucherListener;

/**
 * Created by thunde91 on 3/6/16.
 */
public class DialogFragmentListenerHandler {
    private static DialogFragmentListenerHandler ourInstance = new DialogFragmentListenerHandler();
    private DialogFragmentVoucherListener dialogFragmentVoucherListener;
    private Integer offerID;

    private DialogFragmentListenerHandler() {
    }

    public static DialogFragmentListenerHandler getInstance() {
        return ourInstance;
    }

    public Integer getOfferID() {
        return offerID != null ? offerID : 0;
    }

    public void setOfferID(Integer offerID) {
        this.offerID = offerID;
    }

    public void setDialogFragmentVoucherListener(DialogFragmentVoucherListener dialogFragmentVoucherListener) {
        this.dialogFragmentVoucherListener = dialogFragmentVoucherListener;
    }

    public void publishToListener(boolean success, Integer offerID) {
        if (dialogFragmentVoucherListener != null) {
            dialogFragmentVoucherListener.onDialogFragmentCallback(success, offerID);
        }
    }
}
