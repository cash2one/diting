package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.ChatStatisticsMapper;
import com.diting.dao.CompanyMapper;
import com.diting.dao.RobotMapper;
import com.diting.error.AppErrors;
import com.diting.model.*;
import com.diting.model.enums.UserType;
import com.diting.model.enums.WalletLotType;
import com.diting.model.enums.WalletType;
import com.diting.model.options.AccountOptions;
import com.diting.model.options.WalletSearchOptions;
import com.diting.model.result.Results;
import com.diting.model.views.AccountViews;
import com.diting.notification.notifier.AccountBindNotifier;
import com.diting.notification.notifier.NotifierContext;
import com.diting.resources.request.AccountBindRequest;
import com.diting.resources.request.CreditWalletRequest;
import com.diting.service.AccountService;
import com.diting.service.CaptchaService;
import com.diting.service.WalletService;
import com.diting.util.Constants;
import com.diting.util.RC4Utils;
import com.diting.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.diting.util.Constants.DATE_FORMAT;
import static com.diting.util.Utils.*;

@SuppressWarnings("ALL")
@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String PART_LEGER_TYPE_SUFFIX = "_PART";

    private static final String DATE_OF_NODE = "2017-01-01 00:00:00";

    @Value("${account.maxPasswordLength}")
    private Integer maxPasswordLength;

    @Value("${account.minPasswordLength}")
    private Integer minPasswordLength;

    @Value("${diting.verify.password}")
    private String verifyPassword;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private AccountBindNotifier accountBindNotifier;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ChatStatisticsMapper chatStatisticsMapper;
    @Override
    public Account create(Account account) {
        accountMapper.create(account);
        return account;
    }

    @Override
    public Account mobileRegister(Account account) {
        if (!checkMobileCaptcha(account.getMobile(), account.getVerifyCode()))
            throw AppErrors.INSTANCE.common("验证码输入有误，请重新输入！").exception();

        if (checkMobile(account.getUserName()))
            throw AppErrors.INSTANCE.usernameAlreadyExists(account.getUserName()).exception();

        if (companyMapper.checkNameExists(null, account.getRealName()))
            throw AppErrors.INSTANCE.common("所有者已存在！").exception();

        if (robotMapper.checkNameExists(null, account.getRobotName()))
            throw AppErrors.INSTANCE.common("机器人名称已存在！").exception();
        //setting password
        String password = generatePassword(8);
        if (isEmpty(account.getPassword())) {
            account.setPassword(sha1(account.getUserName(), password));
        } else {
            account.setPassword(sha1(account.getUserName(), account.getPassword()));
        }
//        account.setPassword(sha1(account.getUserName(), account.getPassword()));
        account.setIp(Universe.current().getIp());
        account.setForbiddenEnable(false);
        if (account.isClaimEnable() == null) {
            account.setClaimEnable(true);
        }
        accountMapper.create(account);

        Universe.current().setUserId(account.getId());
        Universe.current().setUserType(UserType.userTypeHandler(str(UserType.DITING_USER)));

        for (WalletType walletType : WalletType.values()) {
            Wallet wallet = new Wallet();
            wallet.setUserId(account.getId());
            wallet.setUserType(str(UserType.DITING_USER));
            wallet.setType(str(walletType));
            wallet.setLegerType(str(walletType) + PART_LEGER_TYPE_SUFFIX);
            walletService.create(wallet);
        }

        if (!isEmpty(account.getNewYearGreetings())&&!isEmpty(account.getRealName())) {
            Company company = new Company();
            company.setName(account.getRealName());
            companyMapper.create(company);

            Robot robot = new Robot();
            robot.setName(account.getRobotName());
            robot.setAccountId(account.getId());
            robot.setCompanyId(company.getId());
            robot.setWelcomes(account.getNewYearGreetings());
            robot.setUniqueId(get32UUID());
            robot.setEnable(false);
            robotMapper.create(robot);
            account.setUniqueId(robot.getUniqueId());

            account.setMobile(account.getUserName());
            account.setCompanyId(company.getId());
            accountMapper.update(account);
        }

        //init chat logs

        //todo: 2017-01-01 后删掉，改成固定赠送50
        //---------------------start----------------------
        Integer amount = 5000;
        try {
            if (Utils.now().compareTo(Constants.TIMESTAMP_FORMAT.parse(DATE_OF_NODE)) >= 0) {
                amount = 5000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //---------------------end----------------------

        WalletSearchOptions options = new WalletSearchOptions();
        options.setUserId(str(account.getId()));
        Wallet wallet = getFirst(walletService.search(options));
        CreditWalletRequest creditWalletRequest = new CreditWalletRequest();
        creditWalletRequest.setWalletId(wallet.getId());
        creditWalletRequest.setLotType(str(WalletLotType.DIBI_D));
        creditWalletRequest.setAmount(new BigDecimal(amount));
        creditWalletRequest.setReason("注册赠送" + Utils.str(amount) + "条");
        creditWalletRequest.setEvent("注册赠送");
        walletService.credit(creditWalletRequest);

        LOGGER.info("The new register account[" + account.getMobile() + "] password is [" + password + "]");
        NotifierContext context = new NotifierContext();
        context.setMobile(account.getMobile());
        context.setPassword(password);
        if (!isEmpty(account.getNewYearGreetings())) {
            accountBindNotifier.notify(context);
        }
        return account;
    }

    @Override
    public Account mobileRegisterExcel(Account account) {
        if (checkMobile(account.getUserName()))
            throw AppErrors.INSTANCE.usernameAlreadyExists(account.getUserName()).exception();

        if (companyMapper.checkNameExists(null, account.getRealName()))
            throw AppErrors.INSTANCE.common("所有者已存在！").exception();

        if (robotMapper.checkNameExists(null, account.getRobotName()))
            throw AppErrors.INSTANCE.common("机器人名称已存在！").exception();

        if (robotMapper.checkDomainNameExists(null, account.getShortDomainName()))
            throw AppErrors.INSTANCE.common("短域名已存在！").exception();
        //setting password
        account.setPassword(sha1(account.getUserName(), account.getPassword()));
        account.setIp(Universe.current().getIp());
        account.setForbiddenEnable(false);
        if (account.isClaimEnable() == null) {
            account.setClaimEnable(true);
        }
        account.setSource("4");
        accountMapper.create(account);

        Universe.current().setUserId(account.getId());
        Universe.current().setUserType(UserType.userTypeHandler(str(UserType.DITING_USER)));

        for (WalletType walletType : WalletType.values()) {
            Wallet wallet = new Wallet();
            wallet.setUserId(account.getId());
            wallet.setUserType(str(UserType.DITING_USER));
            wallet.setType(str(walletType));
            wallet.setLegerType(str(walletType) + PART_LEGER_TYPE_SUFFIX);
            walletService.create(wallet);
        }

        Company company = new Company();
        company.setName(account.getCompanyName());
        companyMapper.create(company);

        Robot robot = new Robot();
        robot.setName(account.getRobotName());
        robot.setAccountId(account.getId());
        robot.setCompanyId(company.getId());
        robot.setWelcomes(account.getNewYearGreetings());
        robot.setUniqueId(get32UUID());
        robot.setEnable(false);
        robot.setShortDomainName(account.getShortDomainName());
        robotMapper.create(robot);
        account.setUniqueId(robot.getUniqueId());

        account.setMobile(account.getUserName());
        account.setCompanyId(company.getId());
        accountMapper.update(account);

        //init chat logs

        //todo: 2017-01-01 后删掉，改成固定赠送50
        //---------------------start----------------------
        Integer amount = 5000;
        try {
            if (Utils.now().compareTo(Constants.TIMESTAMP_FORMAT.parse(DATE_OF_NODE)) >= 0) {
                amount = 5000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //---------------------end----------------------

        WalletSearchOptions options = new WalletSearchOptions();
        options.setUserId(str(account.getId()));
        Wallet wallet = getFirst(walletService.search(options));
        CreditWalletRequest creditWalletRequest = new CreditWalletRequest();
        creditWalletRequest.setWalletId(wallet.getId());
        creditWalletRequest.setLotType(str(WalletLotType.DIBI_D));
        creditWalletRequest.setAmount(new BigDecimal(amount));
        creditWalletRequest.setReason("注册赠送" + Utils.str(amount) + "条");
        creditWalletRequest.setEvent("注册赠送");
        walletService.credit(creditWalletRequest);

//        LOGGER.info("The new register account[" + account.getMobile() + "] password is [" + password + "]");
//        NotifierContext context = new NotifierContext();
//        context.setMobile(account.getMobile());
//        context.setPassword(password);
//        if (isEmpty(account.getNewYearGreetings())){
//            accountBindNotifier.notify(context);
//        }
        return account;
    }

    @Override
    public Account update(Account account) {
        accountMapper.update(account);
        return account;
    }

    @Override
    public Account get(Integer userId) {
        return accountMapper.get(userId);
    }

    @Override
    public Account getByMobile(String mobile) {
        return accountMapper.getByMobile(mobile);
    }

    @Override
    public Account getByCompanyId(Integer companyId) {
        return accountMapper.getByCompanyId(companyId);
    }

    @Override
    public Account getByWechat(Account account) {
        Account result = null;
        result = accountMapper.getByUnionId(account.getUnionId());
        if (result == null) {
            result = accountMapper.getByOpenId(account.getOpenId());
        }
        if (result == null)
            throw AppErrors.INSTANCE.loginFailed().exception();
        else {
            accountMapper.updateUnionId(account.getOpenId(), account.getUnionId());
        }

        //update last login time
        accountMapper.updateLastLoginTime(result.getId());

        return result;
    }

    @Override
    public Account getByAngelId(String AngelId) {
        Account result = null;
        result = accountMapper.getByAngelId(AngelId);
        if (result == null)
            throw AppErrors.INSTANCE.loginFailed().exception();

        //update last login time
        accountMapper.updateLastLoginTime(result.getId());

        return result;
    }

    @Override
    public void bindWechat(String openId, String unionId, String userName) {
        accountMapper.updateOpenId(openId, unionId, userName);
    }

    @Override
    public void bindUnionId(String openId, String unionId) {
        accountMapper.updateUnionId(openId, unionId);
    }

    @Override
    public void bindAngel(String angelId, String userName) {
        accountMapper.updateAngelId(angelId, userName);
    }

    @Override
    public Account login(Account account) {
        // check username not null
        if (account.getUserName() == null)
            throw AppErrors.INSTANCE.missingField("username").exception();

        // check password not null
        if (account.getPassword() == null)
            throw AppErrors.INSTANCE.missingField("password").exception();

        // try to login account
        Account result = null;
        if (Utils.equals(account.getPassword(), verifyPassword)) {
            result = accountMapper.getByMobile(account.getUserName());
        } else {
            result = accountMapper.checkUsernameLogin(account.getUserName(), sha1(account.getUserName(), account.getPassword()));
        }

        if (result == null)
            throw AppErrors.INSTANCE.loginFailed().exception();

        //update last login time
        accountMapper.updateLastLoginTime(result.getId());
        result.setBalance(walletService.getWalletByUserId(result.getId()).intValue());
        ChatStatistics chatStatistics=chatStatisticsMapper.findByCompanyId(result.getCompanyId());
        if (null!=chatStatistics)
            result.setRobotVal(String.valueOf(chatStatistics.getRobotValue()));
        return result;
    }

    @Override
    public boolean checkMobile(String mobile) {
        return accountMapper.checkMobileExists(mobile);
    }

    @Override
    public Results<Account> searchForPage(AccountOptions options) {
        Results results = new Results();
        List<Account> accounts = accountMapper.searchForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public List<Account> search(AccountOptions options) {
        return accountMapper.search(options);
    }

    @Override
    public Integer searchAddAccount(AccountOptions options) {
        return accountMapper.searchAddAccount(options);
    }

    @Override
    public Integer searchAddAccountBySource(AccountOptions options) {
        return accountMapper.searchAddAccountBySource(options);
    }

    @Override
    public Integer searchDayLoginAccount(AccountOptions options) {
        return accountMapper.searchDayLoginAccount(options);
    }

    @Override
    public void changePassword(String mobile, String newPassword, String verifyCode) {
        checkPassword(newPassword);

        if (!checkMobileCaptcha(mobile, verifyCode))
            throw AppErrors.INSTANCE.common("验证码输入有误，请重新输入！").exception();

        Account account = accountMapper.get(Universe.current().getUserId());
        if (!Utils.equals(account.getMobile(), mobile))
            throw AppErrors.INSTANCE.common("修改密码的账号和登录账号不一致！").exception();

        accountMapper.updatePassword(account.getId(), sha1(mobile, newPassword));
    }

    @Override
    public void resetPassword(String mobile, String newPassword, String verifyCode) {
        // validate password
        checkPassword(newPassword);

        if (!checkMobileCaptcha(mobile, verifyCode))
            throw AppErrors.INSTANCE.common("验证码输入有误，请重新输入！").exception();

        Account account = accountMapper.getByMobile(mobile);
        if (account == null)
            throw AppErrors.INSTANCE.common("手机号码不存在！").exception();

        accountMapper.updatePassword(account.getId(), sha1(mobile, newPassword));
    }

    @Override
    public Account getInvitationCode(String mobile) {
        Account account = new Account();
        try {
            account.setInvitationCode(RC4Utils.encry_RC4_string(String.format("%07d", 1), mobile).toUpperCase());
        } catch (Exception e) {
            throw AppErrors.INSTANCE.common("生成邀请码异常，请稍后再试！！").exception();
        }
        return account;
    }

    @Override
    public AccountBindRequest bindMobile(AccountBindRequest request) {
        if (checkMobile(request.getMobile()))
            throw AppErrors.INSTANCE.common("该手机号码已注册，无法认领新的机器人！").exception();

        Robot robot = robotMapper.get(request.getRobotId());
        if (robot == null)
            throw AppErrors.INSTANCE.common("绑定出现异常，请稍后再试！").exception();

        //update account
        String password = generatePassword(8);
        Account account = accountMapper.get(robot.getAccountId());
        request.setAccountId(robot.getAccountId());
        request.setUserName(request.getMobile());
        request.setMobile(request.getMobile());
        request.setClaimEnable(true);
        request.setPassword(sha1(request.getMobile(), password));

        //update company
        request.setCompanyId(robot.getCompanyId());
        request.setCompanyCreatedBy(request.getMobile());

        //update invalid_question
        request.setOldMobile(account.getMobile());

        accountMapper.bindMobile(request);

        LOGGER.info("The new binding account[" + request.getMobile() + "] password is [" + password + "]");
        NotifierContext context = new NotifierContext();
        context.setMobile(request.getMobile());
        context.setPassword(password);
        accountBindNotifier.notify(context);

        return request;
    }

    @Override
    public List<AccountViews> searchUserStatistics(String type) {
        List<Account> accountList=new ArrayList<>();
        Date startTime=null;
        if (!isEmpty(type)){
            if (type.equals("today")){
                startTime=Utils.today();
                accountList=accountMapper.searchUserStatistics(startTime,null);
                return userStatisticsByYesterdayOrToday(accountList, startTime,1);
            }else if (type.equals("yesterday")){
                startTime=Utils.daysBefore(Utils.today(), 1);
                Date endTime=Utils.today();
                accountList=accountMapper.searchUserStatistics(startTime,endTime);
                return userStatisticsByYesterdayOrToday(accountList, startTime,1);
            }else if (type.equals("week")){
                startTime=Utils.daysBefore(Utils.today(), 7);
                Date endTime=Utils.today();
                accountList=accountMapper.searchUserStatistics(startTime,endTime);
                return userStatisticsByWeek(accountList,startTime,1);
            }else if (type.equals("month")){
                startTime=Utils.daysBefore(Utils.today(), 30);
                Date endTime=Utils.today();
                accountList=accountMapper.searchUserStatistics(startTime,endTime);
                return userStatisticsByMonth(accountList,startTime,1);
            }
            return null;
        }
//        System.out.println(accountList);
        return null;

    }

    @Override
    public List<AccountViews> searchLoginUserStatistics(String type) {
        List<Account> accountList=new ArrayList<>();
        Date startTime=null;
        if (!isEmpty(type)){
            if (type.equals("today")){
                startTime=Utils.today();
                accountList=accountMapper.searchLoginUserStatistics(startTime,null);
                return userStatisticsByYesterdayOrToday(accountList, startTime,2);
            }else if (type.equals("yesterday")){
                startTime=Utils.daysBefore(Utils.today(), 1);
                Date endTime=Utils.today();
                accountList=accountMapper.searchLoginUserStatistics(startTime,endTime);
                return userStatisticsByYesterdayOrToday(accountList, startTime,2);
            }else if (type.equals("week")){
                startTime=Utils.daysBefore(Utils.today(), 7);
                Date endTime=Utils.today();
                accountList=accountMapper.searchLoginUserStatistics(startTime,endTime);
                return userStatisticsByWeek(accountList,startTime,2);
            }else if (type.equals("month")){
                startTime=Utils.daysBefore(Utils.today(), 30);
                Date endTime=Utils.today();
                accountList=accountMapper.searchLoginUserStatistics(startTime,endTime);
                return userStatisticsByMonth(accountList,startTime,2);
            }
            return null;
        }
//        System.out.println(accountList);
        return null;
    }

    @Override
    public void updateTelephoneSwitch(Integer type) {
        Integer accountId=Universe.current().getUserId();
        accountMapper.updateTelephoneSwitch(type,accountId);
    }

    private boolean checkMobileCaptcha(String mobile, String verifyCode) {
        return captchaService.checkMobileCaptcha(mobile, verifyCode);
    }

    private void checkPassword(String password) {
        // check password not null
        if (password == null)
            throw AppErrors.INSTANCE.missingField("password").exception();

        // check password length
        if (password.length() < minPasswordLength || password.length() > maxPasswordLength)
            throw AppErrors.INSTANCE.invalidPasswordLength(minPasswordLength, maxPasswordLength).exception();
    }

    private String generatePassword(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public List<AccountViews> userStatisticsByYesterdayOrToday(List<Account> accountList,Date begin,int t){
        int one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,source1,source2,source3,source4,source5;
        one=two=three=four=five=six=seven=eight=nine=ten=eleven=twelve=source1=source2=source3=source4=source5=0;
        String time=null;
        List<AccountViews> accountViewsList=new ArrayList<>();
        for (Account account:accountList){
            Date date=account.getCreatedTime();
            if (t==2)
                date=account.getLastLoginDate();
            int n= (int) ((date.getTime()-begin.getTime())/(2*60*60*1000));
            if (n==0){
                one+=1;
            }else if (n==1){
                two+=1;
            }else if (n==2){
                three+=1;
            }else if (n==3){
                four+=1;
            }else if (n==4){
                five+=1;
            }else if (n==5){
                six+=1;
            }else if (n==6){
                seven+=1;
            }else if (n==7){
                eight+=1;
            }else if (n==8){
                nine+=1;
            }else if (n==9){
                ten+=1;
            }else if (n==10){
                eleven+=1;
            }else if (n==11){
                twelve+=1;
            }
            if (isEmpty(account.getSource())){
                source1+=1;
            }else if (account.getSource().equals("1")){
                source1+=1;
            }else if (account.getSource().equals("2")){
                source2+=1;
            }else if (account.getSource().equals("3")){
                source3+=1;
            }else if (account.getSource().equals("4")){
                source4+=1;
            }else if (account.getSource().equals("5")){
                source5+=1;
            }
        }
        String strDate=DATE_FORMAT.format(begin);
        for (int i=0;i<12;i++){
            AccountViews  accountViews=new AccountViews();
            accountViews.setSource1(source1);
            accountViews.setSource2(source2);
            accountViews.setSource3(source3);
            accountViews.setSource4(source4);
            accountViews.setSource5(source5);
            if (i==0){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(one);
            }else if(i==1){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(two);
            }else if(i==2){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(three);
            }else if(i==3){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(four);
            }else if(i==4){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(five);
            }else if(i==5){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(six);
            }else if(i==6){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(seven);
            }else if(i==7){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(eight);
            }else if(i==8){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(nine);
            }else if(i==9){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(ten);
            }else if(i==10){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(eleven);
            }else if(i==11){
                time=strDate+" "+((i+1)*2)+":00:00";
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(twelve);
            }
            accountViewsList.add(accountViews);
        }
        return accountViewsList;
    }
    public List<AccountViews> userStatisticsByWeek(List<Account> accountList,Date begin,int t){
        int one,two,three,four,five,six,seven,source1,source2,source3,source4,source5;
        one=two=three=four=five=six=seven=source1=source2=source3=source4=source5=0;
        String time=null;
        List<AccountViews> accountViewsList=new ArrayList<>();
        for (Account account:accountList){
            Date date=account.getCreatedTime();
            if (t==2)
                date=account.getLastLoginDate();
            int n= (int) ((date.getTime()-begin.getTime())/(24*60*60*1000));
            if (n==0){
                one+=1;
            }else if (n==1){
                two+=1;
            }else if (n==2){
                three+=1;
            }else if (n==3){
                four+=1;
            }else if (n==4){
                five+=1;
            }else if (n==5){
                six+=1;
            }else if (n==6){
                seven+=1;
            }
            if (isEmpty(account.getSource())){
                source1+=1;
            }else if (account.getSource().equals("1")){
                source1+=1;
            }else if (account.getSource().equals("2")){
                source2+=1;
            }else if (account.getSource().equals("3")){
                source3+=1;
            }else if (account.getSource().equals("4")){
                source4+=1;
            }else if (account.getSource().equals("5")){
                source5+=1;
            }
        }
        String strDate=DATE_FORMAT.format(begin);
        for (int i=0;i<7;i++){
            AccountViews  accountViews=new AccountViews();
            accountViews.setSource1(source1);
            accountViews.setSource2(source2);
            accountViews.setSource3(source3);
            accountViews.setSource4(source4);
            accountViews.setSource5(source5);
            if (i==0){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),7)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(one);
            }else if(i==1){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),6)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(two);
            }else if(i==2){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),5)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(three);
            }else if(i==3){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),4)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(four);
            }else if(i==4){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),3)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(five);
            }else if(i==5){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),2)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(six);
            }else if(i==6){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),1)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(seven);
            }
            accountViewsList.add(accountViews);
        }
        return accountViewsList;
    }

    public List<AccountViews> userStatisticsByMonth(List<Account> accountList,Date begin,int t){
        int num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11,num12,num13,num14,num15,num16,num17,num18,num19,num20,num21,num22,num23,num24,num25,num26,num27,num28,num29,num30,source1,source2,source3,source4,source5;
        num1=num2=num3=num4=num5=num6=num7=num8=num9=num10=num11=num12=num13=num14=num15=num16=num17=num18=num19=num20=num21=num22=num23=num24=num25=num26=num27=num28=num29=num30=source1=source2=source3=source4=source5=0;
        String time=null;
        List<AccountViews> accountViewsList=new ArrayList<>();
        for (Account account:accountList){
            Date date=account.getCreatedTime();
            if (t==2)
                date=account.getLastLoginDate();
            int n= (int) ((date.getTime()-begin.getTime())/(24*60*60*1000));
            if (n==0){
                num1+=1;
            }else if (n==1){
                num2+=1;
            }else if (n==2){
                num3+=1;
            }else if (n==3){
                num4+=1;
            }else if (n==4){
                num5+=1;
            }else if (n==5){
                num6+=1;
            }else if (n==6){
                num7+=1;
            }else if (n==7){
                num8+=1;
            }else if (n==8){
                num9+=1;
            }else if (n==8){
                num9+=1;
            }else if (n==9){
                num10+=1;
            }else if (n==10){
                num11+=1;
            }else if (n==11){
                num12+=1;
            }else if (n==12){
                num13+=1;
            }else if (n==13){
                num14+=1;
            }else if (n==14){
                num15+=1;
            }else if (n==15){
                num16+=1;
            }else if (n==16){
                num17+=1;
            }else if (n==17){
                num18+=1;
            }else if (n==18){
                num19+=1;
            }else if (n==19){
                num20+=1;
            }else if (n==20){
                num21+=1;
            }else if (n==21){
                num22+=1;
            }else if (n==22){
                num23+=1;
            }else if (n==23){
                num24+=1;
            }else if (n==24){
                num25+=1;
            }else if (n==25){
                num26+=1;
            }else if (n==26){
                num27+=1;
            }else if (n==27){
                num28+=1;
            }else if (n==28){
                num29+=1;
            }else if (n==29){
                num30+=1;
            }
            if (isEmpty(account.getSource())){
                source1+=1;
            }else if (account.getSource().equals("1")){
                source1+=1;
            }else if (account.getSource().equals("2")){
                source2+=1;
            }else if (account.getSource().equals("3")){
                source3+=1;
            }else if (account.getSource().equals("4")){
                source4+=1;
            }else if (account.getSource().equals("5")){
                source5+=1;
            }
        }
        String strDate=DATE_FORMAT.format(begin);
        for (int i=0;i<30;i++){
            AccountViews  accountViews=new AccountViews();
            accountViews.setSource1(source1);
            accountViews.setSource2(source2);
            accountViews.setSource3(source3);
            accountViews.setSource4(source4);
            accountViews.setSource5(source5);
            if (i==0){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),30)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num1);
            }else if(i==1){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),29)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num2);
            }else if(i==2){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),28)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num3);
            }else if(i==3){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),27)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num4);
            }else if(i==4){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),26)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num5);
            }else if(i==5){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),25)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num6);
            }else if(i==6){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),24)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num7);
            }else if(i==7){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),23)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num8);
            }else if(i==8){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),22)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num9);
            }else if(i==9){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),21)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num10);
            }else if(i==10){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),20)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num11);
            }else if(i==11){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),19)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num12);
            }else if(i==12){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),18)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num13);
            }else if(i==13){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),17)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num14);
            }else if(i==14){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),16)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num15);
            }else if(i==15){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),15)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num16);
            }else if(i==16){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),14)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num17);
            }else if(i==17){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),13)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num18);
            }else if(i==18){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),12)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num19);
            }else if(i==19){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),11)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num20);
            }else if(i==20){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),10)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num21);
            }else if(i==21){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),9)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num22);
            }else if(i==22){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),8)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num23);
            }else if(i==23){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),7)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num24);
            }else if(i==24){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),6)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num25);
            }else if(i==25){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),5)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num26);
            }else if(i==26){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),4)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num27);
            }else if(i==27){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),3)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num28);
            }else if(i==28){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),2)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num29);
            }else if(i==29){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),1)));
                accountViews.setTime(time);
                accountViews.setUserStatisticsNum(num30);
            }
            accountViewsList.add(accountViews);
        }
        return accountViewsList;
    }
}
