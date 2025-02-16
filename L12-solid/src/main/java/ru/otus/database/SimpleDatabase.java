package ru.otus.database;

import java.util.*;
import ru.otus.entity.Banknote;

public class SimpleDatabase implements Database {
    private final Map<Banknote, Integer> database;

    public SimpleDatabase() {
        this.database = new EnumMap<>(Banknote.class);
    }

    @Override
    public Map<Banknote, Integer> getData() {
        return database;
    }

    public void cleanDb() { // for test
        database.clear();
    }
}
