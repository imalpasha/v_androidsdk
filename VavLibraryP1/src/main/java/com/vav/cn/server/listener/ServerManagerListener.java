package com.vav.cn.server.listener;

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

/**
 * Created by Handrata Samsul on 1/14/2016.
 */
public interface ServerManagerListener {
    public abstract void onSignUpCallback(SignUpResponse signUpResponse, boolean success);

    public abstract void onLoginCallback(LoginResponse loginResponse, boolean success);

    public abstract void onLogoutCallback(LogoutResponse logoutResponse, boolean success);

    public abstract void onDeleteVoucherCallback(DeleteVoucherResponse deleteVoucherResponse, boolean success);

    public abstract void onGetDetailCallback(GetDetailResponse getDetailResponse, boolean success);

    public abstract void onGetHomeListCallback(GetHomeListResponse getHomeListResponse, boolean success);

    public abstract void onGetProfileCallback(GetProfileResponse getProfileResponse, boolean success);

    public abstract void onGetVoucherListCallback(GetVoucherListResponse getVoucherListResponse, boolean success);

    public abstract void onUpdateProfileCallback(UpdateProfileResponse updateProfileResponse, boolean success);

    public abstract void onUseVoucherCallback(UseVoucherResponse useVoucherResponse, boolean success);

    public abstract void onSaveVoucherCallback(SaveVoucherResponse saveVoucherResponse, boolean success);

    public abstract void onVavingCallback(VavingResponse vavingResponse, boolean success);

    public abstract void onFailCallback(String status, String message, boolean isUnauthorizedError);

    public abstract void onTermsConditionCallback(TermsResponse termsResponse, boolean isSuccess);

    public abstract void onSendTokenCallback(boolean isSuccess);
}
