package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.KeyWrapper;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private HwCache<KeyWrapper<Long>, Manager> cache;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
    }

    public DbServiceManagerImpl(
            DataTemplate<Manager> managerDataTemplate,
            TransactionRunner transactionRunner,
            HwCache<KeyWrapper<Long>, Manager> cache) {
        this.managerDataTemplate = managerDataTemplate;
        this.transactionRunner = transactionRunner;
        this.cache = cache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        if (isCacheEnabled()) {
            cache.put(new KeyWrapper<>(manager.getNo()), manager);
        }
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        if (isCacheEnabled()) {
            Optional<Manager> optionalManager = Optional.ofNullable(cache.get(new KeyWrapper<>(no)));
            if (optionalManager.isPresent()) {
                return optionalManager;
            }
        }
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }

    private boolean isCacheEnabled() {
        return cache != null;
    }
}
