package ru.otus.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.crm.dto.ClientDtoRq;
import ru.otus.crm.dto.ClientDtoRs;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.mapper.ClientMapper;
import ru.otus.sessionmanager.TransactionManagerSpring;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final TransactionManagerSpring transactionManager;

    @Override
    public void createClient(ClientDtoRq client) {
        var clientEntity = clientMapper.toEntity(client);
        transactionManager.doInTransaction(() -> clientRepository.save(clientEntity));
    }

    @Override
    public List<ClientDtoRs> findAll() {
        var clientList = clientRepository.findAll();
        return clientList.stream().map(clientMapper::toResponse).toList();
    }
}
