package ru.otus;

import ru.otus.controller.ATM;
import ru.otus.entity.Banknote;
import ru.otus.factory.StaticFactory;

public class Main {
    public static void main(String[] args) {
        ATM atm = (ATM) StaticFactory.getObject("ATM");

        atm.putBanknotes(Banknote.FIFTY);
        atm.putBanknotes(Banknote.TEN);
        atm.putBanknotes(Banknote.FIVE_THOUSAND);
        atm.putBanknotes(Banknote.FIVE_THOUSAND);

        System.out.println(atm.getSum());

        var result = atm.getBanknote(10000);
        System.out.println(result);
        System.out.println(atm.getSum());

        StaticFactory.destroyMethod();
    }
}
