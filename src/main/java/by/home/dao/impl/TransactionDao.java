package by.home.dao.impl;

import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Transaction;
import by.home.dao.factory.ConnectionSingleton;
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

@Slf4j
public class TransactionDao implements ITransactionDao {

    private final String FIND_BY_TRANSACTION_ID = "SELECT * FROM transaction WHERE id=?;";
    private final String FIND_BY_ACCOUNT =
            "SELECT * FROM transaction WHERE account_from=? OR account_to=?;";
    private final String FIND_ALL = "SELECT * FROM transaction;";
    private final String INSERT = "INSERT INTO transaction " +
            "(id, account_from, account_to, amount, time, type_id) VALUES (?,?,?,?,?,?);";

    @Override
    public Optional<Transaction> findById(UUID id) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_TRANSACTION_ID,
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
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_ACCOUNT)) {
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
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(FIND_ALL)) {
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
    public boolean insert(Transaction transaction) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(INSERT)) {
            statement.setString(1, transaction.getId().toString());
            statement.setString(2, transaction.getAccountFrom());
            statement.setString(3, transaction.getAccountTo());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(transaction.getTime()));
            statement.setShort(6, transaction.getTypeId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Transaction getTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(UUID.fromString(resultSet.getString("id")))
                .accountFrom(resultSet.getString("account_from"))
                .accountTo(resultSet.getString("account_to"))
                .typeId(resultSet.getShort("type_id"))
                .amount(resultSet.getBigDecimal("amount"))
                .time(resultSet.getTimestamp("time").toLocalDateTime())
                .build();
    }
}
