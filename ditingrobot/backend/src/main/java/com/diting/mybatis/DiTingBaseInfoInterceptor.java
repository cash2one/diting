package com.diting.mybatis;

import com.diting.core.Universe;
import com.diting.model.BaseModel;
import com.diting.model.ModelEnums;
import com.diting.model.SecuredModel;
import com.diting.util.Utils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.security.SecureRandom;
import java.util.Properties;

import static com.diting.util.ModelUtils.genSecuredSign;
import static com.diting.util.Utils.now;
import static com.diting.util.Utils.str;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})})
public class DiTingBaseInfoInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (invocation.getArgs()[1] instanceof BaseModel && Utils.equals(ModelEnums.SQL_STATEMENT_INSERT, str(sqlCommandType))) {
            BaseModel baseModel = (BaseModel) invocation.getArgs()[1];
            baseModel.setOwner(Universe.current().getUserId());
            baseModel.setOwnerType(Universe.current().getUserType());
            baseModel.setCreatedBy(Universe.current().getUserName());
            if (baseModel.getCreatedTime() == null){
                baseModel.setCreatedTime(now());
            }
            baseModel.setUpdatedBy(Universe.current().getUserName());
            if (baseModel.getUpdatedTime() == null){
                baseModel.setUpdatedTime(now());
            }

            //process Secured
            processModel(baseModel);

            invocation.getArgs()[1] = baseModel;
        } else if (invocation.getArgs()[1] instanceof BaseModel && Utils.equals(ModelEnums.SQL_STATEMENT_UPDATE, str(sqlCommandType))) {
            BaseModel baseModel = (BaseModel) invocation.getArgs()[1];
            baseModel.setOwner(Universe.current().getUserId());
            baseModel.setOwnerType(Universe.current().getUserType());
            baseModel.setUpdatedBy(Universe.current().getUserName());
            baseModel.setUpdatedTime(now());

            //process Secured
            processModel(baseModel);

            invocation.getArgs()[1] = baseModel;
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //nothing
    }

    private void processModel(BaseModel baseModel){
        if(baseModel instanceof SecuredModel){
            SecuredModel securedModel = (SecuredModel) baseModel;
            Long salt = new SecureRandom().nextLong();
            securedModel.setSalt(salt);

            securedModel.setSign(genSecuredSign(securedModel));

        }
    }
}
