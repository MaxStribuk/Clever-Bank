package by.home.service.api;

import by.home.dao.entity.Client;

import java.util.UUID;

public interface IClientService {

    Client getClientById(UUID clientId);
}
