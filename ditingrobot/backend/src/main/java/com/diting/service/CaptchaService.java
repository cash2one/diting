package com.diting.service;

/**
 * CaptchaService.
 */
public interface CaptchaService {

    void getMobileCaptcha(String mobile);

    Boolean checkMobileCaptcha(String mobile, String verifyCode);

}
