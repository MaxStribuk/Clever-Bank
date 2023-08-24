package by.home.dao.impl;

import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
import by.home.factory.util.ConnectionSingleton;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static by.home.dao.util.Constant.ColumnName.BALANCE;
import static by.home.dao.util.Constant.ColumnName.BANK_ID;
import static by.home.dao.util.Constant.ColumnName.CLIENT_ID;
import static by.home.dao.util.Constant.ColumnName.NUMBER;
import static by.home.dao.util.Constant.ColumnName.OPEN_DATE;
import static by.home.dao.util.Constant.SqlQuery.FIND_ACCOUNT_BY_ACCOUNT_NUMBER;
import static by.home.dao.util.Constant.SqlQuery.UPDATE_ACCOUNT;

@Slf4j
public class AccountDao implements IAccountDao {

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
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Account account) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(UPDATE_ACCOUNT)) {
            statement.setBigDecimal(1, account.getBalance());
            statement.setString(2, account.getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .number(resultSet.getString(NUMBER))
                .balance(resultSet.getBigDecimal(BALANCE))
                .clientId(UUID.fromString(resultSet.getString(CLIENT_ID)))
                .bankId(resultSet.getShort(BANK_ID))
                .openDate(resultSet.getDate(OPEN_DATE).toLocalDate())
                .build();
    }
}
