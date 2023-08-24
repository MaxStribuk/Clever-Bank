package by.home.service.api;

import by.home.data.dto.ChangeBalanceDto;
import by.home.data.dto.MoneyTransferDto;

public interface IAccountService {

    void changeBalance(ChangeBalanceDto changeBalanceDto);

    void transferMoney(MoneyTransferDto moneyTransferDto);
}
