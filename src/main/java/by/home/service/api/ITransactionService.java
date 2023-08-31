package by.home.service.api;

import by.home.dao.entity.Transaction;
import by.home.data.dto.AccountStatementDto;

import java.util.List;

public interface ITransactionService {

    void add(Transaction transaction);

    List<Transaction> getTransactions(AccountStatementDto accountStatementDto);
}
