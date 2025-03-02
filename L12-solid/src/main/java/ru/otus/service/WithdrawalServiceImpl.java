package ru.otus.service;

import java.util.*;
import ru.otus.database.Database;
import ru.otus.entity.Banknote;
import ru.otus.factory.StaticFactory;
import ru.otus.service.interfaces.WithdrawalService;

public class WithdrawalServiceImpl implements WithdrawalService {
    private final Database database;

    public WithdrawalServiceImpl() {
        this.database = (Database) StaticFactory.getObject("Database");
    }

    @Override
    public Map<Banknote, Integer> withdrawal(int sum) {
        var data = database.getData();
        if (data.isEmpty()) {
            throw new UnsupportedOperationException("Упс, банкомат пуст");
        }
        var sortedBanknote = new ArrayList<>(data.keySet());

        sortedBanknote.sort((b1, b2) -> Integer.compare(b2.getNominalValue(), b1.getNominalValue()));

        int minNominal = sortedBanknote.getLast().getNominalValue();
        if (sum < minNominal) throw new IllegalArgumentException("Сумма не поддерживается");

        var result = new HashMap<Banknote, Integer>();

        for (Banknote b : sortedBanknote) {
            int nominal = b.getNominalValue();
            if (nominal > sum) continue;
            int countBanknote = data.get(b);
            int needed = sum / nominal;
            int toUse = Math.min(needed, countBanknote);

            if (toUse > 0) {
                result.put(b, toUse);
                sum -= toUse * nominal;
                data.put(b, data.get(b) - toUse);
            }
        }
        if (sum > 0) throw new IllegalArgumentException("Сумма не поддерживается");

        return result;
    }
}
