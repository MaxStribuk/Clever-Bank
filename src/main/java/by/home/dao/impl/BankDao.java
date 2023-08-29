package by.home.dao.impl;

import by.home.dao.api.IBankDao;
import by.home.dao.entity.Bank;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.home.util.Constant.ColumnName.ID;
import static by.home.util.Constant.ColumnName.NAME;
import static by.home.util.Constant.SqlQuery.FIND_BANK_BY_BANK_ID;

@Setter
public class BankDao implements IBankDao {

    private Connection conn;

    @Override
    public Optional<Bank> findById(short id) {
        try (PreparedStatement statement = this.conn.prepareStatement(
                FIND_BANK_BY_BANK_ID,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            statement.setShort(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.first()
                        ? Optional.of(getBank(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Bank getBank(ResultSet resultSet) throws SQLException {
        return Bank.builder()
                .id(resultSet.getShort(ID))
                .name(resultSet.getString(NAME))
                .build();
    }
}
