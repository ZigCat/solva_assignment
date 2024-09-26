package com.github.zigcat.solva_assignment.entities.enums;

public enum CurrencyPair {
    KZT_USD("KZT/USD"),
    RUB_USD("RUB/USD");

    private final String pair;

    CurrencyPair(String pair) {
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

    @Override
    public String toString() {
        return pair;
    }
}
