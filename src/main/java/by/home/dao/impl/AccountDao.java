package by.home.dao.impl;

import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
import by.home.data.exception.CustomSqlException;
import lombok.Setter;

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

@Setter
public class AccountDao implements IAccountDao {

    private Connection conn;

    @Override
    public Optional<Account> findById(String number) {
        try (PreparedStatement statement = this.conn.prepareStatement(
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

    @Override
    public void update(Account account) {
        try (PreparedStatement statement = this.conn.prepareStatement(UPDATE_ACCOUNT)) {
            statement.setBigDecimal(1, account.getBalance());
            statement.setBoolean(2, account.isInterestAccrued());
            statement.setString(3, account.getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    @Override
    public List<Account> getAccountsForInterestAccrual(int count) {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement statement = this.conn.prepareStatement(
                FIND_ACCOUNTS_FOR_INTEREST_ACCRUAL,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            statement.setInt(1, count);
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
