package by.home.dao.impl;

import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
import by.home.data.exception.CustomSqlException;
import by.home.factory.util.ConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static by.home.util.Constant.ColumnName.BALANCE;
import static by.home.util.Constant.ColumnName.BANK_ID;
import static by.home.util.Constant.ColumnName.CLIENT_ID;
import static by.home.util.Constant.ColumnName.INTEREST_ACCRUED;
import static by.home.util.Constant.ColumnName.NUMBER;
import static by.home.util.Constant.ColumnName.OPEN_DATE;
import static by.home.util.Constant.SqlQuery.FIND_ACCOUNTS_FOR_INTEREST_ACCRUAL;
import static by.home.util.Constant.SqlQuery.FIND_ACCOUNT_BY_ACCOUNT_NUMBER;
import static by.home.util.Constant.SqlQuery.UPDATE_ACCOUNT;

/**
 * класс для взаимодействия с таблицей счет
 */
public class AccountDao implements IAccountDao {

    /**
     * ищет счет в БД по id
     *
     * @param number id счета
     * @return {@link Optional}, содержащий счет, если он был найден в БД,
     * или пустой, если счет найден не был
     */
    @Override
    public Optional<Account> findById(String number) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(
                     FIND_ACCOUNT_BY_ACCOUNT_NUMBER,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.first()
                        ? Optional.of(getAccount(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    /**
     * обновляет состояние счета в БД
     *
     * @param account счет, состояние которого будет сохранено в БД
     */
    @Override
    public void update(Account account) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(UPDATE_ACCOUNT)) {
            setParameters(account, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    /**
     * возвращает список счетов для начисления процентов / обновления
     * статуса по счетам с начисленными процентами
     * Используется, когда необходимо достать из БД счета, по которым
     * необходимо начислить проценты в последний день месяца, либо сбросить
     * флаг начисления процентов в первый день месяца
     *
     * @param count  количество записей в выборке
     * @param status статус счета (были ли уже начислены проценты)
     * @return список счетов, удовлетворяющих условиям запроса либо пустой список
     */
    @Override
    public List<Account> getAccountsForInterestAccrual(int count, boolean status) {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(
                     FIND_ACCOUNTS_FOR_INTEREST_ACCRUAL,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            statement.setBoolean(1, status);
            statement.setInt(2, count);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accounts.add(getAccount(resultSet));
                }
            }
            return accounts;
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    /**
     * метод перевода денежных средств с одного счета на другой.
     * Методу передаются счета с уже вычисленными новыми значениями балансов
     * на счетах
     *
     * @param accountFrom счет, с которого осуществляется перевод денег (с состоянием
     *                    баланса на конец транзакции)
     * @param accountTo   счет, на который осуществляется перевод денег (с состоянием
     *                    баланса на конец транзакции)
     */
    @Override
    public void transferMoney(Account accountFrom, Account accountTo) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ConnectionSingleton.getInstance().open();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(UPDATE_ACCOUNT);
            setParameters(accountFrom, statement);
            statement.executeUpdate();
            setParameters(accountTo, statement);
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new CustomSqlException(ex);
                }
            }
            throw new CustomSqlException(e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new CustomSqlException(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new CustomSqlException(e);
                }
            }
        }
    }

    private void setParameters(Account account, PreparedStatement statement) throws SQLException {
        statement.setBigDecimal(1, account.getBalance());
        statement.setBoolean(2, account.isInterestAccrued());
        statement.setString(3, account.getNumber());
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .number(resultSet.getString(NUMBER))
                .balance(resultSet.getBigDecimal(BALANCE))
                .clientId(UUID.fromString(resultSet.getString(CLIENT_ID)))
                .bankId(resultSet.getShort(BANK_ID))
                .openDate(resultSet.getDate(OPEN_DATE).toLocalDate())
                .interestAccrued(resultSet.getBoolean(INTEREST_ACCRUED))
                .build();
    }
}
