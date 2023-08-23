package by.home.dao.api;

import by.home.dao.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITransactionDao {

    Optional<Transaction> findById(UUID id);

    List<Transaction> findAll(String account);

    List<Transaction> findAll();

    boolean insert(Transaction transaction);
}
