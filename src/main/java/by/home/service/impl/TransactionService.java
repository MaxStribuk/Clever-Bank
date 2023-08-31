package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.aop.api.Transactional;
import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Transaction;
import by.home.data.dto.AccountStatementDto;
import by.home.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionDao transactionDao;

    @Override
    @Loggable
    @Transactional(daoInterfaces = ITransactionDao.class)
    public void add(Transaction transaction) {
        this.transactionDao.insert(transaction);
    }

    @Override
    @Loggable
    @Transactional(readOnly = true, daoInterfaces = ITransactionDao.class)
    public List<Transaction> getTransactions(AccountStatementDto accountStatementDto) {
        return this.transactionDao.findAll(
                accountStatementDto.getAccount(),
                accountStatementDto.getDateFrom(),
                accountStatementDto.getDateTo()
        );
    }

}
