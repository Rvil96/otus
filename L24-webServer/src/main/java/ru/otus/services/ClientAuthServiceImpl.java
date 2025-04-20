package ru.otus.services;

import ru.otus.repository.crm.service.DBServiceClient;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final DBServiceClient dbServiceClient;

    public ClientAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient
                .getClientByLogin(login)
                .map(client -> client.getPassword().equals(password))
                .orElse(false);
    }
}
