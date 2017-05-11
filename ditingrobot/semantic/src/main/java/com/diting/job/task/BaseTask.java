package com.diting.job.task;


import com.diting.core.Universe;
import com.diting.model.enums.UserType;
import com.diting.notification.notifier.NotifierContext;
import com.diting.notification.notifier.NotifierSupport;
import com.diting.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static com.diting.util.Utils.str;

/**
 * BaseTask
 */
public class BaseTask extends NotifierSupport {

    protected static final Void NO_RETURN = null;

    @Autowired
    private PlatformTransactionManager transactionManager;

    protected void login() {

        Universe.current().setUserType(UserType.userTypeHandler(str(UserType.SYSTEM)));

        // user name
        Universe.current().setUserName(getClass().getSimpleName());
    }

    @Override
    public void notify(NotifierContext context) {

    }

    protected void safeRun(Callback callback) {
        try {
            // TODO; add retry support in the future
            callback.apply();
        } catch (Exception e) {
            LOGGER.error("Error occurred.", e);
        }
    }

    protected void executeInNewTransaction(final Callback callback) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        template.execute(new TransactionCallback<Void>() {
            public Void doInTransaction(TransactionStatus status) {
                callback.apply();
                return NO_RETURN;
            }
        });
    }

}
