package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.core.Universe;
import com.diting.model.Account;
import com.diting.model.AccountLog;
import com.diting.model.Company;
import com.diting.model.options.AccountOptions;
import com.diting.model.result.Results;
import com.diting.model.views.AccountViews;
import com.diting.resources.request.AccountBindRequest;
import com.diting.resources.request.ChangePasswordRequest;
import com.diting.resources.request.ResetPasswordRequest;
import com.diting.service.AccountLogService;
import com.diting.service.AccountService;
import com.diting.service.CompanyService;
import com.diting.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static com.diting.util.Utils.*;

/**
 * AccountResource
 */
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AccountResource {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AccountLogService accountLogService;

    @POST
    @Timed
    @Path("/")
    public Account create(Account account) {
        return accountService.create(account);
    }

    @POST
    @Timed
    @Path("/register/mobile/excel")
    public Account mobileRegisterExcel(Account account) {
        return accountService.mobileRegisterExcel(account);
    }

    @POST
    @Timed
    @Path("/register/mobile")
    public Account mobileRegister(Account account) {
        return accountService.mobileRegister(account);
    }

    @PUT
    @Timed
    @Path("/update")
    public Account updateAccount(Account account) {
        return accountService.update(account);
    }

    @GET
    @Timed
    @Path("/{userId}")
    public Account getAccountById(@PathParam("userId") Integer userId) {
        return accountService.get(userId);
    }

    @GET
    @Timed
    @Path("/mobile/{mobile}")
    public Account getAccountByMobile(@PathParam("mobile") String mobile) {
        return accountService.getByMobile(mobile);
    }

    @GET
    @Timed
    @Path("/company/{companyId}")
    public Account getByCompanyId(@PathParam("companyId") String companyId) {
        return accountService.getByCompanyId(str2int(companyId));
    }

    @POST
    @Timed
    @Path("/wechat")
    public Account getAccountByWechat(Account account) {
        return accountService.getByWechat(account);
    }

    @GET
    @Timed
    @Path("/angelid/{angelId}")
    public Account getAccountByAngelId(@PathParam("angelId") String angelId) {
        return accountService.getByAngelId(angelId);
    }

    @POST
    @Timed
    @Path("/wechat/bind")
    public Response bindWechat(Account account) {
        accountService.bindWechat(account.getOpenId(),account.getUnionId(), account.getUserName());
        return Response.status(200).build();
    }

    @POST
    @Timed
    @Path("/unionid/bind")
    public Response bindUnionId(Account account) {
        accountService.bindUnionId(account.getOpenId(), account.getUnionId());
        return Response.status(200).build();
    }

    @POST
    @Timed
    @Path("/angel/bind")
    public Response bindAngel(Account account) {
        accountService.bindAngel(account.getAngelId(), account.getUserName());
        return Response.status(200).build();
    }

    @POST
    @Timed
    @Path("/login")
    public Account login(Account account) {
        return accountService.login(account);
    }

    @GET
    @Timed
    @Path("/check-mobile/{mobile}")
    public Response checkMobile(@PathParam("mobile") String mobile) {
        return accountService.checkMobile(mobile) ? Response.status(400).build() : Response.ok().build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<Account> searchForPage(@Context UriInfo uriInfo) {
        return accountService.searchForPage(buildAccountOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/search-all")
    public List<Account> searchAll(@Context UriInfo uriInfo) {
        return accountService.search(buildAccountOptions(uriInfo));
    }

    @POST
    @Timed
    @Path("/password/change")
    public Response changePassword(ChangePasswordRequest request) {
        accountService.changePassword(request.getMobile(), request.getNewPassword(), request.getVerifyCode());

        return Response.status(200).build();
    }

    @POST
    @Timed
    @Path("/password/reset")
    public Response resetPassword(ResetPasswordRequest request) {
        accountService.resetPassword(request.getMobile(), request.getNewPassword(), request.getVerifyCode());

        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/invitation-code/{mobile}")
    public Account getInvitationCode(@PathParam("mobile") String mobile) {
        return accountService.getInvitationCode(mobile);
    }

    @POST
    @Timed
    @Path("/bind-mobile")
    public AccountBindRequest bindMobile(AccountBindRequest request) {
        return accountService.bindMobile(request);
    }

    @PUT
    @Timed
    @Path("/update-admin")
    public Response updateAccountAdmin(AccountOptions options) {
        Account account=accountService.getByCompanyId(options.getCompanyId());
        StringBuffer sb=new StringBuffer();
        if(account!=null){
            if(!account.getUserName().equals(options.getUserName())){
                account.setUserName(options.getUserName());
                sb.append(" 管理员"+ Universe.current().getUserName()+"将手机号"+account.getUserName()+"修改为"+options.getUserName());
            }

            Company company=new Company();
            company=companyService.get(account.getCompanyId());
            if(!company.getName().equals(options.getCompanyName())){
                company.setName(options.getCompanyName());
                companyService.update(company);
                sb.append(" 管理员"+ Universe.current().getUserName()+"将所有者名称"+account.getCompanyName()+"修改为"+options.getCompanyName());
            }
            if(!account.getPassword().equals(options.getPassword())){
                account.setPassword(Utils.sha1(options.getUserName(),options.getPassword()));
                sb.append(" 管理员"+ Universe.current().getUserName()+"将密码"+account.getPassword()+"修改为"+options.getPassword());
            }
        }
        if(!"".equals(sb.toString())&&sb.toString()!=null){
            accountService.update(account);
            AccountLog accountLog=new AccountLog();
            accountLog.setContent(sb.toString());
            accountLog.setReason(options.getReason());
            accountLogService.create(accountLog);
        }
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-type/{type}")
    public List<AccountViews> searchUserStatistics(@PathParam("type") String type){
        return accountService.searchUserStatistics(type);
    }

    @GET
    @Timed
    @Path("/search-login-user/{type}")
    public List<AccountViews> searchLoginUserStatistics(@PathParam("type") String type){
        return accountService.searchLoginUserStatistics(type);
    }

    @POST
    @Timed
    @Path("/update/telephone_switch/{type}")
    public Response updateTelephoneSwitch(@PathParam("type") Integer type){
        accountService.updateTelephoneSwitch(type);
        return Response.status(200).build();
    }
    private AccountOptions buildAccountOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        AccountOptions options = new AccountOptions();
        buildPageableOptions(uriInfo, options);
        options.setCompanyId(str2int(nullIfEmpty(params.getFirst("companyId"))));
        options.setCompanyName(nullIfEmpty(params.getFirst("companyName")));
        options.setId(str2int(nullIfEmpty(params.getFirst("id"))));
        options.setUserName(nullIfEmpty(params.getFirst("userName")));
        return options;
    }
}
