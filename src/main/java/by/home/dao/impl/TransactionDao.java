package by.home.dao.impl;

import by.home.dao.api.ITransactionDao;
import by.home.dao.entity.Transaction;
import by.home.data.exception.CustomSqlException;
import by.home.factory.util.ConnectionSingleton;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static by.home.util.Constant.ColumnName.ACCOUNT_FROM;
import static by.home.util.Constant.ColumnName.ACCOUNT_TO;
import static by.home.util.Constant.ColumnName.AMOUNT;
import static by.home.util.Constant.ColumnName.ID;
import static by.home.util.Constant.ColumnName.TIME;
import static by.home.util.Constant.ColumnName.TYPE_ID;
import static by.home.util.Constant.SqlQuery.FIND_TRANSACTION_BY_ACCOUNT;
import static by.home.util.Constant.SqlQuery.INSERT_TRANSACTION;

/**
 * класс для взаимодействия с таблицей транзакций
 */
public class TransactionDao implements ITransactionDao {

    /**
     * метод позволяет найти все транзакции к счету за нужный промежуток времени
     *
     * @param account счет, для которого будет произведена выборка транзакций
     * @param from    дата, с которой будет поиск транзакций
     * @param to      дата, по которую будет производиться поиск транзакций (включительно)
     * @return список найденных транзакций. Если транзакции не найдены, возвращает
     * пустой список
     */
    @Override
    public List<Transaction> findAll(String account, LocalDate from, LocalDate to) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(FIND_TRANSACTION_BY_ACCOUNT)) {
            statement.setDate(1, Date.valueOf(from));
            statement.setDate(2, Date.valueOf(to));
            statement.setString(3, account);
            statement.setString(4, account);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Transaction> transactions = new ArrayList<>();
                while (resultSet.next()) {
                    transactions.add(getTransaction(resultSet));
                }
                return transactions;
            }
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    /**
     * добавляет транзакцию в БД
     *
     * @param transaction объект транзакции, которая будет добавлена в БД
     */
    @Override
    public void insert(Transaction transaction) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(INSERT_TRANSACTION)) {
            statement.setString(1, transaction.getId().toString());
            statement.setString(2, transaction.getAccountFrom());
            statement.setString(3, transaction.getAccountTo());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(transaction.getTime()));
            statement.setShort(6, transaction.getTypeId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    private Transaction getTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(UUID.fromString(resultSet.getString(ID)))
                .accountFrom(resultSet.getString(ACCOUNT_FROM))
                .accountTo(resultSet.getString(ACCOUNT_TO))
                .typeId((short) (resultSet.getShort(TYPE_ID) - 1))
                .amount(resultSet.getBigDecimal(AMOUNT))
                .time(resultSet.getTimestamp(TIME).toLocalDateTime())
                .build();
    }
}
