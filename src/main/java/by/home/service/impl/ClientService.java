package by.home.service.impl;

import by.home.aop.api.Loggable;
import by.home.dao.api.IClientDao;
import by.home.dao.entity.Client;
import by.home.data.exception.ClientNotFoundException;
import by.home.service.api.IClientService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * сервисный класс для операций с сущностью клиент
 */
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final IClientDao clientDao;

    @Override
    @Loggable
    public Client getClientById(UUID clientId) {
        return this.clientDao.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("client not found"));
    }
}
