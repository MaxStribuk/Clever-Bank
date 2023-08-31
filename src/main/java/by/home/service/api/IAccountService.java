package by.home.service.api;

import by.home.dao.entity.Account;
import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {

    void changeBalance(ChangeBalanceDto changeBalanceDto);

    void transferMoney(MoneyTransferDto moneyTransferDto);

    List<Account> getAccountsForInterestAccrual(int count, boolean status);

    void interestAccrual(Account account, BigDecimal accrual);

    void update(Account account);

    Account findByAccountNumber(String accountNumber);
}
