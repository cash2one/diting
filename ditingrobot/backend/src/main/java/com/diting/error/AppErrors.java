package com.diting.error;

/**
 * AppErrors.
 */
public interface AppErrors {
    AppErrors INSTANCE = ErrorProxy.newProxyInstance(AppErrors.class);

    /* core errors */
    @ErrorDef(httpStatusCode = 500, code = "1", message = "UnCaught Exception. {0}")
    AppError unCaught(String msg);

    @ErrorDef(httpStatusCode = 500, code = "2", message = "{0}")
    AppError common(String msg);

    @ErrorDef(httpStatusCode = 400, code = "10", message = "Unnecessary field found.", field = "{0}")
    AppError unnecessaryField(String field);

    @ErrorDef(httpStatusCode = 400, code = "11", message = "Field is not correct. {1}", field = "{0}")
    AppError fieldNotCorrect(String fieldName, String msg);

    @ErrorDef(httpStatusCode = 400, code = "12", message = "Invalid Json: {0}")
    AppError invalidJson(String detail);

    @ErrorDef(httpStatusCode = 403, code = "13", message = "Access denied")
    AppError accessDenied();

    @ErrorDef(httpStatusCode = 400, code = "100", message = "Missing Input field.", field = "{0}")
    AppError missingField(String field);

    @ErrorDef(httpStatusCode = 404, code = "101", message = "{0} [{1}] not found.")
    AppError notFound(String entity, Integer id);

    @ErrorDef(httpStatusCode = 404, code = "102", message = "{0} [{2}]'s {1} not found.")
    AppError notFound(String hostEntity, String entity, Integer id);

    @ErrorDef(httpStatusCode = 500, code = "103", message = "Failed to create [{0}] entity.")
    AppError createFailed(String entity);

    @ErrorDef(httpStatusCode = 500, code = "104", message = "Failed to update [{0}] entity [{1}].")
    AppError updateFailed(String entity, Integer id);

    @ErrorDef(httpStatusCode = 500, code = "105", message = "Failed to delete [{0}] entity [{1}].")
    AppError deleteFailed(String entity, Integer id);

    @ErrorDef(httpStatusCode = 500, code = "106", message = "Failed to query entity.")
    AppError queryFailed();

    /* customer errors */
    @ErrorDef(httpStatusCode = 400, code = "10000", message = "用户名[{0}]已存在")
    AppError usernameAlreadyExists(String username);

    @ErrorDef(httpStatusCode = 400, code = "10001", message = "手机号[{0}]已存在")
    AppError mobileAlreadyExists(String mobile);

    @ErrorDef(httpStatusCode = 400, code = "10002", message = "邮箱[{0}]已存在")
    AppError emailAlreadyExists(String mobile);

    @ErrorDef(httpStatusCode = 400, code = "10003", message = "密码长度为[{0}]-[{1}]个字符")
    AppError invalidPasswordLength(int minLength, int maxLength);

    @ErrorDef(httpStatusCode = 400, code = "10004", message = "密码不可以和手机号相同")
    AppError mobilePasswordShouldNotBeSame();

    @ErrorDef(httpStatusCode = 400, code = "10005", message = "密码不可以和邮箱相同")
    AppError emailPasswordShouldNotBeSame();

    @ErrorDef(httpStatusCode = 400, code = "10006", message = "非法手机号[{0}]")
    AppError invalidMobile(String mobile);

    @ErrorDef(httpStatusCode = 400, code = "10007", message = "非法邮箱[{0}]")
    AppError invalidEmail(String mobile);

    @ErrorDef(httpStatusCode = 400, code = "10008", message = "登录失败")
    AppError loginFailed();

    @ErrorDef(httpStatusCode = 400, code = "10009", message = "当前密码错误")
    AppError invalidPassword();

    @ErrorDef(httpStatusCode = 400, code = "10010", message = "储值账户已存在")
    AppError walletAlreadyExists();

    @ErrorDef(httpStatusCode = 400, code = "10011", message = "储值帐户[{0}]类型错误")
    AppError invalidWalletType(String walletType);

    @ErrorDef(httpStatusCode = 400, code = "10012", message = "储值帐户主账号不存在")
    AppError missingMasterAccount();

    @ErrorDef(httpStatusCode = 400, code = "10013", message = "储值账户不存在")
    AppError walletNotExists();

    @ErrorDef(httpStatusCode = 400, code = "10014", message = "储值帐户充值失败")
    AppError creditWalletFailed();

    @ErrorDef(httpStatusCode = 400, code = "10015", message = "储值帐户余额不足")
    AppError insufficientWalletBalance();

    @ErrorDef(httpStatusCode = 400, code = "10016", message = "储值帐户消费失败")
    AppError debitWalletFailed();

    @ErrorDef(httpStatusCode = 400, code = "10017", message = "储值槽余额不足")
    AppError insufficientLotBalance();


}
