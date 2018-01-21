package com.vav.cn.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vav.cn.CropImageActivity;
import com.vav.cn.LogInActivity;
import com.vav.cn.R;
import com.vav.cn.TermsAndCondActivity;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;
import com.vav.cn.dialog.CustomDialog;
import com.vav.cn.listener.DialogTwoButtonListener;
import com.vav.cn.listener.OnLoginCallbackSuccess;
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
import com.vav.cn.util.LoginCallbackHelper;
import com.vav.cn.util.MediaOpenHelper;
import com.vav.cn.util.PreferenceManagerCustom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by thunde91 on 4/14/16.
 */
public class SignUpFragment extends Fragment implements ServerManagerListener {
    public static final String TAG_LOG = SignUpFragment.class.getSimpleName();

    private ImageView mBtnBack;
    private TextView mLblTitle;

    private ImageView mImgProfilePict;

    private EditText mTxtName;
    private EditText mTxtEmail;
    private EditText mTxtPassword;

    private TextView mBtnSignUp;
    private TextView mBtnTnc;
    private TextView loginWithExistingAccount;

    private String mPassword;

    private CustomDialog mProgressDialog;
    private OnLoginCallbackSuccess onLoginCallbackSuccess;

    public void setOnLoginCallbackSuccess(OnLoginCallbackSuccess onLoginCallbackSuccess) {
        this.onLoginCallbackSuccess = onLoginCallbackSuccess;
        LoginCallbackHelper.getInstance().setOnLoginCallbackSuccess(onLoginCallbackSuccess);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        mBtnBack = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnBack.setFocusable(true);
        mBtnBack.setClickable(true);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        mLblTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblTitle.setText(R.string.sign_up_title);

        mImgProfilePict = (ImageView) view.findViewById(R.id.imgProfilePict);
        mImgProfilePict.setFocusable(true);
        mImgProfilePict.setClickable(true);
        mImgProfilePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.getInstance().showDialogChooseImage(getActivity());
            }
        });

        //Reload profile picture if have been set before
