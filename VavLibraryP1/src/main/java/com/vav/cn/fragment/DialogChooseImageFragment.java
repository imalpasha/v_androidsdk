package com.vav.cn.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.vav.cn.R;
import com.vav.cn.listener.DialogChooseImageFragmentListener;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class DialogChooseImageFragment extends DialogFragment {
    public static final String LOG_TAG = DialogChooseImageFragment.class.getSimpleName();
    public static final String BUTTON_POSITIVE_LABEL_TAG = "BUTTON_POSITIVE_LABEL_TAG";
    public static final String BUTTON_NEGATIVE_LABEL_TAG = "BUTTON_NEGATIVE_LABEL_TAG";
    private static DialogChooseImageFragment instance = new DialogChooseImageFragment();
    private DialogChooseImageFragmentListener mDialogChooseImageFragmentListener;
    private ViewGroup mViewGroupDialogContainer;
    private TextView mBtnTakePhoto;
    private TextView mBtnChooseFromGallery;

    public static DialogChooseImageFragment getInstance() {
        return instance;
    }

    public void addListener(DialogChooseImageFragmentListener listener) {
        mDialogChooseImageFragmentListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_choose_image_picker, null);

        mViewGroupDialogContainer = (ViewGroup) view.findViewById(R.id.viewGroupDialogContainer);
        mViewGroupDialogContainer.setFocusable(true);
        mViewGroupDialogContainer.setClickable(true);
        mViewGroupDialogContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mBtnTakePhoto = (TextView) view.findViewById(R.id.btnTakePhoto);
        mBtnTakePhoto.setFocusable(true);
        mBtnTakePhoto.setClickable(true);
        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogChooseImageFragmentListener.onTakePhotoClick();
            }
        });
        mBtnChooseFromGallery = (TextView) view.findViewById(R.id.btnChooseFromGallery);
        mBtnChooseFromGallery.setFocusable(true);
        mBtnChooseFromGallery.setClickable(true);
        mBtnChooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogChooseImageFragmentListener.onChooseFromGalleryClick();
            }
        });

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setContentView(view);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        return dialog;
    }
}
