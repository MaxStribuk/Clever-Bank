package by.home.service.task;

import by.home.dao.entity.Account;
import by.home.service.api.IAccountService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static by.home.util.Constant.Utils.ACCOUNTS_FOR_INTEREST_ACCRUAL;

@RequiredArgsConstructor
public class InterestAccrualTask implements Runnable {

    private final ScheduledExecutorService executorService;
    private final IAccountService accountService;

    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() == localDate.lengthOfMonth()) {
            List<Account> accountsForInterestAccrual = this.accountService
                    .getAccountsForInterestAccrual(ACCOUNTS_FOR_INTEREST_ACCRUAL);
            for (Account account : accountsForInterestAccrual) {
                this.executorService.submit(() ->
                        this.accountService.interestAccrual(account));
            }
        }
    }
}
