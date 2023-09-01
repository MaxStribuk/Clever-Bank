package by.home.dao.api;

import by.home.dao.entity.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountDao {

    Optional<Account> findById(String number);

    void update(Account account);

    List<Account> getAccountsForInterestAccrual(int count, boolean status);

    void transferMoney(Account accountFrom, Account accountTo);
}
