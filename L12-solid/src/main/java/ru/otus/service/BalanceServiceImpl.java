package ru.otus.service;

import ru.otus.database.Database;
import ru.otus.factory.StaticFactory;
import ru.otus.service.interfaces.BalanceService;

public class BalanceServiceImpl implements BalanceService {
    private final Database database;

    public BalanceServiceImpl() {
        this.database = (Database) StaticFactory.getObject("Database");
    }

    @Override
    public int getBalance() {
        var data = database.getData();

        if (data.isEmpty()) return 0;

        return data.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getNominalValue() * entry.getValue())
                .sum();
    }
}
