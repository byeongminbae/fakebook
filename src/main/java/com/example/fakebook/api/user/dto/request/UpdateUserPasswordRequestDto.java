package com.example.fakebook.api.user.dto.request;

import com.example.fakebook.global.util.CryptoUtil;

public class UpdateUserPasswordRequestDto {
    private String oldSignPassword;
    private String newSignPassword;
    private String confirmSignPassword;

    public String getOldSignPassword() {
        return CryptoUtil.encryptSha512(oldSignPassword);
    }

    public String getNewSignPassword() {
        return CryptoUtil.encryptSha512(newSignPassword);
    }

    public String getConfirmSignPassword() {
        return CryptoUtil.encryptSha512(confirmSignPassword);
    }
}
