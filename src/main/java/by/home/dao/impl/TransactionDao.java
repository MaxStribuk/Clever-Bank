package by.home.dao.impl;

import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Transaction;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static by.home.dao.util.Constant.ColumnName.ACCOUNT_FROM;
import static by.home.dao.util.Constant.ColumnName.ACCOUNT_TO;
import static by.home.dao.util.Constant.ColumnName.AMOUNT;
import static by.home.dao.util.Constant.ColumnName.ID;
import static by.home.dao.util.Constant.ColumnName.TIME;
import static by.home.dao.util.Constant.ColumnName.TYPE_ID;
import static by.home.dao.util.Constant.SqlQuery.FIND_ALL_TRANSACTION;
import static by.home.dao.util.Constant.SqlQuery.FIND_TRANSACTION_BY_ACCOUNT;
import static by.home.dao.util.Constant.SqlQuery.FIND_TRANSACTION_BY_TRANSACTION_ID;
import static by.home.dao.util.Constant.SqlQuery.INSERT_TRANSACTION;

@Slf4j
@Setter
public class TransactionDao implements ITransactionDao {

    private Connection conn;

    @Override
    public Optional<Transaction> findById(UUID id) {
        try (PreparedStatement statement = conn.prepareStatement(
                     FIND_TRANSACTION_BY_TRANSACTION_ID,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.first()
                        ? Optional.of(getTransaction(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findAll(String account) {
        try (PreparedStatement statement = conn.prepareStatement(FIND_TRANSACTION_BY_ACCOUNT)) {
            statement.setString(1, account);
            statement.setString(2, account);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Transaction> transactions = new ArrayList<>();
                while (resultSet.next()) {
                    transactions.add(getTransaction(resultSet));
                }
                return transactions;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        try (PreparedStatement statement = conn.prepareStatement(FIND_ALL_TRANSACTION)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Transaction> transactions = new ArrayList<>();
                while (resultSet.next()) {
                    transactions.add(getTransaction(resultSet));
                }
                return transactions;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Transaction transaction) {
        try (PreparedStatement statement = conn.prepareStatement(INSERT_TRANSACTION)) {
            statement.setString(1, transaction.getId().toString());
            statement.setString(2, transaction.getAccountFrom());
            statement.setString(3, transaction.getAccountTo());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(transaction.getTime()));
            statement.setShort(6, transaction.getTypeId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Transaction getTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(UUID.fromString(resultSet.getString(ID)))
                .accountFrom(resultSet.getString(ACCOUNT_FROM))
                .accountTo(resultSet.getString(ACCOUNT_TO))
                .typeId(resultSet.getShort(TYPE_ID))
                .amount(resultSet.getBigDecimal(AMOUNT))
                .time(resultSet.getTimestamp(TIME).toLocalDateTime())
                .build();
    }
}
