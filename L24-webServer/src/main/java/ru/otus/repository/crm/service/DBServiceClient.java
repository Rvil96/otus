package ru.otus.repository.crm.service;

import java.util.List;
import java.util.Optional;
import ru.otus.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    Optional<Client> getClientByLogin(String login);
}
