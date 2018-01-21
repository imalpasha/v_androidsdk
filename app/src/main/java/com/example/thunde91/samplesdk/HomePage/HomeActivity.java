package com.example.thunde91.samplesdk.HomePage;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.thunde91.samplesdk.R;
import com.example.thunde91.samplesdk.TabActivity.TabActivity;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.listener.LoginCallback;
import com.vav.cn.listener.OnCloseFragmentListener;
import com.vav.cn.model.DeploymentType;
import com.vav.cn.model.ErrorInfo;
import com.vav.cn.server.model.AuthData;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {


    @Bind(R.id.authToken)
    EditText txtToken;

    @Bind(R.id.guid)
    EditText guid;

    @Bind(R.id.referralCode)
    EditText referralCode;

    @Bind(R.id.btnLogin)
    Button btnLogin;


    private Fragment fragment;
    DeploymentType deploymentType = DeploymentType.DEVELOPMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ButterKnife.bind(this);

        ApplicationHelper.getInstance().setCurrentApplication(getApplication());
        VavDirectGenerator.initialize(deploymentType);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiateLoading(getActivity());
                doLogin();
            }
        });

    }


    public void doLogin() {

        AuthData authData = new AuthData(txtToken.getText().toString(), guid.getText().toString(), referralCode.getText().toString());
        LoginCallback loginCallback = new LoginCallback() {
            @Override
            public void onLoginSuccess() {

                Log.e("Login","Success");
                //dismissLoading();


                //Toast.makeText(getActivity(), "SDK Login Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this, TabActivity.class);
                startActivity(intent);

            }
            @Override
            public void onLoginFailure(ErrorInfo errorInfo) {
                Log.e("Login","Failed");
                //dismissLoading();
            }
        };
        VavDirectGenerator.getInstance().login(authData, loginCallback);

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


}
