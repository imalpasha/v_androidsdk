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

import com.vav.cn.R;
import com.vav.cn.util.GeneralUtil;

/**
 * Created by Handrata Samsul on 1/27/2016.
 */
public class SettingsFragment extends Fragment {
    public static final String TAG_LOG = SettingsFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;

    private ViewGroup mViewGroupNotification;
    private ViewGroup mViewGroupVoucher;
    private ViewGroup mViewGroupStamp;

    private ViewGroup mViewGroupPrivacy;
    private ViewGroup mViewGroupTerms;

    private ViewGroup mViewGroupHelp;
    private ViewGroup mViewGroupReport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

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
                GeneralUtil.getInstance().closeSoftKeyboard(getActivity());
                Fragment fragment = fm.findFragmentByTag(TAG_LOG);
                if (fragment instanceof SettingsFragment) {
                    ft.remove(fragment);
                    ft.commit();
                }
            }
        });

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.settings_title);

        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);

        mViewGroupNotification = (ViewGroup) view.findViewById(R.id.viewGroupNotification);
        mViewGroupNotification.setFocusable(true);
        mViewGroupNotification.setClickable(true);
        mViewGroupNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mViewGroupVoucher = (ViewGroup) view.findViewById(R.id.viewGroupVoucher);
        mViewGroupVoucher.setFocusable(true);
        mViewGroupVoucher.setClickable(true);
        mViewGroupVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = new SettingsVoucherFragment();
                ft.add(R.id.frame_home_member_content, fragment, SettingsVoucherFragment.TAG_LOG);
                ft.addToBackStack(SettingsVoucherFragment.TAG_LOG);
                ft.commit();

            }
        });
        mViewGroupStamp = (ViewGroup) view.findViewById(R.id.viewGroupStamp);
        mViewGroupStamp.setFocusable(true);
        mViewGroupStamp.setClickable(true);
        mViewGroupStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mViewGroupPrivacy = (ViewGroup) view.findViewById(R.id.viewGroupPrivacy);
        mViewGroupPrivacy.setFocusable(true);
        mViewGroupPrivacy.setClickable(true);
        mViewGroupPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mViewGroupTerms = (ViewGroup) view.findViewById(R.id.viewGroupTerms);
        mViewGroupTerms.setFocusable(true);
        mViewGroupTerms.setClickable(true);
        mViewGroupTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mViewGroupHelp = (ViewGroup) view.findViewById(R.id.viewGroupHelp);
        mViewGroupHelp.setFocusable(true);
        mViewGroupHelp.setClickable(true);
        mViewGroupHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mViewGroupReport = (ViewGroup) view.findViewById(R.id.viewGroupReport);
        mViewGroupReport.setFocusable(true);
        mViewGroupReport.setClickable(true);
        mViewGroupReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //				fragment = new SettingsReportProblemFragment();
//				ft.add(R.id.frame_home_member_content, fragment, SettingsReportProblemFragment.TAG_LOG);
//				ft.addToBackStack(SettingsReportProblemFragment.TAG_LOG);
//				ft.commit();
            }
        });

        //START GRAYING OUT NO UI OPTION
        ((TextView) mViewGroupNotification.findViewById(R.id.lblNotification)).setTextColor(getActivity().getResources().getColor(R.color.light_gray));
        ((TextView) mViewGroupStamp.findViewById(R.id.lblStamp)).setTextColor(getActivity().getResources().getColor(R.color.light_gray));
        ((TextView) mViewGroupPrivacy.findViewById(R.id.lblPrivacy)).setTextColor(getActivity().getResources().getColor(R.color.light_gray));
        ((TextView) mViewGroupTerms.findViewById(R.id.lblTerms)).setTextColor(getActivity().getResources().getColor(R.color.light_gray));
        ((TextView) mViewGroupHelp.findViewById(R.id.lblHelp)).setTextColor(getActivity().getResources().getColor(R.color.light_gray));
        ((TextView) mViewGroupReport.findViewById(R.id.lblReport)).setTextColor(getActivity().getResources().getColor(R.color.light_gray));
        //END GRAYING OUT NO UI OPTION

        return view;
    }
}
