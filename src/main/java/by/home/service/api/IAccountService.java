package by.home.service.api;

import by.home.dao.entity.Account;
import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;

import java.util.List;

public interface IAccountService {

    void changeBalance(ChangeBalanceDto changeBalanceDto);

    void transferMoney(MoneyTransferDto moneyTransferDto);

    List<Account> getAccountsForInterestAccrual(int count);

    void interestAccrual(Account account);
}
