package com.vav.cn;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.vav.cn.fragment.SignUpFragment;

/**
 * Created by Handrata Samsul on 1/11/2016.
 */
public class SignUpActivity extends AppCompatActivity {
    SignUpFragment signUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUpFragment = new SignUpFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_sign_up, signUpFragment, SignUpFragment.TAG_LOG);
        //ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (signUpFragment != null) {
            signUpFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (signUpFragment != null) {
            signUpFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
