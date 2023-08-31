package by.home.dao.api;

import by.home.dao.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ITransactionDao {

    List<Transaction> findAll(String account, LocalDate from, LocalDate to);

    void insert(Transaction transaction);
}
