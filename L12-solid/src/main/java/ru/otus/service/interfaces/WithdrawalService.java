package ru.otus.service.interfaces;

import java.util.Map;
import ru.otus.entity.Banknote;

public interface WithdrawalService {
    Map<Banknote, Integer> withdrawal(int sum) throws IllegalArgumentException;
}
