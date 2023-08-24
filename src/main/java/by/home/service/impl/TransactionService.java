package by.home.service.impl;

import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Transaction;
import by.home.service.api.ITransactionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionDao transactionDao;

    @Override
    public void add(Transaction transaction) {
        transactionDao.insert(transaction);
    }
}
