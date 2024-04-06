package org.solution.enums;

import java.math.BigDecimal;

public enum CCOptions{
    ARS(BigDecimal.valueOf(1000.00)),   //1 USD = 43.47 ARS
    CLP(BigDecimal.valueOf(909.64)),  //1 USD = 750.65 CLP
    USD(BigDecimal.valueOf(1.0)),     //1 USD = 1 USD
    EUR(BigDecimal.valueOf(0.92)),    //1 USD = 0.89 EUR
    TRY(BigDecimal.valueOf(30.25) ),    //1 USD = 9.55 TRY
    GBP(BigDecimal.valueOf(0.79)),    //1 USD = 0.73 GBP
    BACK(BigDecimal.valueOf(-1));


    private final BigDecimal exchangeRate;

    CCOptions(BigDecimal exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

    public BigDecimal getExchangeRateFromUSD() {
            return exchangeRate;
        }

    public static CCOptions fromIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("There is no operation with index: " + index);
        }
    }
    public static CCOptions fromString(String string) {
        try {
            return CCOptions.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + string);
        }
    }
    public static void showValues() {
        for (int i = 0; i < values().length; i++) {
            System.out.println(i + ") " + values()[i]);
        }
    }

}
