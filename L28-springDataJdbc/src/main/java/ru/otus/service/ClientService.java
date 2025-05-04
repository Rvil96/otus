package ru.otus.service;

import java.util.List;
import ru.otus.crm.dto.ClientDtoRq;
import ru.otus.crm.dto.ClientDtoRs;

public interface ClientService {
    void createClient(ClientDtoRq client);

    List<ClientDtoRs> findAll();
}
