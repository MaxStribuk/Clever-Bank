package by.home.service.task;

import by.home.dao.entity.Account;
import by.home.service.api.IAccountService;
import by.home.util.PropertiesUtil;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static by.home.util.Constant.Utils.ACCOUNTS_FOR_INTEREST_ACCRUAL;
import static by.home.util.Constant.Utils.HUNDRED_PERCENT;
import static by.home.util.Constant.Utils.PERCENT_PROPERTY_NAME;

/**
 * класс представляет собой задачу по начислению процентов
 */
@RequiredArgsConstructor
public class InterestAccrualTask implements Runnable {

    private final ScheduledExecutorService executorService;
    private final IAccountService accountService;

    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() == localDate.lengthOfMonth()) {
            List<Account> accountsForInterestAccrual = this.accountService
                    .getAccountsForInterestAccrual(ACCOUNTS_FOR_INTEREST_ACCRUAL, false);
            for (Account account : accountsForInterestAccrual) {
                BigDecimal accrual = interestAccrual(account);
                this.executorService.submit(() -> this.accountService.interestAccrual(account, accrual));
            }
        }
    }

    private BigDecimal interestAccrual(Account account) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal percent = new BigDecimal(PropertiesUtil.getProperty(PERCENT_PROPERTY_NAME));
        BigDecimal percents = HUNDRED_PERCENT
                .add(percent)
                .divide(HUNDRED_PERCENT, 2, RoundingMode.HALF_UP);
        BigDecimal newBalance = currentBalance.multiply(percents);
        account.setBalance(newBalance);
        account.setInterestAccrued(true);
        return newBalance.subtract(currentBalance);
    }
}