/*		if(!PreferenceManagerCustom.getInstance().getUserProfilePictPath().equals("")) {
            File image = new File(PreferenceManagerCustom.getInstance().getUserProfilePictPath());
			if(image.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
				mImgProfilePict.setImageBitmap(bitmap);
			}
		}*/

        mTxtName = (EditText) view.findViewById(R.id.txtName);
        mTxtEmail = (EditText) view.findViewById(R.id.txtEmail);
        mTxtPassword = (EditText) view.findViewById(R.id.txtPassword);

        mBtnSignUp = (TextView) view.findViewById(R.id.btnSignup);
        mBtnSignUp.setFocusable(true);
        mBtnSignUp.setClickable(true);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralUtil.getInstance().closeSoftKeyboard(getActivity());
                onBtnSubmitClicked();
            }
        });
        mBtnTnc = (TextView) view.findViewById(R.id.btnTnc);
        mBtnTnc.setFocusable(true);
        mBtnTnc.setClickable(true);
        mBtnTnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPageIntent = new Intent(getActivity(), TermsAndCondActivity.class);
                openPageIntent.putExtra(TermsAndCondActivity.URL_RESOURCE, getString(R.string.terms_and_condition_link));
                openPageIntent.putExtra(TermsAndCondActivity.TITLE_RESOURCE, getString(R.string.tnc_title));
                startActivity(openPageIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        loginWithExistingAccount = (TextView) view.findViewById(R.id.lblLoginWithExistingAccount);
        loginWithExistingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPageIntent = new Intent(getActivity(), LogInActivity.class);
                startActivity(openPageIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        if (onLoginCallbackSuccess == null) {
            loginWithExistingAccount.setVisibility(View.GONE);
        }
        return view;
    }


    public void onBtnSubmitClicked() {
        final String name = mTxtName.getText().toString();
        final String email = mTxtEmail.getText().toString();
        mPassword = mTxtPassword.getText().toString();

        // check valid name, email, password, then check connectivity
        if (!GeneralUtil.isValidName(name)) {
            DialogHelper.getInstance().showSingleButtonBasicDialog(
                    getActivity(),
                    getString(R.string.dialog_sign_up_name_too_short_title),
                    getString(R.string.dialog_sign_up_name_too_short_content),
                    getString(R.string.dialog_sign_up_name_too_short_button));
            mTxtName.requestFocus();
        } else if (!GeneralUtil.isValidEmail(email)) {
            DialogHelper.getInstance().showSingleButtonBasicDialog(
                    getActivity(),
                    getString(R.string.dialog_sign_up_invalid_email_title),
                    getString(R.string.dialog_sign_up_invalid_email_content),
                    getString(R.string.dialog_sign_up_invalid_email_button));
            mTxtEmail.requestFocus();
        } else if (!GeneralUtil.isValidPassword(mPassword)) {
            DialogHelper.getInstance().showSingleButtonBasicDialog(
                    getActivity(),
                    getString(R.string.dialog_sign_up_password_too_short_title),
                    getString(R.string.dialog_sign_up_password_too_short_content),
                    getString(R.string.dialog_sign_up_password_too_short_button));
            mTxtPassword.requestFocus();
        } else if (!GeneralUtil.getInstance().isOnline(getActivity())) {
            DialogHelper.getInstance().showTwoButtonBasicDialog(
                    getActivity(),
                    getString(R.string.dialog_not_online_title),
                    getString(R.string.dialog_not_online_content),
                    getString(R.string.dialog_not_online_positive_button),
                    getString(R.string.dialog_not_online_negative_button),
                    new DialogTwoButtonListener() {
                        @Override
                        public void onBtnPositiveClick(DialogInterface dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onBtnNegativeClick(DialogInterface dialog) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    });
        } else {
            //All is valid
            mProgressDialog = DialogHelper.getInstance().showLoadingProgressDialog(getActivity());

            String hashedPassword = GeneralUtil.hashPassword(mPassword);
            PreferenceManagerCustom.getInstance().setUserHashedPassword(hashedPassword);

            ServerManager.getInstance().doNormalSignUp(name, email, mPassword, this);
        }
    }

    @Override
    public void onSignUpCallback(final SignUpResponse signUpResponse, boolean success) {
        if (success && signUpResponse.getStatus().equals("success")) {
            PreferenceManagerCustom.getInstance().setUserEmail(signUpResponse.getEmail());
            PreferenceManagerCustom.getInstance().setUserGuid(signUpResponse.getGuid());
            PreferenceManagerCustom.getInstance().setUserAuthToken(signUpResponse.getAuthToken());
            PreferenceManagerCustom.getInstance().setUserGuid(signUpResponse.getGuid());
            PreferenceManagerCustom.getInstance().setUserAuthToken(signUpResponse.getAuthToken());
            PreferenceManagerCustom.getInstance().setUserIsLoggedIn(true);
            PreferenceManagerCustom.getInstance().setUserLastLoginDateTimeMillis(Calendar.getInstance().getTimeInMillis());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ApplicationHelper.getInstance().getApplication(), signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    if (onLoginCallbackSuccess != null) {
                        onLoginCallbackSuccess.onLoginCallback(true, null);
                    } else {
                        GeneralUtil.getInstance().goToHomeMemberPage(getActivity());
                    }
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                    Toast.makeText(ApplicationHelper.getInstance().getApplication(), (signUpResponse != null && signUpResponse.getMessage() != null) ? signUpResponse.getMessage() : "SignUp failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onLoginCallback(final LoginResponse loginResponse, boolean success) {

    }

    @Override
    public void onLogoutCallback(LogoutResponse logoutResponse, boolean success) {

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
    public void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success) {

    }

    @Override
    public void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success) {

    }

    @Override
    public void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success) {

    }

    @Override
    public void onVavingCallback(VavingResponse vavingResponse, boolean success) {

    }

    @Override
    public void onFailCallback(final String status, final String message, final boolean isUnauthorizedError) {
        mProgressDialog.dismiss();
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
                        getActivity(),
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.SELECT_PHOTO_GALLERY_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);

                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String filePath = GeneralUtil.getInstance().saveImageInExternalCacheDir(getActivity(), selectedImage, Config.TEMP_FILE_NAME);

                        Intent intent = new Intent(getActivity(), CropImageActivity.class);
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
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String filePath = GeneralUtil.getInstance().saveImageInExternalCacheDir(getActivity(), imageBitmap, Config.TEMP_FILE_NAME);

                    Intent intent = new Intent(getActivity(), CropImageActivity.class);
                    intent.putExtra(CropImageActivity.IMAGE_PATH_KEY, filePath);
                    startActivityForResult(intent, Config.CROP_IMAGE_CODE);
                }
                break;
            case Config.CROP_IMAGE_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    File image = new File(PreferenceManagerCustom.getInstance().getUserProfilePictPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                    mImgProfilePict.setImageBitmap(bitmap);
                }
                break;
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
