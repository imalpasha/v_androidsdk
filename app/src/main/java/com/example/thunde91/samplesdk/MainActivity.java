package com.example.thunde91.samplesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thunde91.samplesdk.TabActivity.TabActivity;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.listener.LoginCallback;
import com.vav.cn.model.DeploymentType;
import com.vav.cn.model.ErrorInfo;
import com.vav.cn.server.model.AuthData;

public class MainActivity extends AppCompatActivity implements LoginCallback {
    private Button buttonLogin;
    private static final String authToken = "d1e203fdc3c92698e8be";
    private static final String guid = "b3fca1a92044a0139df7";
    private static final String referralCode = "0123150007";
    DeploymentType deploymentType = DeploymentType.DEVELOPMENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.getInstance().setCurrentApplication(getApplication());
        VavDirectGenerator.initialize(deploymentType);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
        //directToCustomPages();
    }

    private void directToCustomPages() {
        if (VavDirectGenerator.getInstance().isUserLogin()) {
            Intent intent = new Intent(MainActivity.this, TabActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void doLogin() {
        //Construct authData ob ject. Other constructors are availab le based on your need
        AuthData authData = new AuthData(authToken, guid, referralCode); //if referralCode is not provided, can use
        //AuthData authData = new AuthData(authToken, guid);
        LoginCallback loginCallback = new LoginCallback() {
            @Override
            public void onLoginSuccess() {
                directToCustomPages();
                //put your codes here when login is successful
            }

            @Override
            public void onLoginFailure(ErrorInfo errorInfo) {
                showText("Login Failure");
            }
        };
        //Assuming you have defined a variable vavManager //that refers to the singleton ob ject of VAVManager vavManager.login(authData, loginCallback);
        VavDirectGenerator.getInstance().login(authData, loginCallback);
    }

    public void showText(final String message) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailure(ErrorInfo errorInfo) {

    }
}
