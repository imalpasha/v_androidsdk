package com.vav.cn;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.fragment.HomeFragment;
import com.vav.cn.fragment.ProfileFragment;
import com.vav.cn.fragment.SearchFragment;
import com.vav.cn.fragment.SettingsFragment;
import com.vav.cn.fragment.VavFragment;
import com.vav.cn.fragment.VoucherListFragment;
import com.vav.cn.gcm.GcmHelper;
import com.vav.cn.gcm.GcmListener;
import com.vav.cn.gcm.RegistrationIntentService;
import com.vav.cn.listener.DialogFragmentTwoButtonListener;
import com.vav.cn.server.ServerManager;
import com.vav.cn.server.listener.ServerManagerListener;
import com.vav.cn.server.model.DeleteVoucherResponse;
import com.vav.cn.server.model.GetDetailResponse;
import com.vav.cn.server.model.GetHomeListResponse;
import com.vav.cn.server.model.GetProfileResponse;
import com.vav.cn.server.model.GetVoucherListResponse;
import com.vav.cn.server.model.LoginResponse;
import com.vav.cn.server.model.LogoutResponse;
import com.vav.cn.server.model.SaveVoucherResponse;
import com.vav.cn.server.model.SignUpResponse;
import com.vav.cn.server.model.TermsResponse;
import com.vav.cn.server.model.UpdateProfileResponse;
import com.vav.cn.server.model.UseVoucherResponse;
import com.vav.cn.server.model.VavingResponse;
import com.vav.cn.util.BackgroundIndicatorUtils;
import com.vav.cn.util.DialogHelper;
import com.vav.cn.util.DirectToSplashScreenHelper;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.PreferenceManagerCustom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handrata Samsul on 1/12/2016.
 */
public class HomeMemberActivity extends AppCompatActivity implements ServerManagerListener {
    private static final String TAG_LOG = HomeMemberActivity.class.getSimpleName();
    private boolean doubleBackToExitPressedOnce = false;

    private ViewGroup mViewGroupMenuHome;
    private ViewGroup mViewGroupMenuSearch;
    private ViewGroup mViewGroupMenuVav;
    private ViewGroup mViewGroupMenuVoucher;
    private ViewGroup mViewGroupMenuProfile;

    private List<ImageView> mBottomMenuImageList;
    private ImageView mImgMenuHome;
    private ImageView mImgMenuSearch;
    private ImageView mImgMenuVav;
    private ImageView mImgMenuVoucher;
    private ImageView mImgMenuProfile;
    private Fragment currentFragment;
    private int mCurrentActiveMenu = -1;

