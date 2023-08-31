package by.home.service.task;

import by.home.dao.entity.Account;
import by.home.service.api.IAccountService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static by.home.util.Constant.Utils.ACCOUNTS_FOR_INTEREST_ACCRUAL;

@RequiredArgsConstructor
public class ResetInterestAccrualStatusTask implements Runnable {

    private final ScheduledExecutorService executorService;
    private final IAccountService accountService;

    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() == 1) {
            List<Account> accountsForUpdateAccrualStatus = this.accountService
                    .getAccountsForInterestAccrual(ACCOUNTS_FOR_INTEREST_ACCRUAL, true);
            for (Account account : accountsForUpdateAccrualStatus) {
                account.setInterestAccrued(false);
                this.executorService.submit(() -> this.accountService.update(account));
            }
        }
    }
}