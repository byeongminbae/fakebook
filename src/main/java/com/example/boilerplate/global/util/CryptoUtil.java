package com.example.boilerplate.global.util;

import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptoUtil {
    public static String encryptSha512(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(string.getBytes(StandardCharsets.UTF_8));
            return String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            throw new BusinessException(CommonException.CRYPTO_ENCRYPTION_FAIL_EXCEPTION);
        }
    }
}
