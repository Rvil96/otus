package ru.otus.controller;

import java.util.Map;
import ru.otus.entity.Banknote;

public interface ATM {
    void putBanknotes(Banknote banknote);

    Map<Banknote, Integer> getBanknote(int sum);

    int getSum();
}
