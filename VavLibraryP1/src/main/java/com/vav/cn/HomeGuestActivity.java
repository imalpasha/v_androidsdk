package com.vav.cn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vav.cn.util.Logger;
import com.vav.cn.util.PreferenceManagerCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class HomeGuestActivity extends AppCompatActivity {
    public static final String TAG_LOG = HomeGuestActivity.class.getSimpleName();
    public static CallbackManager mCallbackmanager;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView mLblTitle;
    private TextView mFacebookLogin;
    private TextView mNormalSignUp;
    private TextView mNormalLogin;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManagerCustom.getInstance().setIsWentThroughSplash(true);
        setContentView(R.layout.home_guest);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackmanager = CallbackManager.Factory.create();

        mFacebookLogin = (TextView) findViewById(R.id.btnFacebookLogin);
        mFacebookLogin.setFocusable(true);
        mFacebookLogin.setClickable(true);
        mFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(HomeGuestActivity.this, Arrays.asList("email", "user_photos", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackmanager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            String id = object.getString("id");
                                            String gender = object.getString("gender");

                                            Logger.getInstance().d(TAG_LOG, "Facebook name: " + name + " email:" + email + " id:" + id + " gender:" + gender);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        Logger.getInstance().d(TAG_LOG, "Facebook login success: ");
                    }

                    @Override
                    public void onCancel() {
                        Logger.getInstance().d(TAG_LOG, "Facebook login cancel");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Logger.getInstance().e(TAG_LOG, e.toString());
                    }
                });
            }
        });
        mNormalSignUp = (TextView) findViewById(R.id.btnNormalSignUp);
        mNormalSignUp.setFocusable(true);
        mNormalSignUp.setClickable(true);
        mNormalSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPageIntent = new Intent(HomeGuestActivity.this, SignUpActivity.class);
                startActivity(openPageIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        mNormalLogin = (TextView) findViewById(R.id.btnNormalLogIn);
        mNormalLogin.setFocusable(true);
        mNormalLogin.setClickable(true);
        mNormalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPageIntent = new Intent(HomeGuestActivity.this, LogInActivity.class);
                startActivity(openPageIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.please_click_back_to_exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
