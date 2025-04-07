package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.KeyWrapper;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private HwCache<KeyWrapper<Long>, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    public DbServiceClientImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Client> dataTemplate,
            HwCache<KeyWrapper<Long>, Client> cache) {
        this.dataTemplate = dataTemplate;
        this.transactionRunner = transactionRunner;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var dbClient = transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
        if (isCacheEnabled()) {
            cache.put(new KeyWrapper<>(dbClient.getId()), dbClient);
        }
        return dbClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (isCacheEnabled()) {
            Optional<Client> optionalClient = Optional.ofNullable(cache.get(new KeyWrapper<>(id)));
            if (optionalClient.isPresent()) {
                return optionalClient;
            }
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private boolean isCacheEnabled() {
        return cache != null;
    }
}
