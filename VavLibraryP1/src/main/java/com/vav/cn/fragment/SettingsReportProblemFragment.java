package com.vav.cn.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vav.cn.R;
import com.vav.cn.util.GeneralUtil;

/**
 * Created by Handrata Samsul on 2/3/2016.
 */
public class SettingsReportProblemFragment extends Fragment {
    public static final String TAG_LOG = SettingsReportProblemFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;

    private Spinner mSpinnerTopic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.settings_report_problem_fragment, container, false);

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
                if (fragment instanceof SettingsReportProblemFragment) {
                    ft.remove(fragment);
                    ft.commit();
                }
            }
        });

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(R.string.settings_support_report_title);

        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);
        mBtnHeaderRight.setVisibility(View.INVISIBLE);


        mSpinnerTopic = (Spinner) view.findViewById(R.id.spinnerTopic);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.settings_support_report_topic_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTopic.setAdapter(spinnerAdapter);

        return view;
    }
}
