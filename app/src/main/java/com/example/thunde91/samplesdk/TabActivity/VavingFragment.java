package com.example.thunde91.samplesdk.TabActivity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.thunde91.samplesdk.R;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.listener.OnCloseFragmentListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VavingFragment extends AppCompatActivity implements OnCloseFragmentListener {


    private OnCloseFragmentListener onCloseFragmentListener;


    public static VavingFragment newInstance() {

        VavingFragment fragment = new VavingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaving_page);
        ButterKnife.bind(this);
        //setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);

        /*this.onCloseFragmentListener = new OnCloseFragmentListener(){
            public void onCloseFragment(){
                    Log.e("FragmentClose","Y");
                    finish();
            }
        };*/

        try{
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft2 = fragmentManager.beginTransaction();
            VavDirectGenerator.getInstance().goToVavingFragment(getFragmentManager().beginTransaction(), R.id.frame_home_member_content_full,this.onCloseFragmentListener);

        }catch (Exception e){

        }

    }

    /*@Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }*/

    /*@Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vaving_page, container, false);
        ButterKnife.bind(this, view);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        VavDirectGenerator.getInstance().goToVoucherBook(ft2, R.id.frame_home_member_content,this);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabActivity callingActivity = (TabActivity) getActivity();
                callingActivity.onSelected("insert selected value here");
                dismiss();

            }
        });
        return view;
    }*/


    @Override
    public void onResume() {
        super.onResume();
        //presenter.onResume();
        //bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //presenter.onPause();
        //bus.unregister(this);
    }

    @Override
    public void onCloseFragment(){
        Log.e("FragmentClose","Y");
        finish();
     }

}

