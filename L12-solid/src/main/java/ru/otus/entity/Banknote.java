package ru.otus.entity;

public enum Banknote {
    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private final int nominalValue;

    Banknote(int nominalValue) {
        this.nominalValue = nominalValue;
    }

    public int getNominalValue() {
        return nominalValue;
    }
}
