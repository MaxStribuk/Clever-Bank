package by.home.dao.impl;

import by.home.dao.api.IClientDao;
import by.home.dao.entity.Client;
import by.home.data.exception.CustomSqlException;
import by.home.factory.util.ConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static by.home.util.Constant.ColumnName.ID;
import static by.home.util.Constant.ColumnName.NAME;
import static by.home.util.Constant.ColumnName.PASSPORT_NUMBER;
import static by.home.util.Constant.SqlQuery.FIND_CLIENT_BY_ID;

public class ClientDao implements IClientDao {

    @Override
    public Optional<Client> findById(UUID clientId) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement statement = conn.prepareStatement(
                     FIND_CLIENT_BY_ID,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            statement.setString(1, clientId.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.first()
                        ? Optional.of(getClient(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new CustomSqlException(e.getMessage(), e);
        }
    }

    private Client getClient(ResultSet resultSet) throws SQLException {
        return Client.builder()
                .id(UUID.fromString(resultSet.getString(ID)))
                .name(resultSet.getString(NAME))
                .passportNumber(resultSet.getString(PASSPORT_NUMBER))
                .build();
    }
}
