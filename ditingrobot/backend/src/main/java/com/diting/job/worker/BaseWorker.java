package com.diting.job.worker;

import com.diting.core.Universe;
import com.diting.model.enums.UserType;
import com.diting.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static com.diting.util.Utils.str;

/**
 * BaseWorker
 */
public abstract class BaseWorker implements InitializingBean {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final Void NO_RETURN = null;

    private boolean enabled;
    protected long interval;
    private String workerName;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // convert minutes to milli seconds
        interval = interval * 60 * 1000;

        if (!enabled) {
            LOGGER.info("Worker [" + getWorkerName() + "] is not enabled");
            return;
        }
        LOGGER.info("start process[" + getWorkerName() + "]");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Long startTime = System.currentTimeMillis();

                    try {
                        process();
                    } catch (Exception e) {
                        LOGGER.error("Error occurred during executing worker [" + getWorkerName() + "] " + e.getMessage(), e);
                    } finally {
                        Long cost = System.currentTimeMillis() - startTime;

                        if (cost < interval) {
                            LOGGER.debug("Too fast, sleep for a while.");
                            try {
                                Thread.sleep(interval - cost);
                            } catch (InterruptedException e) {
                                //silently ignore
                            }
                        }
                    }
                }
            }
        }.start();
        LOGGER.info("end process[" + getWorkerName() + "]");
    }

    protected abstract void process() throws Exception;

    protected String getWorkerName() {
        return workerName == null ? getClass().getSimpleName() : workerName;
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

    protected void safeRun(Callback callback) {
        try {
            // TODO; add retry support in the future
            callback.apply();
        } catch (Exception e) {
            LOGGER.error("Error occurred.", e);
        }
    }

    protected void login() {
        Universe.current().setUserType(UserType.userTypeHandler(str(UserType.SYSTEM)));
        Universe.current().setUserName(getClass().getSimpleName());
    }
}
