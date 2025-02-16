package ru.otus.service;

import ru.otus.database.Database;
import ru.otus.entity.Banknote;
import ru.otus.factory.StaticFactory;
import ru.otus.service.interfaces.AdditionService;

public class AdditionServiceImpl implements AdditionService {

    private final Database database;

    public AdditionServiceImpl() {
        this.database = (Database) StaticFactory.getObject("Database");
    }

    @Override
    public void putBanknote(Banknote banknote) {
        var data = database.getData();
        data.merge(banknote, 1, Integer::sum);
    }
}
