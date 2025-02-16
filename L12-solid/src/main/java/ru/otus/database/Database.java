package ru.otus.database;

import java.util.Map;
import ru.otus.entity.Banknote;

public interface Database {
    Map<Banknote, Integer> getData();
}
