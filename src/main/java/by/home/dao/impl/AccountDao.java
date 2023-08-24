package by.home.dao.impl;

import by.home.dao.api.IAccountDao;
import by.home.dao.entity.Account;
import by.home.factory.util.ConnectionSingleton;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class AccountDao implements IAccountDao {


    private final String FIND_BY_ACCOUNT_NUMBER = "SELECT * FROM account WHERE number=?;";
    private final String UPDATE = "UPDATE account SET balance=?, close_date=? WHERE number=?;";

    @Override
    public Optional<Account> findById(String number) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_ACCOUNT_NUMBER,
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
             PreparedStatement statement = conn.prepareStatement(UPDATE)) {
            statement.setBigDecimal(1, account.getBalance());
            statement.setDate(2, Date.valueOf(account.getCloseDate()));
            statement.setString(3, account.getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .number(resultSet.getString("number"))
                .balance(resultSet.getBigDecimal("balance"))
                .clientId(UUID.fromString(resultSet.getString("client_id")))
                .bankId(resultSet.getShort("bank_id"))
                .openDate(resultSet.getDate("open_date").toLocalDate())
                .closeDate(resultSet.getDate("close_date").toLocalDate())
                .build();
    }
}
