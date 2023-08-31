package by.home.dao.api;

import by.home.dao.entity.Client;

import java.util.Optional;
import java.util.UUID;

public interface IClientDao {

    Optional<Client> findById(UUID clientId);
}
