package by.home.service.util.validator;

import by.home.factory.service.AccountServiceSingleton;
import by.home.service.api.IExecutorService;
import by.home.service.task.InterestAccrualTask;
import by.home.service.task.ResetInterestAccrualStatusTask;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static by.home.util.Constant.Utils.DELAY_BETWEEN_SHIPMENTS;
import static by.home.util.Constant.Utils.EXECUTOR_CORE_POOL_SIZE;
import static by.home.util.Constant.Utils.INITIAL_DELAY;

@Getter
public class ExecutorManager implements IExecutorService {

    private ScheduledExecutorService executorService;

    @Override
    public void init() {
        this.executorService = Executors.newScheduledThreadPool(EXECUTOR_CORE_POOL_SIZE);
        this.executorService.scheduleWithFixedDelay(
                new InterestAccrualTask(this.executorService, AccountServiceSingleton.getInstance()),
                INITIAL_DELAY,
                DELAY_BETWEEN_SHIPMENTS,
                TimeUnit.SECONDS);
        this.executorService.scheduleWithFixedDelay(
                new ResetInterestAccrualStatusTask(this.executorService, AccountServiceSingleton.getInstance()),
                INITIAL_DELAY,
                DELAY_BETWEEN_SHIPMENTS,
                TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        this.executorService.shutdown();
    }
}
