package ru.otus.mapper;

import ru.otus.crm.dto.ClientDtoRq;
import ru.otus.crm.dto.ClientDtoRs;
import ru.otus.crm.model.Client;

public interface ClientMapper {
    ClientDtoRs toResponse(Client client);

    Client toEntity(ClientDtoRq clientDto);
}
