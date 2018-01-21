package com.example.thunde91.samplesdk.TabActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.thunde91.samplesdk.R;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.listener.OnCloseFragmentListener;


public class CouponFragment extends AppCompatActivity implements OnCloseFragmentListener {

    private OnCloseFragmentListener onCloseFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaving_page);

        //FragmentManager fragmentManager = getFragmentManager();
        //FragmentTransaction ft2 = fragmentManager.beginTransaction();
        //VavDirectGenerator.getInstance().goToVoucherBook(ft2, R.id.frame_home_member_content,this);

        //onCloseFragmentListener = new OnCloseFragmentListener(){
        //    public void onCloseFragment(){
        //        Log.e("FragmentClose","Y");
        //        finish();
        //    }
        //};

        try{
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft2 = fragmentManager.beginTransaction();
            VavDirectGenerator.getInstance().goToVoucherBook(getFragmentManager().beginTransaction(), R.id.frame_home_member_content,this);
        }catch (Exception e){

        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCloseFragment(){
        Log.e("FragmentClose","Y");
        finish();
    }

}

