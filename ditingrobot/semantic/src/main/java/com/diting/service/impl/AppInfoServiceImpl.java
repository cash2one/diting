package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.error.AppErrors;
import com.diting.model.Account;
import com.diting.model.Company;
import com.diting.model.Fans;
import com.diting.model.Robot;
import com.diting.model.views.AppInfo;
import com.diting.service.*;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/2/16.
 */
@Service("appInfoService")
@Transactional
public class AppInfoServiceImpl  implements AppInfoService{
    @Autowired
    private RobotService robotService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private FansService fansService;
    @Autowired
    private AccountService accountService;

    @Override
    public AppInfo getAppInfo() {
        AppInfo appInfo=new AppInfo();
        Integer userId= Universe.current().getUserId();
        if (null==userId)
            throw AppErrors.INSTANCE.common("用户未登录").exception();
        Account account = accountService.get(userId);
        String headPortrait = account.getHeadImgUrl();
        if(!TextUtils.isEmpty(headPortrait)){
           appInfo.setHeadPortrait(headPortrait);
        }
        Robot robot=robotService.getByUserId(userId);
        Fans fans=new Fans();
        fans.setOwn_phone(String.valueOf(userId));
        Integer count=fansService.find_fans_count(fans);
        if (null!=robot){
            Company company=companyService.get(robot.getCompanyId());
            appInfo.setRobotName(robot.getName());
            if (null!=company){
                appInfo.setCompanyName(company.getName());
            }
            appInfo.setFansCount(count);
            appInfo.setShort_domain_name(robot.getShortDomainName());
            appInfo.setUnique_id(robot.getUniqueId());
        }

        return appInfo;
    }
}