    private CustomDialog mProgressDialog;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PreferenceManagerCustom.getInstance().getIsWentThroughSplash()) {
            Intent mainIntent = new Intent(HomeMemberActivity.this, SplashScreen.class);
            HomeMemberActivity.this.startActivity(mainIntent);
            HomeMemberActivity.this.finish();
        } else {
            setContentView(R.layout.home_member);
            initializeView();
            logUserForCrashlytics();
            initializeGcm();
            directToPlayStore(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        directToPlayStore(intent);
    }

    private void directToPlayStore(Intent intent) {
        if (intent.getExtras() != null) {
            type = intent.getExtras().getString("type");
            if (type != null && type.equals("update")) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment;
        String action = item.getTitle().toString();
        if (action.equals(getString(R.string.action_logout))) {
            DialogHelper.getInstance().showDialogBasicCustomFragment(
                    HomeMemberActivity.this,
                    getString(R.string.dialog_log_out_title),
                    getString(R.string.dialog_log_out_message),
                    getString(R.string.dialog_log_out_btn_positive),
                    getString(R.string.dialog_log_out_btn_negative),
                    false,
                    new DialogFragmentTwoButtonListener() {
                        @Override
                        public void onBtnPositiveClick(DialogFragment dialogFragment) {
                            doLogOut();
                            dialogFragment.dismiss();
                        }

                        @Override
                        public void onBtnNegativeClick(DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                        }
                    });
        } else if (action.equals(getString(R.string.action_settings))) {
            fragment = new SettingsFragment();
            ft.add(R.id.frame_home_member_content, fragment, SettingsFragment.TAG_LOG);
            ft.addToBackStack(SettingsFragment.TAG_LOG);
            ft.commit();

        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (currentFragment != null) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GcmHelper.getInstance().registerRegistrationReceiver(this);
    }

    @Override
    protected void onPause() {
        GcmHelper.getInstance().unregisterRegistrationReceiver(this);
        BackgroundIndicatorUtils.getInstance().setIsFromBackground(true);
        super.onPause();
    }

    private void initializeView() {

        mViewGroupMenuHome = (ViewGroup) findViewById(R.id.viewGroupMenuHome);
        mViewGroupMenuSearch = (ViewGroup) findViewById(R.id.viewGroupMenuSearch);
        mViewGroupMenuVav = (ViewGroup) findViewById(R.id.viewGroupMenuVav);
        mViewGroupMenuVoucher = (ViewGroup) findViewById(R.id.viewGroupMenuVoucher);
        mViewGroupMenuProfile = (ViewGroup) findViewById(R.id.viewGroupMenuProfile);

        mViewGroupMenuHome.setFocusable(true);
        mViewGroupMenuHome.setClickable(true);
        mViewGroupMenuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activateMenu(0)) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    currentFragment = homeFragment;
                    ft.replace(R.id.frame_home_member_content, homeFragment, HomeFragment.TAG_LOG);
                    //ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        mViewGroupMenuSearch.setFocusable(true);
        mViewGroupMenuSearch.setClickable(true);
        mViewGroupMenuSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activateMenu(1)) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SearchFragment searchFragment = new SearchFragment();
                    currentFragment = searchFragment;
                    ft.replace(R.id.frame_home_member_content, searchFragment, SearchFragment.TAG_LOG);
                    //ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        mViewGroupMenuVav.setFocusable(true);
        mViewGroupMenuVav.setClickable(true);
        mViewGroupMenuVav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                VavFragment vavFragment = new VavFragment();
                currentFragment = vavFragment;
                ft.replace(R.id.frame_home_member_content_full, vavFragment, VavFragment.TAG_LOG);
                //ft.addToBackStack(null);
                ft.commit();
            }
        });
        mViewGroupMenuVoucher.setFocusable(true);
        mViewGroupMenuVoucher.setClickable(true);
        mViewGroupMenuVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activateMenu(3)) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    VoucherListFragment voucherFragment = new VoucherListFragment();
                    ft.replace(R.id.frame_home_member_content, voucherFragment, VoucherListFragment.TAG_LOG);
                    //ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        mViewGroupMenuProfile.setFocusable(true);
        mViewGroupMenuProfile.setClickable(true);
        mViewGroupMenuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activateMenu(4)) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ProfileFragment profileFragment = new ProfileFragment();
                    currentFragment = profileFragment;
                    ft.replace(R.id.frame_home_member_content, profileFragment, ProfileFragment.TAG_LOG);
                    //ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        mImgMenuHome = (ImageView) findViewById(R.id.imgMenuHome);
        mImgMenuSearch = (ImageView) findViewById(R.id.imgMenuSearch);
        mImgMenuVav = (ImageView) findViewById(R.id.imgMenuVav);
        mImgMenuVoucher = (ImageView) findViewById(R.id.imgMenuVoucher);
        mImgMenuProfile = (ImageView) findViewById(R.id.imgMenuProfile);

        mBottomMenuImageList = new ArrayList<>();
        mBottomMenuImageList.add(mImgMenuHome);
        mBottomMenuImageList.add(mImgMenuSearch);
        mBottomMenuImageList.add(mImgMenuVav);
        mBottomMenuImageList.add(mImgMenuVoucher);
        mBottomMenuImageList.add(mImgMenuProfile);

        activateMenu(0);

        HomeFragment homeFragment = new HomeFragment();
        currentFragment = homeFragment;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_home_member_content, homeFragment, HomeFragment.TAG_LOG);
        //ft.addToBackStack(null);
        ft.commit();
    }

    public boolean activateMenu(int menuNumber) {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (menuNumber == mCurrentActiveMenu) return false;
        for (int i = 0; i < mBottomMenuImageList.size(); i++) {
            if (menuNumber != i) mBottomMenuImageList.get(i).setSelected(false);
            else mBottomMenuImageList.get(i).setSelected(true);
        }

        mCurrentActiveMenu = menuNumber;

        return true;
    }

    private void doLogOut() {
        //All is valid
        mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(this);

        //log out from server, if success, empty all data on phone
        ServerManager.getInstance().doLogOut(
                PreferenceManagerCustom.getInstance().getUserGuid(),
                PreferenceManagerCustom.getInstance().getUserAuthToken(),
                this);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.SELECT_PHOTO_GALLERY_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String filePath = GeneralUtil.getInstance().saveImageInExternalCacheDir(HomeMemberActivity.this, selectedImage, Config.TEMP_FILE_NAME);

                        Intent intent = new Intent(HomeMemberActivity.this, CropImageActivity.class);
                        intent.putExtra(CropImageActivity.IMAGE_PATH_KEY, filePath);
                        startActivityForResult(intent, Config.CROP_IMAGE_CODE);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case Config.SELECT_PHOTO_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String filePath = GeneralUtil.getInstance().saveImageInExternalCacheDir(HomeMemberActivity.this, imageBitmap, Config.TEMP_FILE_NAME);

                    Intent intent = new Intent(HomeMemberActivity.this, CropImageActivity.class);
                    intent.putExtra(CropImageActivity.IMAGE_PATH_KEY, filePath);
                    startActivityForResult(intent, Config.CROP_IMAGE_CODE);
                }
                break;
            case Config.CROP_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    File image = new File(PreferenceManagerCustom.getInstance().getUserProfilePictPath());
                    ServerManager.getInstance().doUpdateProfilePhoto(PreferenceManagerCustom.getInstance().getUserAuthToken(), image, this);
                    FragmentManager fm = getFragmentManager();
                    Fragment fragment = fm.findFragmentByTag(ProfileFragment.TAG_LOG);
                    if (fragment instanceof ProfileFragment) {
                        ((ProfileFragment) fragment).showProgressDialog();

//						Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
//						((ProfileFragment)fragment).getImgProfilePict().setImageBitmap(bitmap);
                    }
                }
                break;
        }
    }

    private void initializeGcm() {
        if (!PreferenceManagerCustom.getInstance().getSentTokenToServer()) {
            if (GeneralUtil.getInstance().checkPlayServices(this)) {
                GcmHelper.getInstance().initializeBroadcastReceiver(new GcmListener() {
                    @Override
                    public void onRegistrationSucceed(Intent intent) {
                        if (PreferenceManagerCustom.getInstance().getGcmToken() != null) {
                            ServerManager.getInstance().doSendToken(PreferenceManagerCustom.getInstance().getUserAuthToken(), PreferenceManagerCustom.getInstance().getGcmToken(), HomeMemberActivity.this);
                        }
                    }

                    @Override
                    public void onRegistrationError() {

                    }
                });

                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private void logUserForCrashlytics() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
//		Crashlytics.setUserIdentifier(PreferenceManagerCustom.getInstance().getUserAuthToken());
//		Crashlytics.setUserEmail(PreferenceManagerCustom.getInstance().getUserEmail());
//		Crashlytics.setUserName(PreferenceManagerCustom.getInstance().getUserName());
    }

    @Override
    public void onSignUpCallback(SignUpResponse signUpResponse, boolean success) {

    }

    @Override
    public void onLoginCallback(LoginResponse loginResponse, boolean success) {

    }

    @Override
    public void onLogoutCallback(final LogoutResponse logoutResponse, boolean success) {
        mProgressDialog.dismiss();
        if (success) {
            if (logoutResponse.getStatus().equals("success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplicationHelper.getInstance().getApplication(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        //empty shared preference
                        PreferenceManagerCustom.getInstance().clearPreferences();

                        //go to homescreen
                        Intent logOutIntent = new Intent(HomeMemberActivity.this, HomeGuestActivity.class);
                        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logOutIntent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public void onDeleteVoucherCallback(DeleteVoucherResponse deleteVoucherResponse, boolean success) {

    }

    @Override
    public void onGetDetailCallback(GetDetailResponse getDetailResponse, boolean success) {

    }

    @Override
    public void onGetHomeListCallback(GetHomeListResponse getHomeListResponse, boolean success) {

    }

    @Override
    public void onGetProfileCallback(GetProfileResponse getProfileResponse, boolean success) {

    }

    @Override
    public void onGetVoucherListCallback(GetVoucherListResponse getVoucherListResponse, boolean success) {

    }

    @Override
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(ProfileFragment.TAG_LOG);
        if (fragment instanceof ProfileFragment) {
            ((ProfileFragment) fragment).onUpdateProfileCallback(updateProfileResponse, success);
        }
    }

    @Override
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (this == null) {
            return;
        }
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(this);
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        HomeMemberActivity.this,
                        status,
                        message,
                        true
                );
            }
        });
    }

    @Override
    public void onTermsConditionCallback(TermsResponse termsResponse, boolean isSuccess) {

    }

    @Override
    public void onSendTokenCallback(boolean isSuccess) {
        PreferenceManagerCustom.getInstance().setSentTokenToServer(isSuccess);
    }
}
