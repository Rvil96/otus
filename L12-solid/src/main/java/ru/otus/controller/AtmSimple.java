package ru.otus.controller;

import static ru.otus.factory.StaticFactory.*;

import java.util.Map;
import ru.otus.entity.Banknote;
import ru.otus.service.interfaces.AdditionService;
import ru.otus.service.interfaces.BalanceService;
import ru.otus.service.interfaces.WithdrawalService;

public class AtmSimple implements ATM {
    private final BalanceService balanceService;
    private final AdditionService additionService;
    private final WithdrawalService withdrawalService;

    public AtmSimple() {
        this.balanceService = (BalanceService) getObject("BalanceService");
        this.additionService = (AdditionService) getObject("AdditionService");
        this.withdrawalService = (WithdrawalService) getObject("WithdrawalService");
    }

    @Override
    public int getSum() {
        return balanceService.getBalance();
    }

    @Override
    public void putBanknotes(Banknote banknote) {
        additionService.putBanknote(banknote);
    }

    @Override
    public Map<Banknote, Integer> getBanknote(int sum) {
        return withdrawalService.withdrawal(sum);
    }
}
