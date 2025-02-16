package ru.otus.factory;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.controller.AtmSimple;
import ru.otus.database.SimpleDatabase;
import ru.otus.service.AdditionServiceImpl;
import ru.otus.service.BalanceServiceImpl;
import ru.otus.service.WithdrawalServiceImpl;

public class StaticFactory {
    private static final Logger log = LoggerFactory.getLogger(StaticFactory.class);
    private static final Map<String, Object> context = new HashMap<>();

    private StaticFactory() {}

    static {
        log.info("Инициализация контекста");

        initDatabase();
        initService();
        initATM();

        log.info("Контекст поднят");
    }

    private static void initDatabase() {
        context.put("Database", new SimpleDatabase());
        log.info("БД создана");
    }

    private static void initService() {
        context.put("BalanceService", new BalanceServiceImpl());
        context.put("AdditionService", new AdditionServiceImpl());
        context.put("WithdrawalService", new WithdrawalServiceImpl());

        log.info("Сервисы созданы");
    }

    private static void initATM() {
        context.put("ATM", new AtmSimple());
        log.info("АТМ создан");
    }

    public static Object getObject(String nameObject) {
        return context.get(nameObject);
    }

    public static void cleanDb() { // for test
        SimpleDatabase database = (SimpleDatabase) StaticFactory.getObject("Database");
        database.cleanDb();
    }

    public static void destroyMethod() {
        context.clear();
        log.info("Контекст очищен");
    }
}
