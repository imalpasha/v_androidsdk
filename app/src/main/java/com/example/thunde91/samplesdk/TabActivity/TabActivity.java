package com.example.thunde91.samplesdk.TabActivity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thunde91.samplesdk.R;
import com.vav.cn.listener.OnCloseFragmentListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TabActivity extends AppCompatActivity implements OnCloseFragmentListener {



    @Bind(R.id.btnVaving)
    Button btnVaving;

    @Bind(R.id.btnCoupon)
    Button btnCoupon;

    @Bind(R.id.btnLogout)
    Button btnLogout;


    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vav_page);
        ButterKnife.bind(this);

        btnVaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //frame_home_member_content.setVisibility(View.VISIBLE);

                Intent intent = new Intent(TabActivity.this, VavingFragment.class);
                startActivity(intent);

                //FragmentManager fragmentManager = getFragmentManager();
                //FragmentTransaction ft2 = fragmentManager.beginTransaction();
                //fragment = VavDirectGenerator.getInstance().goToVavingFragment(ft2, R.id.frame_home_member_content, TabActivity.this);
                //Intent intent = new Intent(TabActivity.this, VavingFragment.class);
                //startActivity(intent);

            }
        });

        btnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FragmentTransaction ft = TabActivity.this.getFragmentManager().beginTransaction();
                //VavFragment vavFragment = new VavFragment();
                //TabActivity.this.fragment = vavFragment;
                //ft.replace(R.id.frame_home_member_content, vavFragment, "VAV_Frag");
                //ft.commit();

                //here-
                //FragmentManager fragmentManager = getFragmentManager();
                //FragmentTransaction ft2 = fragmentManager.beginTransaction();
                //fragment = VavDirectGenerator.getInstance().goToVoucherBook(ft2, R.id.frame_home_member_content, TabActivity.this);

                //FragmentTransaction ft2 = TabActivity.beginTransaction();

                Intent intent = new Intent(TabActivity.this, CouponFragment.class);
                startActivity(intent);

                //}
                //FragmentManager fragmentManager = getFragmentManager();

                //FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                //if (prev != null) {
                //    ft.remove(prev);
                //}
                //ft.addToBackStack(null);


                //CouponFragment routeListDialogFragment = CouponFragment.newInstance();
                //routeListDialogFragment.show(ft, "countryListDialogFragment");

                // Create and show the dialog.
                //DialogFragment newFragment = VavingFragment.newInstance();
                //newFragment.show(getFragmentManager().beginTransaction(), "dialog");


                //v routeListDialogFragment = CustomPassengerPicker.newInstance(totalAdult, totalChild, totalInfant);
                //routeListDialogFragment.setTargetFragment(SearchFlightFragment.this, 0);
                //routeListDialogFragment.show(getFragmentManager(), "passenger_qty");


            }
        });
        btnLogout.setVisibility(View.GONE);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //doLogout();

            }
        });


    }

    /*public void doLogout() {

        VavDirectGenerator.getInstance().logout(new LogoutCallback() {
            @Override
            public void onLogoutSuccess() {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("just_login", false);
                editor.apply();

                new SweetAlertDialog(TabActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success.")
                        .setContentText("Successfully Logout")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(TabActivity.this, HomeActivity.class);
                                TabActivity.this.startActivity(intent);
                                TabActivity.this.finish();
                                sDialog.dismiss();
                            }
                        })
                        .show();

            }

            @Override
            public void onLogoutFailure(ErrorInfo errorInfo) {

                new SweetAlertDialog(TabActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error.")
                        .setContentText("Failed To Logout")
                        .show();

            }
        });

    }*/

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final String sender;
        try {
            sender= getIntent().getExtras().getString("TEST");
            //this.receiveData();
            Toast.makeText(this, sender, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCloseFragment() {
        Log.e("FragmentClose", "Y");
        finish();
    }

    public void onSelected(String text) {
        Log.e("Passed", text);
    }


}
