package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.KeyWrapper;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

        var clientCash = new MyCache<KeyWrapper<Long>, Client>();
        clientCash.addListener(new HwListener<KeyWrapper<Long>, Client>() {
            @Override
            public void notify(KeyWrapper<Long> key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, clientCash);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
        var listClient = dbServiceClient.findAll();
        log.info("Clients list: {}", listClient);

        // Проверка кеша

        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            clients.add(dbServiceClient.saveClient(new Client(UUID.randomUUID().toString())));
        }

        List<Long> ids = new ArrayList<>();
        long startTimeWithCache = System.currentTimeMillis();
        for (Client client : clients) {
            dbServiceClient.getClient(client.getId());
            ids.add(client.getId());
        }
        long ednTimeWithCache = startTimeWithCache - System.currentTimeMillis();

        clients = null;
        System.gc();

        long startTimeWithoutCache = System.currentTimeMillis();
        for (Long id : ids) {
            dbServiceClient.getClient(id);
        }
        long ednTimeWithoutCache = startTimeWithoutCache - System.currentTimeMillis();

        log.info("Time with cache {}", ednTimeWithCache);
        log.info("Time without cache {}", ednTimeWithoutCache);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
