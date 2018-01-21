package com.vav.cn.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.vav.cn.R;
import com.vav.cn.listener.DialogFragmentSingleButtonListener;
import com.vav.cn.listener.DialogFragmentTwoButtonListener;

/**
 * Created by Handrata Samsul on 1/20/2016.
 */
public class DialogBasicCustomFragment extends DialogFragment {
    public static final String LOG_TAG = DialogBasicCustomFragment.class.getSimpleName();
    public static final String LABEL_TITLE = "LABEL_TITLE";
    public static final String LABEL_MESSAGE = "LABEL_MESSAGE";
    public static final String BUTTON_POSITIVE_LABEL_TAG = "BUTTON_POSITIVE_LABEL_TAG";
    public static final String BUTTON_NEGATIVE_LABEL_TAG = "BUTTON_NEGATIVE_LABEL_TAG";
    public static final String CANCELED_ON_TOUCH_OUTSIDE = "CANCELED_ON_TOUCH_OUTSIDE";
    private static DialogBasicCustomFragment instance = new DialogBasicCustomFragment();
    private String mTitle;
    private String mMessage;
    private String mPositiveLabel;
    private String mNegativeLabel;
    private boolean mCanceledOnTouchOutside;
    private DialogFragmentTwoButtonListener mDialogTwoButtonListener;
    private DialogFragmentSingleButtonListener mDialogSingleButtonListener;
    private ViewGroup mViewGroupDialogContainer;
    private TextView mLblTitle;
    private TextView mLblMessage;
    private TextView mBtnPositive;
    private TextView mBtnNegative;

    public static DialogBasicCustomFragment getInstance() {
        return instance;
    }

    public void addSingleButtonListener(DialogFragmentSingleButtonListener listener) {
        mDialogSingleButtonListener = listener;
        mDialogTwoButtonListener = null;
    }

    public void addTwoButtonListener(DialogFragmentTwoButtonListener listener) {
        mDialogTwoButtonListener = listener;
        mDialogSingleButtonListener = null;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mTitle = args.getString(LABEL_TITLE, "");
        mMessage = args.getString(LABEL_MESSAGE, "");
        mPositiveLabel = args.getString(BUTTON_POSITIVE_LABEL_TAG, "");
        mNegativeLabel = args.getString(BUTTON_NEGATIVE_LABEL_TAG, "");
        mCanceledOnTouchOutside = args.getBoolean(CANCELED_ON_TOUCH_OUTSIDE, false);
    }

    @Override
    public int show(FragmentTransaction ft, String tag) {
        try {
            ft.add(this, tag);
            ft.commit();
            return 0;
        } catch (Exception e) {
            Log.d("DialogBasic", "Exception", e);
        }
        return 1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_basic_custom, null);

        mViewGroupDialogContainer = (ViewGroup) view.findViewById(R.id.viewGroupDialogContainer);
        mViewGroupDialogContainer.setFocusable(true);
        mViewGroupDialogContainer.setClickable(true);
        if (mCanceledOnTouchOutside)
            mViewGroupDialogContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        mLblTitle = (TextView) view.findViewById(R.id.lblTitle);
        if (!mTitle.equals("")) mLblTitle.setText(mTitle);
        else mLblTitle.setVisibility(View.GONE);

        mLblMessage = (TextView) view.findViewById(R.id.lblMessage);
        if (!mMessage.equals("")) mLblMessage.setText(mMessage);
        else mLblMessage.setVisibility(View.GONE);

        mBtnPositive = (TextView) view.findViewById(R.id.btnPositive);
        mBtnPositive.setFocusable(true);
        mBtnPositive.setClickable(true);
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogTwoButtonListener != null) {
                    mDialogTwoButtonListener.onBtnPositiveClick(DialogBasicCustomFragment.this);
                } else if (mDialogSingleButtonListener != null) {
                    mDialogSingleButtonListener.onBtnClick(DialogBasicCustomFragment.this);
                }
            }
        });
        if (!mPositiveLabel.equals("")) mBtnPositive.setText(mPositiveLabel);
        else mBtnPositive.setVisibility(View.GONE);

        mBtnNegative = (TextView) view.findViewById(R.id.btnNegative);
        mBtnNegative.setFocusable(true);
        mBtnNegative.setClickable(true);
        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogTwoButtonListener != null) {
                    mDialogTwoButtonListener.onBtnNegativeClick(DialogBasicCustomFragment.this);
                }
            }
        });
        if (!mNegativeLabel.equals("")) mBtnNegative.setText(mNegativeLabel);
        else mBtnNegative.setVisibility(View.GONE);

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setContentView(view);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        return dialog;
    }
}
