package com.vav.cn.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.util.PreferenceManagerCustom;

/**
 * Created by Handrata Samsul on 2/11/2016.
 */
public class SettingsVoucherFragment extends Fragment {
    public static final String TAG_LOG = SettingsVoucherFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;

    private ImageView mToggleSortByAlphabet;
    private ImageView mToggleSortByExpireDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.settings_voucher, container, false);

        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderLeft.setImageResource(R.drawable.header_btn_back);
        mBtnHeaderLeft.setVisibility(View.VISIBLE);
        mBtnHeaderLeft.setFocusable(true);
        mBtnHeaderLeft.setClickable(true);
        mBtnHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentByTag(TAG_LOG);
                if (fragment != null) {
                    ft.remove(fragment);
                    ft.commit();
                }
            }
        });
        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.settings_voucher_title);
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);

        mToggleSortByAlphabet = (ImageView) view.findViewById(R.id.toggleSortByAlphabet);
        mToggleSortByExpireDate = (ImageView) view.findViewById(R.id.toggleSortByExpireDate);

        mToggleSortByAlphabet.setFocusable(true);
        mToggleSortByAlphabet.setClickable(true);
        mToggleSortByAlphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSortByAlphabet();

            }
        });
        mToggleSortByExpireDate.setFocusable(true);
        mToggleSortByExpireDate.setClickable(true);
        mToggleSortByExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSortByExpireDate();
            }
        });

        if (PreferenceManagerCustom.getInstance().getVoucherSortByType() == PreferenceManagerCustom.VOUCHER_SORT_TYPE.ALPHABET) {
            mToggleSortByAlphabet.setSelected(true);
            mToggleSortByExpireDate.setSelected(false);
        } else {
            mToggleSortByAlphabet.setSelected(false);
            mToggleSortByExpireDate.setSelected(true);
        }

        return view;
    }

    public void switchToSortByAlphabet() {
        mToggleSortByAlphabet.setSelected(true);
        mToggleSortByExpireDate.setSelected(false);

        PreferenceManagerCustom.getInstance().setVoucherSortByType(PreferenceManagerCustom.VOUCHER_SORT_TYPE.ALPHABET);

        Toast.makeText(getActivity(), "Settings saved", Toast.LENGTH_SHORT).show();
    }

    public void switchToSortByExpireDate() {
        mToggleSortByAlphabet.setSelected(false);
        mToggleSortByExpireDate.setSelected(true);

        PreferenceManagerCustom.getInstance().setVoucherSortByType(PreferenceManagerCustom.VOUCHER_SORT_TYPE.EXPIRE_DATE);

        Toast.makeText(getActivity(), "Settings saved", Toast.LENGTH_SHORT).show();
    }
}
