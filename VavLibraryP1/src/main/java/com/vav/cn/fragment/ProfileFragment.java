package com.vav.cn.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.HomeGuestActivity;
import com.vav.cn.R;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.gcm.GcmHelper;
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
import com.vav.cn.util.DialogHelper;
import com.vav.cn.util.DirectToSplashScreenHelper;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.MediaOpenHelper;
import com.vav.cn.util.PreferenceManagerCustom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class ProfileFragment extends Fragment implements ServerManagerListener {
    public static final String TAG_LOG = ProfileFragment.class.getSimpleName();

    private ImageView mBtnHeaderLeft;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderRight;

    private ImageView mImgProfilePict;
    private TextView mLblUserName;
    private TextView mLblUserEmail;
    private ImageView mBtnSettings;
    private TextView mLblUserVavCoin;

    private ViewGroup mViewGroupName;
    private ViewGroup mViewGroupEmail;
    private ViewGroup mViewGroupPassword;
    private ViewGroup mViewGroupNumber;
    private Bitmap bitmap;
    private CustomDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderRight = (ImageView) view.findViewById(R.id.btnHeaderRight);

        mLblHeaderTitle.setText(R.string.profile_title);
        mBtnHeaderLeft.setVisibility(View.INVISIBLE);
        mBtnHeaderRight.setImageResource(R.drawable.header_btn_logout);
        mBtnHeaderRight.setVisibility(View.VISIBLE);
        mBtnHeaderRight.setFocusable(true);
        mBtnHeaderRight.setClickable(true);
        mBtnHeaderRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        ProfileFragment.this.getActivity(),
                        getString(R.string.dialog_log_out_title),
                        getString(R.string.dialog_log_out_message),
                        getString(R.string.dialog_log_out_btn_positive),
                        getString(R.string.dialog_log_out_btn_negative),
                        false,
                        new DialogFragmentTwoButtonListener() {
                            @Override
                            public void onBtnPositiveClick(DialogFragment dialogFragment) {

                                dialogFragment.dismiss();
                                new AsyncTask<Void, Void, Boolean>() {
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        showProgressDialog();
                                    }

                                    @Override
                                    protected Boolean doInBackground(Void... voids) {
                                        try {
                                            GcmHelper.getInstance().unregisterTopics();
                                            return true;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return false;
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean isSuccess) {
                                        super.onPostExecute(isSuccess);
                                        if (!isSuccess) {
                                            dismissProgressDialog();
                                            Handler handler = new Handler(Looper.getMainLooper());
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DialogHelper.getInstance().showDialogBasicCustomFragment(
                                                            getActivity(),
                                                            ApplicationHelper.getInstance().getApplication().getString(R.string.action_logout),
                                                            ApplicationHelper.getInstance().getApplication().getString(R.string.dialog_json_error_message),
                                                            true
                                                    );
                                                }
                                            });
                                        } else {
                                            doLogOut();
                                        }
                                    }
                                }.execute();

                            }

                            @Override
                            public void onBtnNegativeClick(DialogFragment dialogFragment) {
                                dialogFragment.dismiss();
                            }
                        });
            }
        });

        mImgProfilePict = (ImageView) view.findViewById(R.id.imgProfilePict);
        mImgProfilePict.setFocusable(true);
        mImgProfilePict.setClickable(true);
        mImgProfilePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.getInstance().showDialogChooseImage(ProfileFragment.this.getActivity());
            }
        });
        mLblUserName = (TextView) view.findViewById(R.id.lblUserName);
        mLblUserEmail = (TextView) view.findViewById(R.id.lblUserEmail);

        mLblUserName.setText(PreferenceManagerCustom.getInstance().getUserName());
        mLblUserEmail.setText(PreferenceManagerCustom.getInstance().getUserEmail());

        if (!PreferenceManagerCustom.getInstance().getUserProfilePictPath().equals("")) {
            File image = new File(PreferenceManagerCustom.getInstance().getUserProfilePictPath());
            if (image.exists()) {
                bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                mImgProfilePict.setImageBitmap(bitmap);
            }
        }

        mLblUserVavCoin = (TextView) view.findViewById(R.id.lblUserVavCoin);

        mViewGroupName = (ViewGroup) view.findViewById(R.id.viewGroupName);
        mViewGroupName.setFocusable(true);
        mViewGroupName.setClickable(true);
        mViewGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new ProfileChangeNameFragment();
                ft.add(R.id.frame_home_member_content, fragment, ProfileChangeNameFragment.TAG_LOG);
                ft.addToBackStack(ProfileChangeNameFragment.TAG_LOG);
                ft.commit();
            }
        });
        mViewGroupEmail = (ViewGroup) view.findViewById(R.id.viewGroupEmail);
        mViewGroupEmail.setFocusable(true);
        mViewGroupEmail.setClickable(true);
        mViewGroupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new ProfileChangeEmailFragment();
                ft.add(R.id.frame_home_member_content, fragment, ProfileChangeEmailFragment.TAG_LOG);
                ft.addToBackStack(ProfileChangeEmailFragment.TAG_LOG);
                ft.commit();
            }
        });
        mViewGroupPassword = (ViewGroup) view.findViewById(R.id.viewGroupPassword);
        mViewGroupPassword.setFocusable(true);
        mViewGroupPassword.setClickable(true);
        mViewGroupPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new ProfileChangePasswordFragment();
                ft.add(R.id.frame_home_member_content, fragment, ProfileChangePasswordFragment.TAG_LOG);
                ft.addToBackStack(ProfileChangePasswordFragment.TAG_LOG);
                ft.commit();
            }
        });

        mViewGroupNumber = (ViewGroup) view.findViewById(R.id.viewGroupNumber);
        mViewGroupNumber.setFocusable(true);
        mViewGroupNumber.setClickable(true);
        mViewGroupNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new ProfileChangeNumberFragment();
                ft.add(R.id.frame_home_member_content, fragment, ProfileChangeNumberFragment.TAG_LOG);
                ft.addToBackStack(ProfileChangeNumberFragment.TAG_LOG);
                ft.commit();
            }
        });

        mBtnSettings = (ImageView) view.findViewById(R.id.btnSettings);
        mBtnSettings.setFocusable(true);
        mBtnSettings.setClickable(true);
        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new SettingsFragment();
                ft.add(R.id.frame_home_member_content, fragment, SettingsFragment.TAG_LOG);
                ft.addToBackStack(SettingsFragment.TAG_LOG);
                ft.commit();
            }
        });


        //show load indicator
        mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

        //get user profile data
        ServerManager.getInstance().doGetProfile(PreferenceManagerCustom.getInstance().getUserAuthToken(), ProfileFragment.this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doLogOut() {
        //All is valid
        mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

        //log out from server, if success, empty all data on phone
        ServerManager.getInstance().doLogOut(
                PreferenceManagerCustom.getInstance().getUserGuid(),
                PreferenceManagerCustom.getInstance().getUserAuthToken(),
                this);
    }

    public ImageView getImgProfilePict() {
        return mImgProfilePict;
    }

    public TextView getLblUserName() {
        return mLblUserName;
    }

    public TextView getLblUserEmail() {
        return mLblUserEmail;
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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ApplicationHelper.getInstance().getApplication(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            //empty shared preference
                            PreferenceManagerCustom.getInstance().clearPreferences();

                            //go to homescreen
                            Intent logOutIntent = new Intent(getActivity(), HomeGuestActivity.class);
                            logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logOutIntent);
                            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            getActivity().finish();
                        }
                    });
                }
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
    public void onGetProfileCallback(final GetProfileResponse getProfileResponse, boolean success) {
        dismissProgressDialog();
        if (success) {
            PreferenceManagerCustom.getInstance().setUserName(getProfileResponse.getName());
            PreferenceManagerCustom.getInstance().setUserEmail(getProfileResponse.getEmail());
            PreferenceManagerCustom.getInstance().setUserPhone(getProfileResponse.getPhone());
            PreferenceManagerCustom.getInstance().setUserVavCounter(getProfileResponse.getVav_counter());
            if (PreferenceManagerCustom.getInstance().getUserPhoto() == null || !PreferenceManagerCustom.getInstance().getUserPhoto().equals(getProfileResponse.getPhoto_url())) {
                PreferenceManagerCustom.getInstance().setUserPhoto(getProfileResponse.getPhoto_url());
                new DownloadImageTask().execute(getProfileResponse.getPhoto_url());
            }
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLblUserName.setText(PreferenceManagerCustom.getInstance().getUserName());
                    mLblUserEmail.setText(PreferenceManagerCustom.getInstance().getUserEmail());
                    mLblUserVavCoin.setText(PreferenceManagerCustom.getInstance().getUserVavCounter() + "");
                }
            });
        } else {
            if (ProfileFragment.this.getActivity() != null) {
                ProfileFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getProfileResponse != null) {
                            Toast.makeText(ApplicationHelper.getInstance().getApplication(), getProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ApplicationHelper.getInstance().getApplication(), R.string.get_user_profile_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onGetVoucherListCallback(GetVoucherListResponse getVoucherListResponse, boolean success) {

    }

    @Override
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {
        dismissProgressDialog();
        if (success) {
            File image = new File(PreferenceManagerCustom.getInstance().getUserProfilePictPath());
            if (image.exists()) {
                bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                if (ProfileFragment.this.getActivity() != null) {
                    ProfileFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImgProfilePict.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        } else {
            if (ProfileFragment.this.getActivity() != null) {
                ProfileFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplicationHelper.getInstance().getApplication(), R.string.update_user_profile_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && getActivity() != null && !getActivity().isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        dismissProgressDialog();
        if (getActivity() == null) {
            return;
        }
        if (isUnauthorizedError) {
            DirectToSplashScreenHelper.getInstance().redirectToSplashScreen(getActivity());
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.getInstance().showDialogBasicCustomFragment(
                        ProfileFragment.this.getActivity(),
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

    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());
        }
        if (getActivity() != null && !getActivity().isFinishing()) {
            mProgressDialog.show();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Integer, String> {
        private boolean isError = false;
        private File file;

        @Override
        protected String doInBackground(String... strings) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                File filedir = new File(GeneralUtil.getInstance().getAppFolderPath(getActivity()) + File.separator + Config.IMAGE_DIR + File.separator);

                if (!filedir.exists())
                    filedir.mkdirs();
                file = new File(filedir.getPath(), Config.USER_PROFILE_PICT_FILE_NAME + ".jpg");
                output = new FileOutputStream(file);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                isError = true;
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (!isError && file != null) {
                PreferenceManagerCustom.getInstance().setUserProfilePictPath(file.getPath());
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    mImgProfilePict.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Config.REQUEST_PERMISSION_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MediaOpenHelper.getInstance().openCamera(getActivity());
                } else {
                    Toast.makeText(getActivity(), R.string.open_camera_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case Config.REQUEST_PERMISSION_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MediaOpenHelper.getInstance().openGallery(getActivity());
                } else {
                    Toast.makeText(getActivity(), R.string.read_gallery_file_fail, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

}
