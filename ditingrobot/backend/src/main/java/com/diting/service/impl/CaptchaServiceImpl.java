package com.diting.service.impl;


import com.diting.cache.RedisImpl;
import com.diting.error.AppErrors;
import com.diting.model.ModelEnums;
import com.diting.notification.notifier.NotifierContext;
import com.diting.notification.notifier.VerificationCodeNotifier;
import com.diting.service.CaptchaService;
import com.diting.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.diting.util.Utils.*;

@Service("captchaService")
@Transactional
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    private static final String COUNT_SUFFIX = "COUNT_";

    @Autowired
    private VerificationCodeNotifier verificationCodeNotifier;

    @Autowired
    private RedisImpl redis;

    @Value("${diting.verifyCode}")
    private String diTingVerifyCode;

    @Value("${diting.enabled}")
    private Boolean enabled;

    @Value("${diting.threshold}")
    private Integer threshold;

    protected static final Integer LENGTH = 4;
    protected static final Integer FROM = 0;
    protected static final Integer TO = 10;

    @Override
    public void getMobileCaptcha(String mobile) {
        LOGGER.info("get mobile [" + mobile + "] captcha start.");
        if (mobile == null || mobile.length() != 11) {
            LOGGER.info("get mobile [" + mobile + "] captcha fail for mobile invalid");
            throw AppErrors.INSTANCE.missingField("mobile").exception();
        }

        String verifyCode = generateVerificationCode(LENGTH);
        if (Utils.isEmpty(verifyCode)) {
            LOGGER.info("get mobile [" + mobile + "] captcha fail for verifyCode is empty");
            throw AppErrors.INSTANCE.common("获取验证码失败！").exception();
        }

        long FIFTEEN_MINUTES = Utils.minutesAfter(15).getTime();

        redis.put(ModelEnums.VERIFY_CODE_TYPE_BASIC + mobile, verifyCode, FIFTEEN_MINUTES);

        //Record the number of sending every day
        count(mobile);

        LOGGER.info("get mobile [" + mobile + "] verifyCode is " + verifyCode);

        sendVerifyCode(mobile, verifyCode);

        LOGGER.info("Successful send verification code to  mobile [" + mobile + "].");
    }


    @Override
    public Boolean checkMobileCaptcha(String mobile, String verifyCode) {
        boolean result = false;
        //Across the captcha validation
        if (enabled)
            //dev magic verify code
            if (Utils.equals(verifyCode, diTingVerifyCode)) return true;

        try {
            result = Utils.equals(verifyCode, redis.strGet(ModelEnums.VERIFY_CODE_TYPE_BASIC + mobile).toString());
            LOGGER.info("Successful check verification code for mobile [" + mobile + "].");
        } catch (Exception e) {
            LOGGER.error("{}", e);
            return result;
        }

        return result;
    }


    private void sendVerifyCode(final String mobile, final String verifyCode) {
        NotifierContext context = new NotifierContext();
        context.setMobile(mobile);
        context.setVerificationCode(verifyCode);

        LOGGER.info("get mobile [" + mobile + "] captcha start sendVerifyCode is " + verifyCode);
        verificationCodeNotifier.notify(context);
    }


    private static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    private static String generateVerificationCode(int charCount) {
        StringBuffer verifyCode = new StringBuffer();
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(FROM, TO) + '0');
            verifyCode.append(c);
        }
        return verifyCode.toString();
    }

    private void count(String mobile) {
        Integer count = 0;
        try {
            count = str2int(isNull(redis.get(COUNT_SUFFIX + mobile), "0"));
        } catch (Exception e) {
            LOGGER.error("error occurred count record number .", e);
        }

        if (count >= threshold) {
            LOGGER.info("[" + mobile + "] get verification code number is beyond the limit .");
            throw AppErrors.INSTANCE.common("获取验证码次数超出上限，请联系人工客服解决！").exception();
        }

        long ZERO_POINT = daysAfter(today(), 1).getTime();

        redis.put(COUNT_SUFFIX + mobile, ++count, ZERO_POINT);

        LOGGER.info("success count records.");

    }

}