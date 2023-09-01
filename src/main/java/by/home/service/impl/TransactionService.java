package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Transaction;
import by.home.data.dto.AccountStatementDto;
import by.home.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * сервисный класс для операций с сущностью транзакция
 */
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionDao transactionDao;

    @Override
    @Loggable
    public void add(Transaction transaction) {
        this.transactionDao.insert(transaction);
    }

    @Override
    @Loggable
    public List<Transaction> getTransactions(AccountStatementDto accountStatementDto) {
        return this.transactionDao.findAll(
                accountStatementDto.getAccount(),
                accountStatementDto.getDateFrom(),
                accountStatementDto.getDateTo()
        );
    }

}
